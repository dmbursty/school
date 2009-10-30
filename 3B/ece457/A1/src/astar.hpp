#ifndef _A_STAR_H
#define _A_STAR_H

#include "grid.hpp"

class AStar {
 public:
  AStar(Grid* grid);
  ~AStar();

  void solve();
 protected:
 private:
  Grid* grid;
  void makePath(Point* pt);
  int checkPoint(int x, int y, int n, bool** visited);
  int heuristic(int x, int y);
};

#endif
