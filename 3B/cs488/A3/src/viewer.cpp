#include "viewer.hpp"
#include "algebra.hpp"
#include "trackball.hpp"
#include <iostream>
#include <stack>
#include <math.h>
#include <GL/gl.h>
#include <GL/glu.h>

Viewer::Viewer(SceneNode* root) : root(root), mode(POSITION), buttons(0)
{
  Glib::RefPtr<Gdk::GL::Config> glconfig;

  // Ask for an OpenGL Setup with
  //  - red, green and blue component colour
  //  - a depth buffer to avoid things overlapping wrongly
  //  - double-buffered rendering to avoid tearing/flickering
  glconfig = Gdk::GL::Config::create(Gdk::GL::MODE_RGB |
                                     Gdk::GL::MODE_DEPTH |
                                     Gdk::GL::MODE_DOUBLE);
  if (glconfig == 0) {
    // If we can't get this configuration, die
    std::cerr << "Unable to setup OpenGL Configuration!" << std::endl;
    abort();
  }

  // Accept the configuration
  set_gl_capability(glconfig);

  // Register the fact that we want to receive these events
  add_events(Gdk::BUTTON1_MOTION_MASK    |
             Gdk::BUTTON2_MOTION_MASK    |
             Gdk::BUTTON3_MOTION_MASK    |
             Gdk::BUTTON_PRESS_MASK      |
             Gdk::BUTTON_RELEASE_MASK    |
             Gdk::VISIBILITY_NOTIFY_MASK);

  trackball = false;
  zbuffer = false;
  backface = false;
  frontface = false;

  // We have to pull out the root node's transformation so that we can
  // apply it before the trackball's transformations
  rootTransform = root->get_transform();
  // Clear the root node's transform
  root->set_transform(Matrix4x4());

  // Initialize current undo command
  currentCommand = new Command();
}

Viewer::~Viewer()
{
  // Nothing to do here right now.
}

void Viewer::invalidate()
{
  // Force a rerender
  Gtk::Allocation allocation = get_allocation();
  get_window()->invalidate_rect( allocation, false);
}

void Viewer::on_realize()
{
  // Do some OpenGL setup.
  // First, let the base class do whatever it needs to
  Gtk::GL::DrawingArea::on_realize();

  Glib::RefPtr<Gdk::GL::Drawable> gldrawable = get_gl_drawable();

  if (!gldrawable)
    return;

  if (!gldrawable->gl_begin(get_gl_context()))
    return;

  glShadeModel(GL_SMOOTH);
  glClearColor( 0.0, 0.0, 0.0, 0.0 );
  glDisable(GL_DEPTH_TEST);

  gldrawable->gl_end();
}

bool Viewer::on_expose_event(GdkEventExpose* event)
{
  Glib::RefPtr<Gdk::GL::Drawable> gldrawable = get_gl_drawable();

  if (!gldrawable) return false;

  if (!gldrawable->gl_begin(get_gl_context()))
    return false;

  // Set up for perspective drawing
  glMatrixMode(GL_PROJECTION);
  glLoadIdentity();
  glViewport(0, 0, get_width(), get_height());
  gluPerspective(40.0, (GLfloat)get_width()/(GLfloat)get_height(), 0.1, 1000.0);

  // change to model view for drawing
  glMatrixMode(GL_MODELVIEW);
  glLoadIdentity();

  // Clear framebuffer
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

  // Culling
  if (backface && frontface) {
    glEnable(GL_CULL_FACE);
    glCullFace(GL_FRONT_AND_BACK);
  } else if (backface) {
    glEnable(GL_CULL_FACE);
    glCullFace(GL_BACK);
  } else if (frontface) {
    glEnable(GL_CULL_FACE);
    glCullFace(GL_FRONT);
  } else {
    glDisable(GL_CULL_FACE);
  }

  // Z-Buffering
  if (zbuffer) {
    glEnable(GL_DEPTH_TEST);
  } else {
    glDisable(GL_DEPTH_TEST);
  }

  // Set up lighting
  glEnable(GL_LIGHTING);
  glEnable(GL_LIGHT0);
  glEnable(GL_NORMALIZE);
  GLfloat position[4] = {25.0, -5.0, 25.0, 1.0};
  GLfloat on[4] = {1.0, 1.0, 1.0, 1.0};
  GLfloat half[4] = {0.5, 0.5, 0.5, 1.0};
  GLfloat off[4] = {0.0, 0.0, 0.0, 1.0};
  glLightfv(GL_LIGHT0, GL_POSITION, position);
  glLightfv(GL_LIGHT0, GL_AMBIENT, on);
  glLightfv(GL_LIGHT0, GL_DIFFUSE, on);
  glLightfv(GL_LIGHT0, GL_SPECULAR, on);

  // Draw stuff
  drawScene(false);

  if (trackball) {
    glPushMatrix();
    draw_trackball_circle();
    glPopMatrix();
  }

  // Swap the contents of the front and back buffers so we see what we
  // just drew. This should only be done if double buffering is enabled.
  gldrawable->swap_buffers();

  gldrawable->gl_end();

  return true;
}

