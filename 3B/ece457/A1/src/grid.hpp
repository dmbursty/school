#ifndef _GRID_H
#define _GRID_H

#include <vector>
#include "point.hpp"
using std::vector;

class Grid {
 public:
  Grid(int x, int y);
  Grid(const Grid& g);
  ~Grid();
  vector<Point*> net;
  int width, height;

  void set_obstacle(int x1, int y1, int x2, int y2);
  void set_net(int x, int y);

  void print();
  void print_path(char* name, int id);
  int get(int x, int y) const;
  void set(int x, int y, int val);
  int checkPoint(int x, int y, bool** visited);

  vector<Point*> path;

 protected:

 private:
  int** grid;

};

#endif
