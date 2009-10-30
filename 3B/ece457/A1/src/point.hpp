#ifndef _POINT_H
#define _POINT_H

#include <string>

class Grid;

class Point {
 public:
  Point();
  Point(int x, int y, Point* parent=NULL, Grid* grid=NULL);
  ~Point();
  int x;
  int y;
  Point* parent;
  int h;
  int depth;
  int middleness;

 protected:
 private:
  int heuristic(int x, int y, Grid* grid);
};

#endif
