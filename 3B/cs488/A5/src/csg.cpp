#include "csg.hpp"

Intersection UnionNode::ray_intersect(Ray r) {
  return lchild->ray_intersect(r);
}
