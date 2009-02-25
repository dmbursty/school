// Author: Michael Terry
#ifndef EVENT_H_
#define EVENT_H_

namespace cs349
{

/*
 * The Event class is the base class for any user interface event. The
 * only requirement for subclasses is to implement the "DispatchEvent"
 * method. This method is called by Application when it is pulled off
 * the event queue, when it should be delivered and handled.
 */
class Event
{
public:
	Event();
	virtual ~Event();
	virtual void DispatchEvent() = 0;
	virtual bool IsPaint() = 0;
};

}

#endif /*EVENT_H_*/
