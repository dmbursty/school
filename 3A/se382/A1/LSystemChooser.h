// Author: Daniel Burstyn
#ifndef LSYSTEMCHOOSER_H_
#define LSYSTEMCHOOSER_H_

#include "Button.h"
#include "Component.h"
#include "IteratedSystemsManager.h"

namespace cs349 {

class ChooserItem;

class LSystemChooser : public Component
{
 private:
  ChooserItem* selected;

 protected:
  virtual void PaintComponent(Graphics* g);

 public:
  LSystemChooser();
  virtual ~LSystemChooser() {}
  virtual void SetBounds(const Rectangle& r);
  void SelectMe(ChooserItem* ci);
};

class ChooserItem : public ToggleButton
{
 private:
  LSystemChooser* chooser;
  IteratedSystem* system;
  string name;
 protected:
  virtual void PaintComponent(Graphics* g);
 public:
  ChooserItem(const string& name, LSystemChooser* chooser, IteratedSystem* system)
      : name(name), ToggleButton(name.c_str(), name.c_str()), chooser(chooser), system(system){}
  ~ChooserItem() {}

  void Select() {this->SetToggled(true);}
  void Deselect() {this->SetToggled(false);}
  virtual void OnToggleOn();
  virtual void OnToggleOff();
  IteratedSystem* GetSystem() { return this->system; }
};

}  // cs349

#endif /*LSYSTEMCHOOSER_H_*/
