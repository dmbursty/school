// Author: Michael Terry

#include "Point.h"

namespace cs349
{

Point::Point()
{
	x = 0;
	y = 0;
}

Point::Point(double x, double y)
{
	this->x = x;
	this->y = y;
}

Point::~Point()
{
}

ostream & operator << (ostream &os, const Point & p)
{
	os << "p(" << p.x << ", " << p.y << ")";
	return os;
}

}
