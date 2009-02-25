// Author: Michael Terry

#include "Rectangle.h"
#include <algorithm>

using namespace std;

namespace cs349
{

Rectangle::Rectangle()
{
  x = 0;
  y = 0;
  width = 0;
  height = 0;
}

Rectangle::Rectangle(double x, double y, double width, double height)
{
  this->x = x;
  this->y = y;
  this->width = width;
  this->height = height;
}

Rectangle::~Rectangle()
{
}

Point Rectangle::GetTopLeft() const
{
  return Point(x, y);
}

Point Rectangle::GetBottomRight() const
{
  return Point(x+width, y+height);
}

bool Rectangle::IsEmpty() const
{
  return (this->width <= 0 || this->height <= 0);
}

bool Rectangle::IsPointInRectangle(const Point & p) const
{
  return (   (p.x >= this->x)
      && (p.y >= this->y)
      && (p.x < (this->x + this->width))
      && (p.y < (this->y + this->height)));

}

bool Rectangle::IntersectsRectangle(const Rectangle & r) const
{
  if (x >= r.GetBottomRight().x
    || GetBottomRight().x <= r.x
    || y >= r.GetBottomRight().y
    || GetBottomRight().y <= r.y
    )
  {
    return false;
  }
  return true;
}

Rectangle Rectangle::GetIntersection(const Rectangle & r) const
{
  if (!IntersectsRectangle(r)) {
    return Rectangle();
  }
  double x1 = max(this->x, r.x);
  double y1 = max(this->y, r.y);
  double x2 = min(this->GetBottomRight().x, r.GetBottomRight().x);
  double y2 = min(this->GetBottomRight().y, r.GetBottomRight().y);
  return Rectangle(x1, y1, x2-x1, y2-y1);
}

Rectangle Rectangle::GetUnion(const Rectangle& r) const
{
  double x1 = min(this->x, r.x);
  double y1 = min(this->y, r.y);
  double x2 = max(this->GetBottomRight().x, r.GetBottomRight().x);
  double y2 = max(this->GetBottomRight().y, r.GetBottomRight().y);
  return Rectangle(x1, y1, x2-x1, y2-y1);
}

ostream & operator << (ostream &os, const Rectangle& r)
{
  os << "r(" << r.x << ", " << r.y << ", " << r.width << ", " << r.height << ")";
  return os;
}

}

