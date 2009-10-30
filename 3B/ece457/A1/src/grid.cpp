#include "grid.hpp"
#include "point.hpp"
#include <iostream>
#include <stack>
#include <stdlib.h>
using std::endl;
using std::cout;
using std::string;

Grid::Grid(int x, int y) {
  width = x;
  height = y;
  grid = new int*[x];
  for (int i = 0; i < x; i++) {
    *(grid+i) = new int[y];
  }

  for (int i = 0; i < x; i++) {
    for (int j = 0; j < x; j++) {
      grid[i][j] = 0;
    }
  }
}

Grid::Grid(const Grid& g) {
  width = g.width;
  height = g.height;
  grid = new int*[width];
  for (int i = 0; i < width; i++) {
    *(grid+i) = new int[height];
  }

  for (int i = 0; i < width; i++) {
    for (int j = 0; j < width; j++) {
      grid[i][j] = 0;
    }
  }

  for (int x = 0; x < width; x++) {
    for (int y = 0; y < height; y++) {
      grid[x][y] = g.get(x,y);
    }
  }

  net.assign(g.net.begin(), g.net.end());
}

Grid::~Grid() {
}

void Grid::set_obstacle(int x1, int y1, int x2, int y2) {
  for (int x = x1; x <= x2; x++) {
    for (int y = y1; y <= y2; y++) {
      grid[x][y] = 1;
    }
  }
}

void Grid::set_net(int x, int y) {
  // Optimize net order
  bool inserted = false;
  Point* pt = new Point(x, y);
  pt->middleness = ( abs(x - width/2) + abs(y - height/2));
  for (vector<Point*>::iterator i = net.begin(); i < net.end(); i++) {
    if ((*i)->middleness > pt->middleness) {
      net.insert(i, pt);
      inserted = true;
      break;
    }
  }
  if (! inserted) {
    net.push_back(pt);
  }
  grid[x][y] = 2;
}

void Grid::print() {
  for (int y = 0; y < height; y++) {
  //for (int y = height - 1; y >= 0; y--) {
    for (int x = 0; x < width; x++) {
      char out;
      switch (grid[x][y]) {
        case 0:  // Empty
          out = ' '; break;
        case 1:  // Obstacle
          out = 'X'; break;
        case 2:  // Net
          out = 'N'; break;
        case 3:  // Path
          out = '.'; break;
        case 4:  // Net in path
          out = '&'; break;
      }
      cout << out;
    }
    cout << endl;
  }
}

void Grid::print_path(char* name, int id) {
  string sequence = "";
  int num_seq = 0;
  char buffer[50];
  // Traverse the path and print all segments
  bool** visited = new bool*[width];
  for (int i = 0; i < width; i++) {
    *(visited+i) = new bool[height];
    for (int j = 0; j < height; j++) {
      visited[i][j] = false;
    }
  }
  std::stack<Point*> open;
  Point* curr = net[0];
  open.push(curr);
  while(!open.empty()) {
    curr = open.top();
    open.pop();
    int x,y,res;
    x = curr->x;
    y = curr->y;
    visited[x][y] = true;

    if (curr->parent != NULL) {
      sprintf(buffer, "\n(%d,%d)-(%d,%d)", curr->parent->x, curr->parent->y, x, y);
      sequence.append(buffer);
      num_seq++;
    }

    // Left
    res = checkPoint(x-1, y, visited);
    if (res >= 3) {
      open.push(new Point(x-1, y, curr));
    }

    // Right
    res = checkPoint(x+1, y, visited);
    if (res >= 3) {
      open.push(new Point(x+1, y, curr));
    }

    // Down
    res = checkPoint(x, y-1, visited);
    if (res >= 3) {
      open.push(new Point(x, y-1, curr));
    }

    // Up
    res = checkPoint(x, y+1, visited);
    if (res >= 3) {
      open.push(new Point(x, y+1, curr));
    }
  }
  sprintf(buffer, "net%s %d %d", name, id, num_seq);
  sequence.insert(0, buffer);
  std::cout << sequence << std::endl;
  std::cout << "!" << std::endl;
}

int Grid::checkPoint(int x, int y, bool** visited) {
  if (visited[x][y]) {
    return 1;
  }
  return get(x,y);
}

int Grid::get(int x, int y) const {
  if (x < 0 || x >= width || y < 0 || y >= height) {
    return 1;
  }
  return grid[x][y];
}

void Grid::set(int x, int y, int val) {
  grid[x][y] = val;
  if (val >= 3) {
    path.push_back(new Point(x,y));
  }
}
