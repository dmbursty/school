// Author: Michael Terry

#ifndef POINT_H_
#define POINT_H_

#include <iostream>
using namespace std;

namespace cs349
{

/*
 * A rudimentary point class
 */
class Point
{
public:
	double x;
	double y;
	Point();
	Point(double x, double y);
	virtual ~Point();

};

ostream & operator << (ostream &os, const Point & p);

}

#endif /*POINT_H_*/
