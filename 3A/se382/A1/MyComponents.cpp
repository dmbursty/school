#include "MyComponents.h"
#include "Application.h"
#include "Label.h"

namespace cs349 {

void StepButton::OnClick() {
  Application::GetInstance()->GetLSystemController()->Step();
  Application::GetInstance()->GetLSystemController()->Pause();
}

void RestartButton::OnClick() {
  Application::GetInstance()->GetLSystemController()->Restart();
}

void BackButton::OnClick() {
  Application::GetInstance()->GetLSystemController()->Back();
  Application::GetInstance()->GetLSystemController()->Pause();
}

PlayPauseButton::PlayPauseButton() : ToggleButton("Pause", "Play") {
  // Start playing at the beginning
  // To change this, see LSystemController constructor
}

void PlayPauseButton::OnToggleOn() {
  // Pause
  Application::GetInstance()->GetLSystemController()->Pause();
}

void PlayPauseButton::OnToggleOff() {
  // Play
  Application::GetInstance()->GetLSystemController()->Play();
}

void AboutButton::OnClick() {
  Application::GetInstance()->StartAboutMode();
}

SystemSlider::SystemSlider() : Slider() {
  this->SetNumTicks(Application::GetInstance()->GetLSystemController()->GetMaxSteps());
}

void SystemSlider::OnSelectTick(int i) {
  Application::GetInstance()->GetLSystemController()->IterateToStep(i);
}

AnimationSlider::AnimationSlider() : Slider() {
  this->SetNumTicks(20);
  this->JumpToTick(8);
}

void AnimationSlider::OnSelectTick(int i) {
  int speed = 1100 - 50 * i;
  Application::GetInstance()->GetLSystemController()->SetTimerSpeed(speed);
  Repaint();
}

AboutBox::AboutBox() : Component() {
  // Fake the local bounds for now
  Rectangle lb = Rectangle(0,0,200,75);
  Label* l;

  l = new Label("Welcome to the L-System Viewer!");
  l->SetBounds(Rectangle(0,10,lb.width,10));
  this->AddComponent(l);

  l = new Label("By: Max Burstyn");
  l->SetBounds(Rectangle(0,23,lb.width,10));
  this->AddComponent(l);

  l = new Label("Click or press ESC to resume.");
  l->SetBounds(Rectangle(0,55,lb.width,10));
  this->AddComponent(l);
}

void AboutBox::PaintComponent(Graphics* g) {
  if (this->IsVisible()) {
    Rectangle localbounds;
    GetLocalBounds(&localbounds);
    g->Invert();
    g->FillRect(localbounds);
    g->Invert();
    g->DrawRect(localbounds);
  }
}

}  // cs349
