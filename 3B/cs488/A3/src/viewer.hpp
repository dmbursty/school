#ifndef CS488_VIEWER_HPP
#define CS488_VIEWER_HPP

#include <set>
#include <gtkmm.h>
#include <gtkglmm.h>
#include "scene.hpp"
#include "undo.hpp"

// The "main" OpenGL widget
class Viewer : public Gtk::GL::DrawingArea {
public:
  Viewer(SceneNode* root);
  virtual ~Viewer();

  // A useful function that forces this widget to rerender. If you
  // want to render a new frame, do not call on_expose_event
  // directly. Instead call this, which will cause an on_expose_event
  // call when the time is right.
  void invalidate();

  void set_label(Gtk::Label* label) {this->label = label;}

  void resetPosition() {trackballTrans = Matrix4x4(); invalidate();}
  void resetOrientation() {trackballRot = Matrix4x4(); invalidate();}
  void resetJoints() {root->reset(); undoStack.reset(); updateLabel();}
  void resetAll() {resetPosition(); resetOrientation(); resetJoints();}

  void toggleTrackball() { trackball = !trackball; invalidate(); }
  void toggleZBuffer() { zbuffer = !zbuffer; invalidate(); }
  void toggleBackface() { backface = !backface; invalidate(); }
  void toggleFrontface() { frontface = !frontface; invalidate(); }

  void modeJoint() { mode = JOINT; }
  void modePosition() { mode = POSITION; }

  void undo() {undoStack.undo(); updateLabel(); invalidate();}
  void redo() {undoStack.redo(); updateLabel(); invalidate();}
  
protected:

  // Events we implement
  // Note that we could use gtkmm's "signals and slots" mechanism // instead, but for many classes there's a convenient member
  // function one just needs to define that'll be called with the
  // event.

  // Called when GL is first initialized
  virtual void on_realize();
  // Called when our window needs to be redrawn
  virtual bool on_expose_event(GdkEventExpose* event);
  // Called when the window is resized
  virtual bool on_configure_event(GdkEventConfigure* event);
  // Called when a mouse button is pressed
  virtual bool on_button_press_event(GdkEventButton* event);
  // Called when a mouse button is released
  virtual bool on_button_release_event(GdkEventButton* event);
  // Called when the mouse moves
  virtual bool on_motion_notify_event(GdkEventMotion* event);

  // Draw a circle for the trackball, with OpenGL commands.
  // Assumes the context for the viewer is active.
  void draw_trackball_circle();
  
private:
  // Root
  SceneNode* root;
  Matrix4x4 rootTransform;
  // Selection set
  std::set<JointNode*> selection;
  // Trackball transforms
  Matrix4x4 trackballRot;
  Matrix4x4 trackballTrans;
  // Undo Stack
  UndoStack undoStack;
  Command* currentCommand;

  // Display flags
  bool trackball, zbuffer, backface, frontface;

  // Mode
  enum Mode {POSITION, JOINT};
  Mode mode;
  int buttons;
  int last_x, last_y;

  // Picking
  void startPicking(int x, int y, int bufsize, unsigned int* buffer);
  void stopPicking(unsigned int* buffer);
  void processHits(int hits, unsigned int* buffer);

  // Helpers
  void trackballRotation(int diffx, int diffy);
  void drawScene(bool picking);

  // The Label for the undo stack
  void updateLabel();
  Gtk::Label* label;
};

#endif
