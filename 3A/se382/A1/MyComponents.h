// Author: Daniel Burstyn
#ifndef MYCOMPONENTS_H_
#define MYCOMPONENTS_H_

#include <cstdlib>
#include "Button.h"
#include "Slider.h"

namespace cs349 {

class StepButton : public Button
{
 public:
  StepButton() : Button("Step >") {}
  void OnClick();
};

class RestartButton : public Button
{
 public:
  RestartButton() : Button("Restart") {}
  void OnClick();
};

class BackButton : public Button
{
 public:
  BackButton() : Button("< Back") {}
  void OnClick();
};

class PlayPauseButton : public ToggleButton
{
 public:
  PlayPauseButton();
  void OnToggleOn(); // Pause
  void OnToggleOff(); // Play
};

class QuitButton : public Button
{
 public:
  QuitButton() : Button("Quit") {}
  void OnClick() {std::exit(0);}
};

class AboutButton : public Button
{
 public:
  AboutButton() : Button("About") {}
  void OnClick();
};

class SystemSlider : public Slider
{
 public:
  SystemSlider();
  void OnSelectTick(int i);
};

class AnimationSlider : public Slider
{
 public:
  AnimationSlider();
  void OnSelectTick(int i);
};

class AboutBox : public Component
{
 protected:
  virtual void PaintComponent(Graphics* g);

 public:
  AboutBox();
};

}  // cs349

#endif /*MYCOMPONENTS_H_*/
