// Author: Daniel Burstyn
#ifndef LSYSTEMCONTROLLER_H_
#define LSYSTEMCONTROLLER_H_

#include "Slider.h"
#include "Button.h"
#include "Label.h"
#include "LSystemViewer.h"
#include "Timer.h"

namespace cs349
{

class LSystemController : public TimerListener {
 private:
  Label* label;
  LSystemViewer* lsv;
  Timer* timer;
  Button* back_button;
  Button* step_button;
  ToggleButton* pp_button; // Play/Pause
  Slider* s_slider; // System slider
  bool timermem;

  // Check system and update controls accordingly
  void CheckSystem();

 public:
  LSystemController(LSystemViewer* lsv);

  void SetStepButton(Button* button) {this->step_button = button;}
  void SetBackButton(Button* button) {this->back_button = button;}
  void SetPPButton(ToggleButton* button) {this->pp_button = button;}
  void SetSSlider(Slider* slider) {this->s_slider = slider;}
  void SetLabel(Label* label) {this->label = label;}

  void Step();
  void Back();
  void Restart();
  void Pause();
  void Play();
  void IterateToStep(int step);
  void SetTimerSpeed(int speed);

  void PauseTimer();
  void UnpauseTimer();

  int GetCurrentStep() {return lsv->GetSystem()->GetNumIterations();}
  int GetMaxSteps() {return lsv->GetSystem()->GetMaxNumIterations();}

  void UseSystem(IteratedSystem* system);
  void HandleTimerEvent(const TimerEvent& e);
};

}  // cs349

#endif /*LSYSTEMCONTROLLER_H_*/
