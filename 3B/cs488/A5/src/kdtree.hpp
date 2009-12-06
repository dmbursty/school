#ifndef KD_TREE_HPP
#define KD_TREE_HPP

#include "algebra.hpp"
#include <vector>

class KDTree {
 public:
  KDTree(double depth) : depth(depth), left(NULL), right(NULL) {}

  void add(Point3D p) { points.push_back(p); }

  void split();
  void print(std::string prefix);

 private:
  int depth;
  std::vector<Point3D> points;
  KDTree* left;
  KDTree* right;
  double median;
};

#endif
