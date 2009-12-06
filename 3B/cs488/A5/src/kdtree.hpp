#ifndef KD_TREE_HPP
#define KD_TREE_HPP

#include "algebra.hpp"
#include <vector>

class KDTree {
 public:
  KDTree(double depth) : depth(depth), left(NULL), right(NULL) {}

  void add(Point3D p) { points.push_back(p); }

  int split();
  void print(std::string prefix);
  int getDensityAt(Point3D& p);
  int getLeafDensityAt(Point3D& p);

 private:
  int depth;
  std::vector<Point3D> points;
  KDTree* left;
  KDTree* right;
  double median;
};

#endif