bool Viewer::on_configure_event(GdkEventConfigure* event)
{
  Glib::RefPtr<Gdk::GL::Drawable> gldrawable = get_gl_drawable();

  if (!gldrawable) return false;

  if (!gldrawable->gl_begin(get_gl_context()))
    return false;

  // Set up perspective projection, using current size and aspect
  // ratio of display

  glMatrixMode(GL_PROJECTION);
  glLoadIdentity();
  glViewport(0, 0, event->width, event->height);
  gluPerspective(40.0, (GLfloat)event->width/(GLfloat)event->height, 0.1, 1000.0);

  // Reset to modelview matrix mode

  glMatrixMode(GL_MODELVIEW);

  gldrawable->gl_end();

  return true;
}

bool Viewer::on_button_press_event(GdkEventButton* event)
{
  last_x = event->x;
  last_y = event->y;

  // update button bitstring
  switch(event->button) {
    case 1:
      buttons = buttons ^ 1; break;
    case 2:
      buttons = buttons ^ 2; break;
    case 3:
      buttons = buttons ^ 4; break;
  }

  if (mode == JOINT && event->button == 1) {
    // Pick primitive
    int bufsize = 100;
    unsigned int buffer[bufsize];

    startPicking(event->x, event->y, bufsize, buffer);
    drawScene(true);
    stopPicking(buffer);
  }

  return true;
}

bool Viewer::on_button_release_event(GdkEventButton* event)
{
  switch(event->button) {
    case 1:
      buttons = buttons ^ 1; break;
    case 2:
      buttons = buttons ^ 2; break;
    case 3:
      buttons = buttons ^ 4; break;
  }

  if (mode == JOINT &&
      (buttons == 0 || buttons == 1) &&
      (event->button == 2 || event->button == 3) &&
      !currentCommand->empty()) {
    // On release of buttons 2 & 3, restart the undo scope
    undoStack.add(currentCommand);
    updateLabel();
    currentCommand = new Command();
  }
  return true;
}

bool Viewer::on_motion_notify_event(GdkEventMotion* event)
{
  int diff_x = event->x - last_x;
  last_x = event->x;
  int diff_y = event->y - last_y;
  last_y = event->y;

  if (mode == JOINT) {
    if (buttons & 2) {
      std::set<JointNode*>::iterator it;
      for (it = selection.begin(); it != selection.end(); it++) {
        double diff = (*it)->rotate_x(diff_y);
        if (diff != 0) {
          MiniCommand* mc = new MiniCommand(*it, diff, true);
          currentCommand->add(mc);
        }
      }
      invalidate();
    }
    if (buttons & 4) {
      std::set<JointNode*>::iterator it;
      for (it = selection.begin(); it != selection.end(); it++) {
        double diff = (*it)->rotate_y(diff_x);
        if (diff != 0) {
          MiniCommand* mc = new MiniCommand(*it, diff, false);
          currentCommand->add(mc);
        }
      }
      invalidate();
    }
  } else if (mode == POSITION) {
    // Trackball
    if (buttons & 1) {
      Matrix4x4 trans(Vector4D(1, 0, 0, diff_x/30.0),
                      Vector4D(0, 1, 0, diff_y/-30.0),
                      Vector4D(0, 0, 1, 0),
                      Vector4D(0, 0, 0, 1));
      trackballTrans = trackballTrans * trans;
    }
    if (buttons & 2) {
      Matrix4x4 trans(Vector4D(1, 0, 0, 0),
                      Vector4D(0, 1, 0, 0),
                      Vector4D(0, 0, 1, diff_y/30.0),
                      Vector4D(0, 0, 0, 1));
      trackballTrans = trackballTrans * trans;
    }
    if (buttons & 4) {
      // Rotation
      trackballRotation(diff_x, diff_y);
    }
    invalidate();
  }
  return true;
}

