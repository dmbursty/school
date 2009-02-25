#include "LSystemController.h"

namespace cs349
{

LSystemController::LSystemController(LSystemViewer* lsv) : lsv(lsv) {
  this->timer = new Timer(500, true, this);
  // Play by default, if you change this, change PlayPauseButton in MyButtons.cpp
  this->timer->Start();
}

void LSystemController::CheckSystem() {
  // Buttons
  int num = lsv->GetSystem()->GetNumIterations();
  int max = lsv->GetSystem()->GetMaxNumIterations();
  if (num == 0) {
    back_button->Disable();
    back_button->Repaint();
  } else if (back_button->IsDisabled()) {
    back_button->Enable();
    back_button->Repaint();
  }
  if (num == max) {
    step_button->Disable();
    step_button->Repaint();
  } else if (step_button->IsDisabled()) {
    step_button->Enable();
    step_button->Repaint();
  }

  // Sliders
  s_slider->JumpToTick(num);

  // Repaint
  s_slider->Repaint();
  lsv->Repaint();
}

void LSystemController::Step() {
  if (this->lsv->GetSystem()->IterateIfPossible()) {
    this->CheckSystem();
  }
}

void LSystemController::Back() {
  int it = this->lsv->GetSystem()->GetNumIterations();
  it -= 1;
  if (it < 0) return;
  this->IterateToStep(it);
}

void LSystemController::Restart() {
  this->lsv->GetSystem()->Reset();
  this->lsv->ResetView();
  this->CheckSystem();
}

void LSystemController::Pause() {
  this->timer->Stop();
  if (!pp_button->GetToggled()) {
    this->pp_button->SilentToggle();
    this->pp_button->Repaint();
  }
}

void LSystemController::Play() {
  this->timer->Start();
  if (pp_button->GetToggled()) {
    this->pp_button->SilentToggle();
    this->pp_button->Repaint();
  }
}

void LSystemController::IterateToStep(int step) {
  this->lsv->GetSystem()->IterateToStep(step);
  this->CheckSystem();
}

void LSystemController::SetTimerSpeed(int speed) {
  this->timer->SetDelay(speed);
}

void LSystemController::PauseTimer() {
  this->timermem = this->timer->IsRunning();
  this->timer->Stop();
}

void LSystemController::UnpauseTimer() {
  if (this->timermem) this->timer->Start();
}

void LSystemController::UseSystem(IteratedSystem* system) {
  lsv->SetSystem(system);
  label->SetText((string("Current System: ") + system->GetName()).c_str());
  this->s_slider->SetNumTicks(system->GetMaxNumIterations());
  this->CheckSystem();
}

void LSystemController::HandleTimerEvent(const TimerEvent& e) {
  this->Step();
}

}  // cs349
