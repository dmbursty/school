// Author: Michael Terry

#ifndef APPLICATION_H_
#define APPLICATION_H_

#include <X11/Xlib.h>

#include <vector>
using namespace std;

#include "Button.h"
#include "Event.h"
#include "PaintAggregator.h"
#include "Timer.h"
#include "LSystemController.h"

namespace cs349
{

/*
 * Application is the back-bone of your application. Application does the following:
 * - Initializes and maintains the connection with X
 * - Implements the event loop. In the event loop, new events are received from X, packaged up
 *   as our own custom Event objects, and added to our event queue. Events are then
 *   peeled off the event queue in FIFO order, and then asked to deliver themselves
 * - Maintains a list of timers that it regularly "services" so they can check whether they
 *   need to tell their listeners (clients) that a certain amount of time has elapsed. This
 *   allows the application to regularly update itself (e.g., for animations) without using
 *   threads. Single-threaded interactive applications are significantly easier to code
 *
 * Application is a singleton: There is only one instance of it in existence throughout
 * the lifetime of the application. As such, you don't directly instantiate the Application
 * object. Instead, you call Application::GetInstance().
 *
 * Important note: Normally, the rule of thumb with memory management is that if you allocate
 * something, you are responsible for deallocating it. As an exception to this rule, when
 * adding events to the Application event queue, the Application object will take care of
 * deleting the event after it has been delivered.
 */
class Application
{
 private:
  static Application* s_Instance;

  Display*       display;
  int            screen;
  vector<Event*> eventQueue;
  vector<Timer*> timers;
  bool           quit;
  bool           about;

  Button* mouse_up_needer;
  Slider* mouse_up_needer_sl;
  LSystemViewer* mouse_up_needer_lsv;
  Slider* mouse_move_needer;
  LSystemViewer* mouse_move_needer_lsv;
  LSystemController* lsystemcontroller;
  Component* about_info;
  PaintAggregator* paint_aggregator;

  Application();
  friend int ErrorHandler(Display* display);
  int HandleXError(Display* display);

 public:
  static Application* GetInstance();
  ~Application();

  /*
   * The following two methods retrieve the X Display and default screen for this X server
   */
  Display* GetXDisplay() const;
  int GetDefaultScreen() const;

  /*
   * The values representing black and white in this X server
   */
  unsigned long GetBlackColor() const;
  unsigned long GetWhiteColor() const;

  /*
   * Add a Timer for regular execution in the event loop
   */
  void AddTimer(Timer* timer);
  void RemoveTimer(Timer* timer);

  /*
   * Adds an event to the queue. The Application object will delete the memory pointed
   * to by the event after it has been delivered.
   */
  void AddEventToQueue(Event* e);

  /*
   * Starts the application event loop. This will run until "Quit" is called or the
   * user closes the window.
   */
  void Run();

  /*
   * Stops the event loop
   */
  void Quit();

  void SetAboutInfo(Component* c) {this->about_info = c;}
  void SubscribeForMouseUp(Button* c);
  void SubscribeForMouseUp(Slider* c);
  void SubscribeForMouseUp(LSystemViewer* c);
  void SubscribeForMouseMove(Slider* c);
  void SubscribeForMouseMove(LSystemViewer* c);
  void TriggerMouseUp() const;
  void SendMouseMove(const Point& p) const;
  void SetLSystemController(LSystemController* lsc);
  void MakeLSController(LSystemViewer* lsv);
  LSystemController* GetLSystemController();
  void StartAboutMode();
  void EndAboutMode();
  void HandleKeyEvent(const KeyEvent& e);
};

}

#endif /*APPLICATION_H_*/
