// Author: Daniel Burstyn

#ifndef PAINT_AGGREGATOR_H_
#define PAINT_AGGREGATOR_H_

#include "PaintEvent.h"
#include <vector>

namespace cs349
{

class PaintAggregator {
 private:
  vector<PaintEvent*> events;
  bool has_events;

 public:
  PaintAggregator() : has_events(false){}

  bool HasEvents() {return has_events;}

  void AddEvent(PaintEvent* e);
  PaintEvent* Aggregate();
};

}  // cs349

#endif /*PAINT_AGGREGATOR_H_*/
