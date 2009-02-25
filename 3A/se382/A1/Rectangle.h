// Author: Michael Terry

#ifndef RECTANGLE_H_
#define RECTANGLE_H_

#include <iostream>
using namespace std;

#include "Point.h"

namespace cs349
{

/*
 * A rudimentary rectangle class
 */
class Rectangle
{
 public:
  double x;
  double y;
  double width;
  double height;

  Rectangle();
  Rectangle(double x, double y, double width, double height);
  virtual ~Rectangle();

  virtual Point GetBottomRight() const;
  virtual Rectangle GetIntersection(const Rectangle & r) const;
  virtual Rectangle GetUnion(const Rectangle& r) const;
  virtual Point GetTopLeft() const;

  virtual bool IsEmpty() const;
  virtual bool IsPointInRectangle(const Point & p) const;
  virtual bool IntersectsRectangle(const Rectangle & r) const;

};

ostream & operator << (ostream &os, const Rectangle& r);

}

#endif /*RECTANGLE_H_*/
