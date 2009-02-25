// Author: Michael Terry

#ifndef TIMER_H_
#define TIMER_H_

#include <vector>
#include <iostream>

using namespace std;

namespace cs349
{
  class Timer;
}

#include "TimerListener.h"
#include "Event.h"

namespace cs349
{

/*
 * A basic timer class that integrates with the Application object.
 * To use, instantiate a Timer with the delay specified in milliseconds,
 * whether the timer repeats, and the object that will be called whenever
 * the timer goes.
 *
 * You should not need to modify this class to make it work, though you
 * must ensure that the Application object regularly calls the "ServiceTimer"
 * method in its event loop to make sure it can check whether enough time
 * has elapsed to notify the listeners.
 *
 * You must explicitly call "Start" to start the timer.
 *
 * Caveats: Precision of the timer will be limited to how often "ServiceTimer"
 *          is called by Application in its event loop. The timer is not
 *          guaranteed to notify its listeners at the elapsed time specified.
 */
class Timer
{
 private:
  unsigned long delayMS;
  unsigned long lastTime;
  bool repeat;
  bool running;
  vector<TimerListener*> timerListeners;

  // Called by the Application from its event loop to enable the Timer to check whether
  // it needs to create a TimerEvent
  void ServiceTimer();

  static unsigned long GetCurrentTime();

  friend class Application;
  friend class TimerEvent;

 public:
  Timer(unsigned long delayMS, bool repeat, TimerListener* l);
  virtual ~Timer();

  void SetDelay(unsigned long delay);

  void AddTimerListener(TimerListener* l);
  void RemoveTimerListener(TimerListener* l);

  /*
   * Starts/stops the record
   */
  void Start();
  void Stop();

  bool IsRunning() const;

};

}

#endif /*TIMER_H_*/
