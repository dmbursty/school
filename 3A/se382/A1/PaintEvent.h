// Author: Michael Terry

#ifndef PAINTEVENT_H_
#define PAINTEVENT_H_

namespace cs349
{
  class PaintEvent;
}

#include "ComponentEvent.h"
#include "Rectangle.h"

namespace cs349
{

/*
 * Encapsulates a paint event. Damaged area refers to
 * what area of the window needs to be repainted.
 *
 * Coordinates are assumed to be in the coordinate system of
 * the component to which the event is delivered.
 *
 * TODO: You will need to implement this class's methods. Look
 * at MouseEvent and KeyEvent for examples on how to do it
 */
class PaintEvent :  public ComponentEvent
{
 private:
  Rectangle damagedArea;

 public:
  PaintEvent(XWindow* window, const Rectangle& damagedArea);
  virtual ~PaintEvent() {}

  virtual void DispatchEvent();

  Rectangle GetDamagedArea() const;
  virtual bool IsPaint() {return true;}
};

}

#endif /*PAINTEVENT_H_*/
