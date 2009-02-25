// Author: Michael Terry

#ifndef MOUSEEVENT_H_
#define MOUSEEVENT_H_

namespace cs349
{
  class MouseEvent;
}

#include "ComponentEvent.h"
#include "Point.h"

namespace cs349
{

/*
 * Encapsulates a mouse event.
 *
 * Coordinates are assumed to be in the coordinate system of
 * the component to which the event is delivered.
 *
 */
class MouseEvent :  public ComponentEvent
{
 public:
  enum EventType {
    buttonPress,
    buttonRelease,
    mouseMove,
    mouseDrag
  };
 private:
  EventType type;
  Point    where;
  int button;
 public:
  MouseEvent(XWindow* window, EventType type, int button, const Point & where);
  virtual ~MouseEvent();

  virtual void  DispatchEvent();
  EventType    GetType() const;
  Point       GetWhere() const;
  int         GetButton() const {return this->button;}
};

}

#endif /*MOUSEEVENT_H_*/
