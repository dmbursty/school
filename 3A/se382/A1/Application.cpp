// Author: Michael Terry

#include "Application.h"

#include <X11/Xutil.h>
#include <unistd.h>
#include <algorithm>

#include "Timer.h"
#include "XWindow.h"

namespace cs349
{

int ErrorHandler(Display* display);
Application* Application::s_Instance = NULL;

/*
 * This is a callback from X whenever it encounters a serious
 * error (such as when the user closes the window). You can
 * ignore it
 */
int ErrorHandler(Display* display)
{
  return Application::GetInstance()->HandleXError(display);
}

Application::Application()
{
  // Initialize the X display and get the default screen
  display = XOpenDisplay("");
  if (!display) {
    // Ideally, you should throw an exception here
    return;
  }
  screen = DefaultScreen(display);
  quit   = false;
  about  = false;
  mouse_up_needer = NULL;
  mouse_up_needer_sl = NULL;
  mouse_up_needer_lsv = NULL;
  mouse_move_needer = NULL;
  mouse_move_needer_lsv = NULL;
  paint_aggregator = new PaintAggregator();
}

Application::~Application()
{
  // Clear up any memory laying around
  for (vector<Event*>::iterator iter=this->eventQueue.begin();
      iter != this->eventQueue.end(); iter++) {
    delete(*iter);
  }
  this->eventQueue.clear();
}

void Application::AddEventToQueue(Event* e)
{
  this->eventQueue.push_back(e);
}

void Application::AddTimer(Timer* timer)
{
  timers.push_back(timer);
}

unsigned long Application::GetBlackColor() const
{
  return BlackPixel(this->display, this->screen);
}

int Application::GetDefaultScreen() const
{
  return this->screen;
}

Application* Application::GetInstance()
{
  if (!Application::s_Instance) {
    s_Instance = new Application();
  }
  return s_Instance;
}

unsigned long Application::GetWhiteColor() const
{
  return WhitePixel(this->display, this->screen);
}

Display* Application::GetXDisplay() const
{
  return this->display;
}

int Application::HandleXError(Display* display)
{
  // This method is called whenever there is a serious
  // X error. This method will be called when the user
  // manually closes the window. You could clean up
  // the event queue, release memory, and anything else
  // needed before the application quits, because once
  // returning from this method, X will forcibly terminate
  // the application
  return -1;
}

void Application::Quit()
{
  quit = true;
}

void Application::RemoveTimer(Timer* timer)
{
  // Remove a timer from our queue.
  // And yes, it *is* ridiculous that it takes this much to remove items from vectors in C++
  vector<Timer*>::iterator where = remove(this->timers.begin(), this->timers.end(), timer);
  this->timers.erase(where, this->timers.end());
}

void Application::SubscribeForMouseUp(Button* c) {
  this->mouse_up_needer = c;
}

void Application::SubscribeForMouseUp(Slider* c) {
  this->mouse_up_needer_sl = c;
}

void Application::SubscribeForMouseUp(LSystemViewer* c) {
  this->mouse_up_needer_lsv = c;
}

void Application::SubscribeForMouseMove(Slider* c) {
  this->mouse_move_needer = c;
}

void Application::SubscribeForMouseMove(LSystemViewer* c) {
  this->mouse_move_needer_lsv = c;
}

void Application::TriggerMouseUp() const {
  if (mouse_up_needer != NULL) {
    this->mouse_up_needer->UncaughtMouseUp();
  }
  if (mouse_up_needer_sl != NULL) {
    this->mouse_up_needer_sl->UncaughtMouseUp();
  }
  if (mouse_up_needer_lsv != NULL) {
    this->mouse_up_needer_lsv->UncaughtMouseUp();
  }
}

void Application::SendMouseMove(const Point& p) const {
  if (mouse_move_needer != NULL) {
    Rectangle bounds = this->mouse_move_needer->GetBounds();
    AffineTransform at = AffineTransform::GetTranslationMatrix(bounds.x, bounds.y);
    this->mouse_move_needer->MouseMoved(at.GetInverse().Transform(p));
  }
  if (mouse_move_needer_lsv != NULL) {
    Rectangle bounds = this->mouse_move_needer_lsv->GetBounds();
    AffineTransform at = AffineTransform::GetTranslationMatrix(bounds.x, bounds.y);
    this->mouse_move_needer_lsv->MouseMoved(at.GetInverse().Transform(p));
  }
}

void Application::SetLSystemController(LSystemController* lsc) {
  this->lsystemcontroller = lsc;
}

void Application::MakeLSController(LSystemViewer* lsv) {
  this->lsystemcontroller = new LSystemController(lsv);
}

LSystemController* Application::GetLSystemController() {
  return this->lsystemcontroller;
}

void Application::StartAboutMode() {
  if (!this->about) {
    this->lsystemcontroller->PauseTimer();
    this->about_info->SetVisible(true);
    this->about = true;
  }
}

void Application::EndAboutMode() {
  if (this->about) {
    this->lsystemcontroller->UnpauseTimer();
    this->about_info->SetVisible(false);
    this->about = false;
  }
}

void Application::HandleKeyEvent(const KeyEvent& e) {
  switch(e.GetChar()) {
    case 27:
        if (this->about) EndAboutMode();
        break;
    default:
        //cout << e.GetChar() << "\n";
        break;
  }
}

void Application::Run()
{
  XSetIOErrorHandler(ErrorHandler);

  // TODO
  /*
   * OK, showtime. This is the meat of the application class, and the place where you
   * will implement the event loop. The basic logic of the event loop is as follows:
   * - You need to determine whether there are any events pending in X lib. If so,
   *   grab it, package it up as one of our own events, then add it to our event queue
   * - Once you check X lib for events, then you should service the timers to allow
   *   them to update themselves (we already coded this for you)
   * - You should then take an event off of our event queue and dispatch it using the
   *   DispatchEvent
   * - You'll need to flush X using XFlush() at some point so it actually communicates
   *   changes to the X server
   * - And you should sleep if there is nothing left to do, so the app is not throttling
   *   the CPU
   *
   * One thing you're probably wondering is why we package up an event and add it to the event
   * queue, rather than dispatching it right away. The reason is that events may be added
   * to the queue by other objects (such as TimerEvents). Thus, to preserve proper temporal
   * ordering of events, you need to add events to the queue and dispath them one by one.
   */
  while (!this->quit) {

    // TODO (as described above)
    int num_events = XPending(display);
    for (int i = 0; i < num_events; i++) {
      XEvent event;
      XNextEvent(display, &event);
      if (event.type == Expose) {
        Rectangle damagedArea(event.xexpose.x, event.xexpose.y,
                              event.xexpose.width, event.xexpose.height);
        PaintEvent* wrappedEvent = new PaintEvent(
            XWindow::GetXWindowForWindow(event.xexpose.window),
            damagedArea);
        AddEventToQueue(wrappedEvent);
      } else if (event.type == ButtonPress ||
                 event.type == ButtonRelease) {
        this->EndAboutMode();
        MouseEvent::EventType event_type;
        switch(event.type) {
          case(ButtonPress): event_type = MouseEvent::buttonPress; break;
          case(ButtonRelease): event_type = MouseEvent::buttonRelease; break;
        }
        Point p(event.xbutton.x, event.xbutton.y);
        MouseEvent* wrappedEvent = new MouseEvent(
            XWindow::GetXWindowForWindow(event.xexpose.window),
            event_type, event.xbutton.button, p);
        AddEventToQueue(wrappedEvent);
      } else if (event.type == MotionNotify) {
        Point p(event.xmotion.x, event.xmotion.y);
        MouseEvent* wrappedEvent = new MouseEvent(
            XWindow::GetXWindowForWindow(event.xexpose.window),
            MouseEvent::mouseMove, event.xbutton.button, p);
        AddEventToQueue(wrappedEvent);
      } else if (event.type == KeyPress) {
        KeyEvent* wrappedEvent = new KeyEvent(
            XWindow::GetXWindowForWindow(event.xexpose.window),
            KeyEvent::keyPress, event.xkey);
        AddEventToQueue(wrappedEvent);
      }
    }

    // Give timers a chance to operate
    for (vector<Timer*>::iterator iter=this->timers.begin(); iter != this->timers.end(); iter++) {
      Timer* timer = (*iter);
      timer->ServiceTimer();
    }

    for(int i = 0; i < eventQueue.size(); i++) {
      if (eventQueue[i]->IsPaint()) {
        paint_aggregator->AddEvent(dynamic_cast<PaintEvent*>(eventQueue[i]));
      } else {
        eventQueue[i]->DispatchEvent();
        delete eventQueue[i];
      }
    }
    eventQueue.clear();

    if (paint_aggregator->HasEvents()) {
      PaintEvent* e = paint_aggregator->Aggregate();
      e->DispatchEvent();
      delete e;
    }

    XFlush(display);

    usleep(0.01 * 1000 * 1000);
    // TODO (as described above)
  }
}

}

