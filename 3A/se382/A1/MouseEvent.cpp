// Author: Michael Terry

#include "MouseEvent.h"
#include "XWindow.h"

namespace cs349
{

MouseEvent::MouseEvent(XWindow* window, EventType type, int button, const Point & where) : ComponentEvent(window)
{
  this->type = type;
  this->where = where;
  this->button = button;
}

MouseEvent::~MouseEvent()
{
}

void MouseEvent::DispatchEvent()
{
  this->GetWindow()->HandleMouseEvent(*this);
}

MouseEvent::EventType MouseEvent::GetType() const
{
  return this->type;
}

Point MouseEvent::GetWhere() const
{
  return this->where;
}

}
