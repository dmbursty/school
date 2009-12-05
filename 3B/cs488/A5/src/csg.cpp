#include "csg.hpp"
#include <vector>

Intersections CSGNode::ray_intersect(Ray r) {
  r.transform(get_inverse());

  Intersections left = lchild->ray_intersect(r);
  Intersections right = rchild->ray_intersect(r);
  Intersections ret;
  for (int i = 0; i < left.size(); i++) {
    left[i].left = true;
    ret.addInter(left[i]);
  }
  for (int i = 0; i < right.size(); i++) {
    right[i].left = false;
    ret.addInter(right[i]);
  }
  ret.sort(r.eye);
  // Invoke CSG boolean handler
  // If we got an odd number of intersections, we must have been inside
  // that object
  handle(ret, left.size() % 2, right.size() % 2);
  ret.transform(get_transform(), get_inverse());
  return ret;
}

void UnionNode::handle(Intersections& inters, bool in_l, bool in_r) {
  // Traverse intersections and keep track of when we are inside left or right
  std::vector<Intersection>::iterator it;
  for (it = inters.inter.begin(); it != inters.inter.end();) {
    if (it->left) {
      if (in_l) {
        // Leaving left object
        if (in_r) {
          it = inters.inter.erase(it);
        } else { it++; }
        in_l = false;
      } else {
        // Entering left object
        if (in_r) {
          it = inters.inter.erase(it);
        } else { it++; }
        in_l = true;
      }
    } else {
      if (in_r) {
        // Leaving right object
        if (in_l) {
          it = inters.inter.erase(it);
        } else { it++; }
        in_r = false;
      } else {
        // Entering right object
        if (in_l) {
          it = inters.inter.erase(it);
        } else { it++; }
        in_r = true;
      }
    }
  }
}

void IntersectNode::handle(Intersections& inters, bool in_l, bool in_r) {
  // Traverse intersections and keep track of when we are inside left or right
  std::vector<Intersection>::iterator it;
  for (it = inters.inter.begin(); it != inters.inter.end();) {
    if (it->left) {
      if (in_l) {
        // Leaving left object
        if (!in_r) {
          it = inters.inter.erase(it);
        } else { it++; }
        in_l = false;
      } else {
        // Entering left object
        if (!in_r) {
          it = inters.inter.erase(it);
        } else { it++; }
        in_l = true;
      }
    } else {
      if (in_r) {
        // Leaving right object
        if (!in_l) {
          it = inters.inter.erase(it);
        } else { it++; }
        in_r = false;
      } else {
        // Entering right object
        if (!in_l) {
          it = inters.inter.erase(it);
        } else { it++; }
        in_r = true;
      }
    }
    if (it == inters.inter.end()) break;
  }
}

void DifferenceNode::handle(Intersections& inters, bool in_l, bool in_r) {
  // Traverse intersections and keep track of when we are inside left or right
  std::vector<Intersection>::iterator it;
  for (it = inters.inter.begin(); it != inters.inter.end();) {
    if (it->left) {
      if (in_l) {
        // Leaving left object
        if (in_r) {
          it = inters.inter.erase(it);
        } else { it++; }
        in_l = false;
      } else {
        // Entering left object
        if (in_r) {
          it = inters.inter.erase(it);
        } else { it++; }
        in_l = true;
      }
    } else {
      if (in_r) {
        // Leaving right object
        if (!in_l) {
          it = inters.inter.erase(it);
        } else {
          it->normal = -1 * it->normal;
          it++;
        }
        in_r = false;
      } else {
        // Entering right object
        if (!in_l) {
          it = inters.inter.erase(it);
        } else {
          it->normal = -1 * it->normal;
          it++;
        }
        in_r = true;
      }
    }
    if (it == inters.inter.end()) break;
  }
}
