#ifndef _SMART_H
#define _SMART_H

#include "grid.hpp"

class Smart {
 public:
  Smart(Grid* grid);
  ~Smart();

  void solve();
 protected:
 private:
  Grid* grid;
  void makePath(Point* pt);
  int checkPoint(int x, int y, int n, bool** visited);
  int heuristic(int x, int y);
};

#endif
