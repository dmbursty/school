#ifndef TURTLEGRAPHICS_H_
#define TURTLEGRAPHICS_H_

#include "../Point.h"

namespace cs349
{

class TurtleGraphics
{
private:
	Point curLocation;
	Point directionVector;
	
public:
	TurtleGraphics();
	virtual ~TurtleGraphics();
	
	Point GetLocation() const;
	void  SetLocation(const Point & p);
	
	Point GetAngleVector() const;
	void  SetAngleVector(const Point & p);

	Point MoveForward();
	void  PointLeft();
	void  PointRight();
	void  PointUp();
	void  PointDown();
	void  Turn(double angle);
};

}

#endif /*TURTLEGRAPHICS_H_*/
