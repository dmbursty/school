// Author: Michael Terry

#include "Timer.h"
#include <algorithm>
#include "Application.h"

#include <time.h>

namespace cs349
{

Timer::Timer(unsigned long delayMS, bool repeat, TimerListener* l)
{
  this->delayMS = delayMS;
  this->repeat  = repeat;
  this->timerListeners.push_back(l);
  this->running = false;
}

Timer::~Timer()
{
}

void Timer::SetDelay(unsigned long delay) {
  this->delayMS = delay;
  if (this->running) {
    this->ServiceTimer();
  }
}

void Timer::AddTimerListener(TimerListener* l)
{
  this->timerListeners.push_back(l);
}

unsigned long Timer::GetCurrentTime()
{
  int result;
  struct timespec tp;
  result = clock_gettime(CLOCK_REALTIME, &tp);
  return tp.tv_sec * 1000 + tp.tv_nsec / 1000000;
}

bool Timer::IsRunning() const
{
  return this->running;
}

void Timer::RemoveTimerListener(TimerListener* l)
{
  remove(this->timerListeners.begin(), this->timerListeners.end(), l);
}

void Timer::ServiceTimer()
{
  if (!this->running) {
    return;
  }
  unsigned long elapsedTimeMS = GetCurrentTime() - this->lastTime;
  if (elapsedTimeMS > this->delayMS) {
    this->lastTime = GetCurrentTime();
    Application* app = Application::GetInstance();
    app->AddEventToQueue(new TimerEvent(this));
    if (!this->repeat) {
      this->Stop();
    }
  }
}

void Timer::Start()
{
  if (!this->running) {
    Application::GetInstance()->AddTimer(this);
    this->lastTime = GetCurrentTime();
    this->running = true;
  }
}

void Timer::Stop()
{
  if (this->running) {
    this->running = false;
    Application::GetInstance()->RemoveTimer(this);
  }
}

}
