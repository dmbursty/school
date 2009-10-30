#ifndef _BFS_H
#define _BFS_H

#include "grid.hpp"

class BFS {
 public:
  BFS(Grid* grid);
  ~BFS();

  void solve();
 protected:
 private:
  Grid* grid;
  void makePath(Point* pt);
  int checkPoint(int x, int y, int n, bool** visited);
};

#endif
