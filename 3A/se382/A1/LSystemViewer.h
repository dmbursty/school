// Author: Daniel Burstyn
#ifndef LSYSTEMVIEWER_H_
#define LSYSTEMVIEWER_H_

#include "lsystem/IteratedSystem.h"
#include "IteratedSystemsManager.h"
#include "Component.h"

namespace cs349
{

class LSystemViewer : public Component
{
 private:
  bool pressed;
  Point pan_reference;
  Point pan;
  IteratedSystem* system;
  int zoom;

 protected:
  virtual void PaintComponent(Graphics* g);
  virtual bool HandleMouseEvent(const MouseEvent& e);

 public:
  LSystemViewer();
  virtual ~LSystemViewer() {}
  IteratedSystem* GetSystem() { return system; }
  void SetSystem(IteratedSystem* system);

  virtual void MouseMoved(const Point& p);
  virtual void UncaughtMouseUp();
  void ResetView();
};

}  // cs349

#endif /*LSYSTEMVIEWER_H_*/
