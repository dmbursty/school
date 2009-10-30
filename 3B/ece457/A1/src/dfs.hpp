#ifndef _DFS_H
#define _DFS_H

#include "grid.hpp"

class DFS {
 public:
  DFS(Grid* grid);
  ~DFS();

  void solve();
 protected:
 private:
  Grid* grid;
  void makePath(Point* pt);
  int checkPoint(int x, int y, int n, bool** visited);
};

#endif
