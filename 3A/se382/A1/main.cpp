#include "Application.h"
#include "XWindow.h"
#include "MyComponents.h"
#include "Label.h"
#include "LSystemViewer.h"
#include "LSystemChooser.h"
#include "IteratedSystemsManager.h"

#include <iostream>
#include <string.h>
using namespace std;
using namespace cs349;

class Clock :  public Component, public TimerListener
{
 private:
  string timeStr;

 public:
  Clock() {
    Timer* t = new Timer(500, true, this);
    t->Start();
  }

  virtual void PaintComponent(Graphics* g) {
    g->DrawText(Point(0,10), this->timeStr.c_str());
  }

  virtual bool HandleMouseEvent(const MouseEvent & e) {
    if (!Component::HandleMouseEvent(e)) {
      cout << "Got mouse event number: " << e.GetType() << endl;
    }
    return true;
  }

  virtual void HandleTimerEvent(const cs349::TimerEvent & e) {
    time_t rawtime;
      struct tm * timeinfo;

      time ( &rawtime );
      timeinfo = localtime ( &rawtime );

    timeStr = asctime(timeinfo);
    this->Repaint();
  }

};

int main ( int argc, char *argv[] )
{
  Application* app = Application::GetInstance();

  XWindow* window = new XWindow(app->GetXDisplay(), app->GetDefaultScreen(),
      "L-System Viewer", Rectangle(0, 0, 765, 600), argv, argc);

  RestartButton* restart_button = new RestartButton();
  LSystemViewer* lsv = new LSystemViewer();
  StepButton* step_button = new StepButton();
  BackButton* back_button = new BackButton();
  PlayPauseButton* pp_button = new PlayPauseButton();
  QuitButton* quit_button = new QuitButton();
  AboutButton* about_button = new AboutButton();
  LSystemChooser* lsc = new LSystemChooser();
  SystemSlider* s_slider = new SystemSlider();
  AnimationSlider* a_slider = new AnimationSlider();
  AboutBox* about_box = new AboutBox();
  Label* curr_system = new LeftLabel("Current System: Fractal Fern");
  Label* systems = new LeftLabel("Available Systems:");
  Label* iteration = new LeftLabel("Current Iteration:");
  Label* speed = new LeftLabel("Animation Speed:");

  app->GetLSystemController()->SetStepButton(step_button);
  app->GetLSystemController()->SetBackButton(back_button);
  app->GetLSystemController()->SetPPButton(pp_button);
  app->GetLSystemController()->SetSSlider(s_slider);
  app->GetLSystemController()->SetLabel(curr_system);
  app->SetAboutInfo(about_box);

  // Some consts
  int x1, x2;
  int y1, y2;
  x1 = 35;
  x2 = x1 + 470 + 20;
  y1 = 40;
  y2 = y1 + 420 + 20;
  int bh = 25; // Button height
  int spc = 35; // Button spacing (vertical)
  int bsp = 100;  // Button spacing (horz)
  int xs = 180; // Slider starting x

  curr_system->SetBounds(Rectangle(x1,y1-25,100,25));
  systems->SetBounds(Rectangle(x2,y1-25,120,25));
  iteration->SetBounds(Rectangle(x1,y2,100,25));
  speed->SetBounds(Rectangle(x1,y2+spc,100,25));

  lsv->SetBounds(Rectangle(x1, y1, x2-x1-20, y2-y1-20));
  lsc->SetBounds(Rectangle(x2, y1, 200, y2-y1-20));

  s_slider->SetBounds(Rectangle(xs, y2, x2-xs-20, bh));
  a_slider->SetBounds(Rectangle(xs, y2+spc, x2-xs-20, bh));

  back_button->SetBounds(Rectangle(x2, y2, 95, bh));
  step_button->SetBounds(Rectangle(x2+bsp, y2, 100, bh));

  pp_button->SetBounds(Rectangle(x2, y2+spc, 95, bh));
  restart_button->SetBounds(Rectangle(x2+bsp, y2+spc, 100, bh));

  about_button->SetBounds(Rectangle(x2, y2+2*spc, 95, bh));
  quit_button->SetBounds(Rectangle(x2+bsp, y2+2*spc, 100, bh));

  about_box->SetBounds(Rectangle(250, 250, 200, 75));

  window->AddComponent(curr_system);
  window->AddComponent(systems);
  window->AddComponent(iteration);
  window->AddComponent(speed);
  window->AddComponent(lsv);
  window->AddComponent(step_button);
  window->AddComponent(back_button);
  window->AddComponent(restart_button);
  window->AddComponent(pp_button);
  window->AddComponent(quit_button);
  window->AddComponent(about_button);
  window->AddComponent(lsc);
  window->AddComponent(s_slider);
  window->AddComponent(a_slider);
  window->AddComponent(about_box);

  about_box->SetVisible(false);

  // Draw the window the first time
  window->SetVisible(true);

  app->Run();

  delete app;
  delete window;

  return 0;
}


