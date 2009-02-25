#include "TurtleGraphics.h"
#include "../AffineTransform.h"

namespace cs349
{

TurtleGraphics::TurtleGraphics()
{
	this->curLocation     = Point(0,0);
	this->directionVector = Point(1,0);
}

TurtleGraphics::~TurtleGraphics()
{
}

Point TurtleGraphics::GetLocation() const
{
	return this->curLocation;
}

void TurtleGraphics::SetLocation(const Point & p)
{
	this->curLocation = p;
}

Point TurtleGraphics::GetAngleVector() const
{
	return this->directionVector;
}

void  TurtleGraphics::SetAngleVector(const Point & p)
{
	this->directionVector = p;
}


Point TurtleGraphics::MoveForward()
{
	this->curLocation.x += this->directionVector.x;
	this->curLocation.y += this->directionVector.y;
	return this->curLocation;
}

void  TurtleGraphics::PointLeft()
{
	this->directionVector = Point(-1, 0);
}

void  TurtleGraphics::PointRight()
{
	this->directionVector = Point(1, 0);
}

void  TurtleGraphics::PointUp()
{
	this->directionVector = Point(0, -1);
}

void  TurtleGraphics::PointDown()
{
	this->directionVector = Point(0, 1);
}

void  TurtleGraphics::Turn(double angle)
{
	AffineTransform t;
	t.RotateInDegrees(angle);
	this->directionVector = t.Transform(this->directionVector);
}

}
