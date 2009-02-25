// Author: Daniel Burstyn
#ifndef BUTTON_H_
#define BUTTON_H_

#include "Component.h"

namespace cs349
{

class Button : public Component
{
 private:
  bool disabled;
  bool pressed;
  bool inverted;

 protected:
  const char* text;
  virtual void PaintComponent(Graphics* g);
  void Invert();

 public:
  Button(const char* text)
      : text(text),
        pressed(false),
        inverted(false),
        disabled(false) {}
  virtual ~Button() {}

  virtual bool HandleMouseEvent(const MouseEvent& e);

  virtual void UncaughtMouseUp();
  virtual const char* GetText();

  virtual bool IsDisabled() {return this->disabled;}
  virtual void Enable() {this->disabled = false;}
  virtual void Disable() {this->disabled = true;}
  // Implement in extension
  virtual void OnClick();
};

class ToggleButton : public Button
{
 private:
  const char* alttext;
  bool toggled;

 protected:
  virtual void PaintComponent(Graphics* g);

 public:

  virtual void OnClick();
  ToggleButton(const char* text, const char* alttext)
      : Button(text), alttext(alttext), toggled(false) {}
  virtual const char* GetText();

  virtual bool GetToggled() {return this->toggled;}
  virtual void SetToggled(bool toggled) {this->toggled = toggled;}
  virtual void SilentToggle();
  virtual void TriggerToggle();

  // Implement in extension
  virtual void OnToggleOn();
  virtual void OnToggleOff();
};

}  // cs349

#endif /*BUTTON_H_*/
