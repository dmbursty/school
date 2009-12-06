#include "kdtree.hpp"

extern unsigned int LEAF_PHOTONS;
extern double CAUSTIC_RADIUS;

struct point_compare {
  point_compare(int axis) : axis(axis) {}
  int axis;
  bool operator() (Point3D a, Point3D b) { return a[axis] < b[axis]; }
};

int KDTree::split() {
  // Once we have few enough points, don't bother splitting
  if (points.size() <= LEAF_PHOTONS) return depth;

  // Sort our points based on axis/depth
  std::sort(points.begin(), points.end(), point_compare(depth % 3));

  unsigned int middle = points.size() / 2;
  median = (points[middle][depth%3] + points[middle+1][depth%3]) / 2;

  left = new KDTree(depth + 1);
  right = new KDTree(depth + 1);

  std::vector<Point3D>::iterator it;
  for (it = points.begin(); it != points.end();) {
    // Check if the point is within our radius
    if (std::fabs((*it)[depth%3] - median) > CAUSTIC_RADIUS) {
      if ((*it)[depth%3] < median) {
        left->add(*it);
        it = points.erase(it);
      } else {
        right->add(*it);
        it = points.erase(it);
      }
    } else {
      // Keep this point at this node
      it++;
    }
  }

  return std::max(left->split(), right->split());
}

void KDTree::print(std::string prefix) {
  if (left != NULL) {
    std::cout << prefix << depth << " " << median << std::endl;
    for (unsigned int i = 0; i < points.size(); i++) {
      std::cout << prefix << points[i] << std::endl;
    }
    std::cout << prefix << "left:" << std::endl;
    left->print(prefix + "  ");
    std::cout << prefix << "right:" << std::endl;
    right->print(prefix + "  ");
  } else {
    std::cout << prefix << "leaf" << std::endl;
    for (unsigned int i = 0; i < points.size(); i++) {
      std::cout << prefix << points[i] << std::endl;
    }
  }
}

int KDTree::getDensityAt(Point3D& p) {
  if (left == NULL || right == NULL) {
    return getLeafDensityAt(p);
  }
  int ret = getLeafDensityAt(p);

  if (p[depth%3] < median) {
    ret += left->getDensityAt(p);
  } else if (p[depth%3] > median) {
    ret += right->getDensityAt(p);
  }
  return ret;
}

int KDTree::getLeafDensityAt(Point3D& p) {
  int ret = 0;
  for (unsigned int i = 0; i < points.size(); i++) {
    if ((p - points[i]).length() < CAUSTIC_RADIUS) ret++;
  }
  return ret;
}
