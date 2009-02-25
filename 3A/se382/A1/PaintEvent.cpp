#include "PaintEvent.h"
#include "XWindow.h"
#include "Rectangle.h"

namespace cs349
{

//TODO: implement

PaintEvent::PaintEvent(XWindow* window, const Rectangle& damagedArea)
    : ComponentEvent(window), damagedArea(damagedArea) {}

void PaintEvent::DispatchEvent() {
  this->GetWindow()->HandlePaintEvent(*this);
}

Rectangle PaintEvent::GetDamagedArea() const {
  return damagedArea;
}

}
