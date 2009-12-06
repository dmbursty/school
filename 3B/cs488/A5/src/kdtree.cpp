#include "kdtree.hpp"

extern unsigned int LEAF_PHOTONS;

struct point_compare {
  point_compare(int axis) : axis(axis) {}
  int axis;
  bool operator() (Point3D a, Point3D b) { return a[axis] < b[axis]; }
};

void KDTree::split() {
  // Once we have few enough points, don't bother splitting
  if (points.size() <= LEAF_PHOTONS) return;

  // Sort our points based on axis/depth
  std::sort(points.begin(), points.end(), point_compare(depth % 3));

  unsigned int middle = points.size() / 2;
  median = (points[middle][depth%3] + points[middle+1][depth%3]) / 2;

  left = new KDTree(depth + 1);
  right = new KDTree(depth + 1);

  for (unsigned int i = 0; i < points.size(); i++) {
    if (i <= middle) {
      left->add(points[i]);
    } else {
      right->add(points[i]);
    }
  }

  left->split();
  right->split();
}

void KDTree::print(std::string prefix) {
  if (left != NULL) {
    std::cout << prefix << depth << " " << median << std::endl;
    std::cout << prefix << "left:" << std::endl;
    left->print(prefix + "  ");
    std::cout << prefix << "right:" << std::endl;
    right->print(prefix + "  ");
  } else {
    std::cout << prefix << "leaf" << std::endl;
    for (int i = 0; i < points.size(); i++) {
      std::cout << prefix << points[i] << std::endl;
    }
  }
}
