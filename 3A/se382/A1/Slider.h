// Author: Daniel Burstyn
#ifndef SLIDER_H_
#define SLIDER_H_

#include "Component.h"

namespace cs349
{

class Slider : public Component
{
 private:
  int numTicks;
  int currentTick;
  bool pressed;

 protected:
  virtual void PaintComponent(Graphics* g);

 public:
  Slider();
  ~Slider() {}

  virtual bool HandleMouseEvent(const MouseEvent& e);
  virtual void MouseMoved(const Point& p);
  virtual void UncaughtMouseUp();

  virtual void SetNumTicks(int i);

  virtual void JumpToTick(int i);
  virtual int GetTick() {return this->currentTick;}

  // Implement these
  virtual void OnSelectTick(int i);
};

}  // cs349

#endif /*SLIDER_H_*/
