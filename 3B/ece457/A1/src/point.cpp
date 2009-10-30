#include "point.hpp"
#include "grid.hpp"
#include <math.h>
#include <iostream>
#include <stack>

Point::Point() {
}

Point::Point(int x, int y, Point* parent, Grid* grid) {
  this->x = x;
  this->y = y;
  this->parent = parent;

  if (parent == NULL) {
    this->depth = 0;
  } else {
    this->depth = parent->depth + 1;
  }

  if (grid != NULL) {
    this->h = heuristic(x, y, grid);
  } else {
    this->h = 0;
  }
  this->h += this->depth;

}

int Point::heuristic(int myx, int myy, Grid* grid) {
  int final_h = 999999;
  for (int i = 0; i < grid->path.size(); i++) {
    int h = fabs(myx - grid->path[i]->x) + fabs(myy - grid->path[i]->y);
    if (h < final_h) final_h = h;
  }
  return 0;
}

Point::~Point() {
}
