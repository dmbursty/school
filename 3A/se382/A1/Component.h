// Author: Michael Terry
#ifndef COMPONENT_H_
#define COMPONENT_H_

#include <vector>
using namespace std;

// Forward declarations
namespace cs349
{
  class XWindow;
}

// Project includes
#include "Graphics.h"
#include "KeyEvent.h"
#include "MouseEvent.h"
#include "PaintEvent.h"
#include "Point.h"
#include "Rectangle.h"

namespace cs349
{

/*
 * Component is one of the biggest classes in the framework as it is the
 * "thing" that users interact with: It represents anything that is visible,
 * and anything that can respond to mouse and key events. It is also the
 * base class for the XWindow class, which represents the actual window.
 *
 * Components can contain other components (AddComponent). This creates
 * your interactor tree, a hierarchical ordering of interactive components.
 * The root of this interactor tree is an XWindow object.
 *
 * Components have a location and bounds. This location is in the coordinate
 * system of its parent Component. For example, if component A is a child of
 * the window, and has a location of (5, 5), then that location is relative
 * to the top-left corner of the window's display area. If component A has a
 * child, component B, with a location of (10, 5), then its location in
 * the window is (15, 10), since (10, 5) is relative to the top-left corner
 * of its parent, component A, whose location is (5, 5).
 *
 * While the location of the component is relative to its parent's coordinate
 * system (i.e., its location), any painting and interaction in the component
 * is assumed to be in the coordinate system of the component itself. For example,
 * say the user clicks at point (17,20) on the window. In our example above, that point
 * corresponds to point (12,15) in component A's coordinate system. Thus, when receiving
 * a mouse event, the coordinates will be relative to the top-left corner of the component,
 * rather than in window coordinates.
 *
 * Because coordinates in events are assumed to be in the coordinate system of the
 * component itself, you are responsible for properly translating coordinates
 * to children coordinates if you pass events on to them. For example, in our example
 * above, (17,20) in the window coordinate system translates to (12,15) in component A,
 * which corresponds to (2, 10) in component B's coordinate system.
 *
 * Components respond to events via the HandleKeyEvent/HandleMouseEvent
 * methods. When the component needs to be painted, the Paint method is called with
 * a Graphics object. All drawing operations occur through the Graphics object.
 * Typically, what you do is write a single implementation of Paint, which sets
 * everything up with the Graphics context, then calls the internal, protected method,
 * PaintComponent. Subclasses then override PaintComponent to implement custom behavior.
 * In Paint, things like the background/foreground color are set in the Graphics object
 * prior to calling PaintComponent.
 *
 * Components should only paint themselves (and their children) when their visible
 * attribute is set to true.
 *
 * Whenever the component changes its visual presentation, it should call one of the "Repaint"
 * methods. The Repaint methods should add a PaintEvent event to the Application event queue to
 * be repainted in the future.
 */
class Component
{
 private:
  Component* parent;
  bool visible;
  unsigned long backgroundColor;
  unsigned long foregroundColor;

 protected:
  vector<Component*> children;
  Rectangle bounds;

  virtual void PaintComponent(Graphics* g);

 public:
  Component();
  virtual ~Component();

  virtual void       AddComponent(Component* child);

  virtual unsigned long  GetBackgroundColor() const;
  virtual Rectangle     GetBounds() const;
  virtual void     GetLocalBounds(Rectangle* rect) const;
  virtual unsigned long  GetForegroundColor() const;
  virtual Component*     GetParent();
  virtual XWindow*     GetParentWindow();

  virtual bool       HandleKeyEvent(const KeyEvent & e);
  virtual bool       HandleMouseEvent(const MouseEvent & e);

  /*
   * Tests whether a point, in the parent component's coordinate system,
   * is within this component's bounds.
   */
  virtual bool       IsPointInComponent(const Point & p) const;

  virtual bool       IsVisible() const;

  virtual void       Paint(Graphics* g);

  virtual void       Repaint();
  virtual void       Repaint(const Rectangle & r);

  virtual void       SetBackgroundColor(unsigned long c);
  virtual void       SetForegroundColor(unsigned long c);

  virtual void       SetBounds(const Rectangle & r);
  virtual void       SetLocation(const Point & p);
  virtual void       SetParent(Component* parent);
  virtual void       SetSize(int width, int height);
  virtual void       SetVisible(bool visible);

};

}
#endif /*COMPONENT_H_*/
