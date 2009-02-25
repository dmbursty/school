// Author: Michael Terry

#ifndef TIMEREVENT_H_
#define TIMEREVENT_H_

#include <vector>

using namespace std;

namespace cs349
{
	class TimerEvent;
}

#include "Event.h"
#include "Timer.h"

namespace cs349
{

/*
 * An event that is created whenever a timer "goes off".
 */
class TimerEvent : public Event
{
private:
	Timer* timer;
	TimerEvent(Timer* timer);
	
	friend class Timer;
	
public:
	virtual ~TimerEvent();
	virtual void DispatchEvent();
	Timer* GetTimer() const;
	bool IsPaint() {return false;}
};

}

#endif /*TIMEREVENT_H_*/
