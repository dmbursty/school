// Author: Daniel Burstyn

#include "PaintAggregator.h"
#include "Application.h"

namespace cs349
{

void PaintAggregator::AddEvent(PaintEvent* e) {
  this->has_events = true;
  this->events.push_back(e);
}

PaintEvent* PaintAggregator::Aggregate() {
  XWindow* window = events[0]->GetWindow();
  Rectangle ret = events[0]->GetDamagedArea();
  for (int i = 1; i < events.size(); i++) {
    ret = ret.GetUnion(events[i]->GetDamagedArea());
    delete events[i];
  }
  events.clear();
  this->has_events = false;
  return new PaintEvent(window, ret);
}

}  // cs349