void Viewer::trackballRotation(int diffx, int diffy) {
  float diameter = get_width() < get_height() ?
    (float)get_width() * 0.5 : (float)get_height() * 0.5;
  float x = last_x - (get_width() / 2);
  float y = last_y - (get_height() / 2);
  float oldx = last_x - diffx - (get_width() / 2);
  float oldy = last_y - diffy - (get_height() / 2);

  float vecX, vecY, vecZ;
  vCalcRotVec(x, y, oldx, oldy, diameter, &vecX, &vecY, &vecZ);

  Matrix4x4 m;
  vAxisRotMatrix(vecX, -vecY, vecZ, m);

  trackballRot = m * trackballRot;
}

void Viewer::draw_trackball_circle()
{
  int current_width = get_width();
  int current_height = get_height();

  // Set up for orthogonal drawing to draw a circle on screen.
  // You'll want to make the rest of the function conditional on
  // whether or not we want to draw the circle this time around.
  glMatrixMode(GL_PROJECTION);
  glLoadIdentity();
  glViewport(0, 0, current_width, current_height);
  glOrtho(0.0, (float)current_width,
           0.0, (float)current_height, -0.1, 0.1);

  // change to model view for drawing
  glMatrixMode(GL_MODELVIEW);

  // Reset modelview matrix
  glLoadIdentity();

  // draw a circle for use with the trackball
  glDisable(GL_LIGHTING);
  glEnable(GL_LINE_SMOOTH);
  glColor3f(1.0, 1.0, 1.0);
  double radius = current_width < current_height ?
    (float)current_width * 0.25 : (float)current_height * 0.25;
  glTranslated((float)current_width * 0.5, (float)current_height * 0.5, 0);
  glBegin(GL_LINE_LOOP);
  for(size_t i=0; i<40; ++i) {
    double cosine = radius * cos(i*2*M_PI/40);
    double sine = radius * sin(i*2*M_PI/40);
    glVertex2f(cosine, sine);
  }
  glEnd();
  glColor3f(0.0, 0.0, 0.0);
  glDisable(GL_LINE_SMOOTH);
}

void Viewer::startPicking(int x, int y, int bufsize, unsigned int* buffer) {
  GLint viewport[4];

  glSelectBuffer(bufsize, buffer);
  glRenderMode(GL_SELECT);
  glInitNames();
  glGetIntegerv(GL_VIEWPORT,viewport);
  glMatrixMode(GL_PROJECTION);
  glPushMatrix();
  glLoadIdentity();

  gluPickMatrix(x, viewport[3]-y, 1, 1, viewport);
  gluPerspective(40.0, (GLfloat)get_width()/(GLfloat)get_height(), 0.1, 1000.0);
  glMatrixMode(GL_MODELVIEW);
}

void Viewer::stopPicking(unsigned int* buffer) {
  // restoring the original projection matrix
  glMatrixMode(GL_PROJECTION);
  glPopMatrix();
  glMatrixMode(GL_MODELVIEW);
  glFlush();

  // returning to normal rendering mode
  int hits = glRenderMode(GL_RENDER);

  // if there are hits process them
  if (hits != 0) processHits(hits,buffer);
}

void Viewer::processHits(int hits, unsigned int* buffer)
{
   // Taken from lighthouse3d.com
   GLuint names, *ptr, minZ,*ptrNames, numberOfNames;

   ptr = (GLuint *) buffer;
   minZ = 0xffffffff;
   for (int i = 0; i < hits; i++) {
      names = *ptr;
    ptr++;
    if (*ptr < minZ) {
      numberOfNames = names;
      minZ = *ptr;
      ptrNames = ptr+2;
    }

    ptr += names+2;
  }

  // Find the selected joint
  JointNode* node = NULL;
  root->pick(node, *ptrNames);

  if (node != NULL) {
    // Pick the joint's children
    node->togglePicked();

    // Update the set of picked nodes
    std::set<JointNode*>::iterator it;
    it = selection.find(node);
    if (it == selection.end()) {
      selection.insert(node);
    } else {
      selection.erase(it);
    }
  }
  invalidate();
}


void Viewer::drawScene(bool picking) {
  glPushMatrix();
  glMultMatrixd(trackballTrans.transpose().getAsArray());
  glMultMatrixd(rootTransform.transpose().getAsArray());
  glMultMatrixd(trackballRot.transpose().getAsArray());
  root->walk_gl(picking);
  glPopMatrix();
}

void Viewer::updateLabel() {
  std::ostringstream oss;
  oss << "Undo: " << undoStack.getUndoSize();
  oss << "     ";
  oss << "Redo: " << undoStack.getRedoSize();
  label->set_text(oss.str());
}
