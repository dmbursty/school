#include "astar.hpp"
#include <iostream>
#include <queue>
#include <vector>
using std::cerr;
using std::endl;
using std::priority_queue;

class compare {
public:
  bool operator() (Point*& p, Point*& q) const {
    return p->h > q->h;
  }
};

AStar::AStar(Grid* grid) {
  this->grid = grid;
}

AStar::~AStar() {
}

void AStar::solve() {
  int num_explored = 0;
  int num_visited = 0;
  priority_queue<Point*,std::vector<Point*>, compare> open;
  bool** visited = new bool*[grid->width];
  for (int i = 0; i < grid->width; i++) {
    *(visited+i) = new bool[grid->height];
    for (int j = 0; j < grid->height; j++) {
      visited[i][j] = false;
    }
  }
  Point* s = grid->net[0];
  grid->set(s->x,s->y, 4);
  for (int n = 0; n < grid->net.size(); n++) {
    Point* start = grid->net[n];
    // If the net point is already in the path, stop
    if (grid->get(start->x,start->y) >= 3) continue;
    open.push(start);
    num_visited++;

    // Search
    while(true) {
      int res, x, y;
      Point* curr = open.top();
      open.pop();
      x = curr->x;
      y = curr->y;
      visited[x][y] = true;
      num_explored++;

      bool done = false;
      // Left
      res = checkPoint(x-1, y, n, visited);
      switch(res) {
        case 0:
          open.push(new Point(x-1, y, curr, grid->net[n-1]));
          num_visited++;
          visited[x-1][y] = true;
          break;
        case 1:
          makePath(new Point(x-1, y, curr));
          done = true;
          break;
      }
      if (done) { break; }

      // Down
      res = checkPoint(x, y+1, n, visited);
      switch(res) {
        case 0:
          open.push(new Point(x, y+1, curr, grid->net[n-1]));
          num_visited++;
          visited[x][y+1] = true;
          break;
        case 1:
          makePath(new Point(x, y+1, curr));
          done = true;
          break;
      }
      if (done) { break; }

      // Right
      res = checkPoint(x+1, y, n, visited);
      switch(res) {
        case 0:
          open.push(new Point(x+1, y, curr, grid->net[n-1]));
          num_visited++;
          visited[x+1][y] = true;
          break;
        case 1:
          makePath(new Point(x+1, y, curr));
          done = true;
          break;
      }
      if (done) { break; }

      // Up
      res = checkPoint(x, y-1, n, visited);
      switch(res) {
        case 0:
          open.push(new Point(x, y-1, curr, grid->net[n-1]));
          num_visited++;
          visited[x][y-1] = true;
          break;
        case 1:
          makePath(new Point(x, y-1, curr));
          done = true;
          break;
      }
      if (done) { break; }
    }

    // Clear visited
    for (int i = 0; i < grid->width; i++) {
      for (int j = 0; j < grid->height; j++) {
        visited[i][j] = false;
      }
    }
    // Clear priority_queue
    while (!open.empty()) {
      open.pop();
    }
  }
  std::cerr << "Visited " << num_visited << " and explored " << num_explored << endl;
}

void AStar::makePath(Point* pt) {
  Point* curr = pt;
  while (curr->parent != NULL) {
    if (grid->get(curr->x, curr->y) == 2) {
      grid->set(curr->x, curr->y, 4);
    } else if (grid->get(curr->x, curr->y) == 0) {
      grid->set(curr->x, curr->y, 3);
    }
    curr = curr->parent;
  }
  if (grid->get(curr->x, curr->y) == 2) {
    grid->set(curr->x, curr->y, 4);
  } else if (grid->get(curr->x, curr->y) == 0) {
    grid->set(curr->x, curr->y, 3);
  }
}

int AStar::checkPoint(int x, int y, int n, bool** visited) {
  // Return valid, invalid, done
  int res = grid->get(x, y);
  if (res == 1 || visited[x][y]) { return -1; }
  if ((n == 0 && res == 2) || res >= 3) { return 1; }
  return 0;
}
