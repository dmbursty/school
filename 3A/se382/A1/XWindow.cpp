// Author: Michael Terry

#include "XWindow.h"

#include <algorithm>
#include <X11/Xutil.h>

#include "Application.h"

namespace cs349
{

map<Window, XWindow*> XWindow::windowMappings;
XWindow* XWindow::GetXWindowForWindow(Window window)
{
  return windowMappings[window];
}

XWindow::XWindow(Display* display, int screen, const char* title,
    const Rectangle& bounds, char** argv, int argc)
{
  this->display = display;

  XSizeHints hints; // Specifies size on creation
  hints.x = (int)bounds.x;
  hints.y = (int)bounds.y;
  hints.width = (int)bounds.width;
  hints.height = (int)bounds.height;
  hints.flags = PPosition | PSize;

  // Create the window
  this->window = XCreateSimpleWindow(this->display,
                    DefaultRootWindow(this->display),
                    hints.x,
                    hints.y,
                    hints.width,
                    hints.height,
                    0, // Window border width. Doesn't include width by window manager
                    Application::GetInstance()->GetBlackColor(),
                    Application::GetInstance()->GetWhiteColor());

  // Add ourselves to the mappings
  windowMappings[this->window] = this;

  // Set its basic properties
  XSetStandardProperties(this->display,
                         this->window,
                         title,
                         title,
                         None, // Pixmap for icon
                         argv,
                         argc,
                         &hints);

  // Select input events
  XSelectInput(this->display,
               this->window,
               ButtonPressMask
               | ButtonReleaseMask
               | KeyPressMask
               | KeyReleaseMask
               | ExposureMask
               | PointerMotionMask
               | ButtonMotionMask
               | StructureNotifyMask);

  GC gc = XCreateGC(display,
                    window,
                    0,
                    0);

  this->graphics = new Graphics(this->display, this->window, gc);
  this->graphics->SetForegroundColor(Application::GetInstance()->GetBlackColor());
  this->graphics->SetBackgroundColor(Application::GetInstance()->GetWhiteColor());

  // Now set our bounds to that which we were set
  XWindowAttributes attr;
  XGetWindowAttributes(this->display, this->window, &attr);
  Component::SetBounds(Rectangle(attr.x, attr.y, attr.width, attr.height));
}

XWindow::~XWindow()
{
  // Remove ourself from the mappings
  windowMappings.erase(windowMappings.find(this->window));
}

Graphics* XWindow::GetGraphics()
{
  return this->graphics;
}

XWindow* XWindow::GetParentWindow()
{
  return this;
}

void XWindow::HandlePaintEvent(const PaintEvent & e)
{
  // Set up Graphics
  this->graphics->SetTransform(AffineTransform::GetIdentityMatrix());
  this->graphics->SetClipRect(e.GetDamagedArea());
  this->Paint(this->graphics); // Calls Component's implementation
}

void XWindow::SetTitle(const char* title)
{
  XSizeHints hints;

  // Set its basic properties
  XSetStandardProperties(  display,
              this->window,
              title,
              title,
              None, // Pixmap for icon
              NULL,
              0,
              &hints);
}

// Overrides
void XWindow::SetBounds(const Rectangle & r)
{
  Component::SetBounds(r);
  // TODO: Set bounds of this window
}
void XWindow::SetLocation(const Point & p)
{
  Component::SetLocation(p);
  // TODO: Set location of this window
}
void XWindow::SetParent(Component* parent)
{
  // no-op. Ideally, should throw an error
}
void XWindow::SetSize(int width, int height)
{
  Component::SetSize(width, height);
  // TODO: Set size of this window
}
void XWindow::SetVisible(bool visible)
{
  Component::SetVisible(visible);
  if (visible) {
      XMapRaised( display, window );
  } else {
    // TODO: Hide window
  }
}

void XWindow::PaintComponent(Graphics* g)
{
  g->Invert();
  g->FillRect(g->GetClipRect());
  g->Invert();
}

}
