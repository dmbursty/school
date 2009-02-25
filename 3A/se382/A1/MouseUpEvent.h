// Author: Daniel Burstyn

#ifndef MOUSEUPEVENT_H_
#define MOUSEUPEVENT_H_

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
class MouseUpEvent :  public ComponentEvent
{
 public:
 private:
 public:
  MouseUpEvent(XWindow* window) : ComponentEvent(window) {}
  virtual ~MouseUpEvent() {}

  virtual void  DispatchEvent();
};

}

#endif /*MOUSEUPEVENT_H_*/

