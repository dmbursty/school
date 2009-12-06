#include "csg.hpp"
#include <vector>

extern unsigned int HIER_BOUND;

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

BoundingNode* UnionNode::generateBounds() {
  std::cerr << "Building bounding box for union" <<std::endl;
  if (!HIER_BOUND) return NULL;
  BoundingNode* l_bound = lchild->generateBounds();
  BoundingNode* r_bound = rchild->generateBounds();

  if (l_bound == NULL || r_bound == NULL) return NULL;

  // Collect all the points in the CSG's coordinates so we can find a new bound
  std::vector<Point3D> points;
  // From the left child
  points.push_back(l_bound->obj()->get_transform() * Point3D(
      l_bound->get_min()[0], l_bound->get_min()[1], l_bound->get_min()[2]));
  points.push_back(l_bound->obj()->get_transform() * Point3D(
      l_bound->get_min()[0], l_bound->get_max()[1], l_bound->get_min()[2]));
  points.push_back(l_bound->obj()->get_transform() * Point3D(
      l_bound->get_max()[0], l_bound->get_min()[1], l_bound->get_min()[2]));
  points.push_back(l_bound->obj()->get_transform() * Point3D(
      l_bound->get_max()[0], l_bound->get_max()[1], l_bound->get_min()[2]));
  points.push_back(l_bound->obj()->get_transform() * Point3D(
      l_bound->get_min()[0], l_bound->get_min()[1], l_bound->get_max()[2]));
  points.push_back(l_bound->obj()->get_transform() * Point3D(
      l_bound->get_min()[0], l_bound->get_max()[1], l_bound->get_max()[2]));
  points.push_back(l_bound->obj()->get_transform() * Point3D(
      l_bound->get_max()[0], l_bound->get_min()[1], l_bound->get_max()[2]));
  points.push_back(l_bound->obj()->get_transform() * Point3D(
      l_bound->get_max()[0], l_bound->get_max()[1], l_bound->get_max()[2]));

  // Initialize bounds
  double bounds[6];
  bounds[0] = points[0][0];
  bounds[1] = points[0][1];
  bounds[2] = points[0][2];
  bounds[3] = points[0][0] + 0.0001;
  bounds[4] = points[0][1] + 0.0001;
  bounds[5] = points[0][2] + 0.0001;

  // Find min and max given left points
  for (int i = 1; i < 8; i++) {
    bounds[0] = std::min(points[i][0], bounds[0]);
    bounds[1] = std::min(points[i][1], bounds[1]);
    bounds[2] = std::min(points[i][2], bounds[2]);
    bounds[3] = std::max(points[i][0], bounds[3]);
    bounds[4] = std::max(points[i][1], bounds[4]);
    bounds[5] = std::max(points[i][2], bounds[5]);
  }


  // Grab right object points
  points.clear();
  points.push_back(r_bound->obj()->get_transform() * Point3D(
      r_bound->get_min()[0], r_bound->get_min()[1], r_bound->get_min()[2]));
  points.push_back(r_bound->obj()->get_transform() * Point3D(
      r_bound->get_min()[0], r_bound->get_max()[1], r_bound->get_min()[2]));
  points.push_back(r_bound->obj()->get_transform() * Point3D(
      r_bound->get_max()[0], r_bound->get_min()[1], r_bound->get_min()[2]));
  points.push_back(r_bound->obj()->get_transform() * Point3D(
      r_bound->get_max()[0], r_bound->get_max()[1], r_bound->get_min()[2]));
  points.push_back(r_bound->obj()->get_transform() * Point3D(
      r_bound->get_min()[0], r_bound->get_min()[1], r_bound->get_max()[2]));
  points.push_back(r_bound->obj()->get_transform() * Point3D(
      r_bound->get_min()[0], r_bound->get_max()[1], r_bound->get_max()[2]));
  points.push_back(r_bound->obj()->get_transform() * Point3D(
      r_bound->get_max()[0], r_bound->get_min()[1], r_bound->get_max()[2]));
  points.push_back(r_bound->obj()->get_transform() * Point3D(
      r_bound->get_max()[0], r_bound->get_max()[1], r_bound->get_max()[2]));

  // Modify bounds given right points
  for (int i = 0; i < 8; i++) {
    bounds[0] = std::min(points[i][0], bounds[0]);
    bounds[1] = std::min(points[i][1], bounds[1]);
    bounds[2] = std::min(points[i][2], bounds[2]);
    bounds[3] = std::max(points[i][0], bounds[3]);
    bounds[4] = std::max(points[i][1], bounds[4]);
    bounds[5] = std::max(points[i][2], bounds[5]);
  }

  // Make the bounding node
  Cube* cube = new Cube();
  GeometryNode* bounder = new GeometryNode("bound", cube);
  bounder->set_material(l_bound->bound()->get_material());
  bounder->translate(Vector3D(bounds[0], bounds[1], bounds[2]));
  bounder->scale(Vector3D(bounds[3] - bounds[0],
                          bounds[4] - bounds[1],
                          bounds[5] - bounds[2]));
  bounder->set_transform(get_transform() * bounder->get_transform());
  BoundingNode* boundNode = new BoundingNode(
      "bound", bounder, this, true,
      Point3D(bounds[0], bounds[1], bounds[2]),
      Point3D(bounds[3], bounds[4], bounds[5]));
  return boundNode;
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

BoundingNode* IntersectNode::generateBounds() {
  std::cerr << "Building bounding box for intersect" <<std::endl;
  if (!HIER_BOUND) return NULL;
  BoundingNode* l_bound = lchild->generateBounds();
  BoundingNode* r_bound = rchild->generateBounds();

  if (l_bound == NULL || r_bound == NULL) return NULL;

  // Collect all the points in the CSG's coordinates so we can find a new bound
  std::vector<Point3D> points;
  // From the left child
  points.push_back(l_bound->obj()->get_transform() * Point3D(
      l_bound->get_min()[0], l_bound->get_min()[1], l_bound->get_min()[2]));
  points.push_back(l_bound->obj()->get_transform() * Point3D(
      l_bound->get_min()[0], l_bound->get_max()[1], l_bound->get_min()[2]));
  points.push_back(l_bound->obj()->get_transform() * Point3D(
      l_bound->get_max()[0], l_bound->get_min()[1], l_bound->get_min()[2]));
  points.push_back(l_bound->obj()->get_transform() * Point3D(
      l_bound->get_max()[0], l_bound->get_max()[1], l_bound->get_min()[2]));
  points.push_back(l_bound->obj()->get_transform() * Point3D(
      l_bound->get_min()[0], l_bound->get_min()[1], l_bound->get_max()[2]));
  points.push_back(l_bound->obj()->get_transform() * Point3D(
      l_bound->get_min()[0], l_bound->get_max()[1], l_bound->get_max()[2]));
  points.push_back(l_bound->obj()->get_transform() * Point3D(
      l_bound->get_max()[0], l_bound->get_min()[1], l_bound->get_max()[2]));
  points.push_back(l_bound->obj()->get_transform() * Point3D(
      l_bound->get_max()[0], l_bound->get_max()[1], l_bound->get_max()[2]));

  // Initialize left bounds
  double l_bounds[6];
  l_bounds[0] = points[0][0];
  l_bounds[1] = points[0][1];
  l_bounds[2] = points[0][2];
  l_bounds[3] = points[0][0] + 0.0001;
  l_bounds[4] = points[0][1] + 0.0001;
  l_bounds[5] = points[0][2] + 0.0001;

  // Find min and max given left points
  for (int i = 1; i < 8; i++) {
    l_bounds[0] = std::min(points[i][0], l_bounds[0]);
    l_bounds[1] = std::min(points[i][1], l_bounds[1]);
    l_bounds[2] = std::min(points[i][2], l_bounds[2]);
    l_bounds[3] = std::max(points[i][0], l_bounds[3]);
    l_bounds[4] = std::max(points[i][1], l_bounds[4]);
    l_bounds[5] = std::max(points[i][2], l_bounds[5]);
  }


  // Grab right object points
  points.clear();
  points.push_back(r_bound->obj()->get_transform() * Point3D(
      r_bound->get_min()[0], r_bound->get_min()[1], r_bound->get_min()[2]));
  points.push_back(r_bound->obj()->get_transform() * Point3D(
      r_bound->get_min()[0], r_bound->get_max()[1], r_bound->get_min()[2]));
  points.push_back(r_bound->obj()->get_transform() * Point3D(
      r_bound->get_max()[0], r_bound->get_min()[1], r_bound->get_min()[2]));
  points.push_back(r_bound->obj()->get_transform() * Point3D(
      r_bound->get_max()[0], r_bound->get_max()[1], r_bound->get_min()[2]));
  points.push_back(r_bound->obj()->get_transform() * Point3D(
      r_bound->get_min()[0], r_bound->get_min()[1], r_bound->get_max()[2]));
  points.push_back(r_bound->obj()->get_transform() * Point3D(
      r_bound->get_min()[0], r_bound->get_max()[1], r_bound->get_max()[2]));
  points.push_back(r_bound->obj()->get_transform() * Point3D(
      r_bound->get_max()[0], r_bound->get_min()[1], r_bound->get_max()[2]));
  points.push_back(r_bound->obj()->get_transform() * Point3D(
      r_bound->get_max()[0], r_bound->get_max()[1], r_bound->get_max()[2]));

  // Initialize right bounds
  double r_bounds[6];
  r_bounds[0] = points[0][0];
  r_bounds[1] = points[0][1];
  r_bounds[2] = points[0][2];
  r_bounds[3] = points[0][0] + 0.0001;
  r_bounds[4] = points[0][1] + 0.0001;
  r_bounds[5] = points[0][2] + 0.0001;

  // Find min and max given right points
  for (int i = 1; i < 8; i++) {
    r_bounds[0] = std::min(points[i][0], r_bounds[0]);
    r_bounds[1] = std::min(points[i][1], r_bounds[1]);
    r_bounds[2] = std::min(points[i][2], r_bounds[2]);
    r_bounds[3] = std::max(points[i][0], r_bounds[3]);
    r_bounds[4] = std::max(points[i][1], r_bounds[4]);
    r_bounds[5] = std::max(points[i][2], r_bounds[5]);
  }

  // Calculate total bounds
  double bounds[6];
  bounds[0] = std::max(l_bounds[0], r_bounds[0]);
  bounds[1] = std::max(l_bounds[1], r_bounds[1]);
  bounds[2] = std::max(l_bounds[2], r_bounds[2]);
  bounds[3] = std::min(l_bounds[3], r_bounds[3]);
  bounds[4] = std::min(l_bounds[4], r_bounds[4]);
  bounds[5] = std::min(l_bounds[5], r_bounds[5]);

  // Make sure there is _some_ intersection (non-disjoint objects)
  if (bounds[0] > bounds[3] || bounds[1] > bounds[4] || bounds[2] > bounds[5]) {
    return NULL;
  }

  // Make the bounding node
  Cube* cube = new Cube();
  GeometryNode* bounder = new GeometryNode("bound", cube);
  bounder->set_material(l_bound->bound()->get_material());
  bounder->translate(Vector3D(bounds[0], bounds[1], bounds[2]));
  bounder->scale(Vector3D(bounds[3] - bounds[0],
                          bounds[4] - bounds[1],
                          bounds[5] - bounds[2]));
  bounder->set_transform(get_transform() * bounder->get_transform());
  BoundingNode* boundNode = new BoundingNode(
      "bound", bounder, this, true,
      Point3D(bounds[0], bounds[1], bounds[2]),
      Point3D(bounds[3], bounds[4], bounds[5]));
  return boundNode;
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

BoundingNode* DifferenceNode::generateBounds() {
  std::cerr << "Building bounding box for difference" <<std::endl;
  if (!HIER_BOUND) return NULL;
  BoundingNode* l_bound = lchild->generateBounds();

  if (l_bound == NULL) return NULL;

  // Collect all the points in the CSG's coordinates so we can find a new bound
  std::vector<Point3D> points;
  // From the left child
  points.push_back(l_bound->obj()->get_transform() * Point3D(
      l_bound->get_min()[0], l_bound->get_min()[1], l_bound->get_min()[2]));
  points.push_back(l_bound->obj()->get_transform() * Point3D(
      l_bound->get_min()[0], l_bound->get_max()[1], l_bound->get_min()[2]));
  points.push_back(l_bound->obj()->get_transform() * Point3D(
      l_bound->get_max()[0], l_bound->get_min()[1], l_bound->get_min()[2]));
  points.push_back(l_bound->obj()->get_transform() * Point3D(
      l_bound->get_max()[0], l_bound->get_max()[1], l_bound->get_min()[2]));
  points.push_back(l_bound->obj()->get_transform() * Point3D(
      l_bound->get_min()[0], l_bound->get_min()[1], l_bound->get_max()[2]));
  points.push_back(l_bound->obj()->get_transform() * Point3D(
      l_bound->get_min()[0], l_bound->get_max()[1], l_bound->get_max()[2]));
  points.push_back(l_bound->obj()->get_transform() * Point3D(
      l_bound->get_max()[0], l_bound->get_min()[1], l_bound->get_max()[2]));
  points.push_back(l_bound->obj()->get_transform() * Point3D(
      l_bound->get_max()[0], l_bound->get_max()[1], l_bound->get_max()[2]));

  // Initialize bounds
  double bounds[6];
  bounds[0] = points[0][0];
  bounds[1] = points[0][1];
  bounds[2] = points[0][2];
  bounds[3] = points[0][0] + 0.0001;
  bounds[4] = points[0][1] + 0.0001;
  bounds[5] = points[0][2] + 0.0001;

  // Find min and max given left points
  for (int i = 1; i < 8; i++) {
    bounds[0] = std::min(points[i][0], bounds[0]);
    bounds[1] = std::min(points[i][1], bounds[1]);
    bounds[2] = std::min(points[i][2], bounds[2]);
    bounds[3] = std::max(points[i][0], bounds[3]);
    bounds[4] = std::max(points[i][1], bounds[4]);
    bounds[5] = std::max(points[i][2], bounds[5]);
  }

  // Make the bounding node
  Cube* cube = new Cube();
  GeometryNode* bounder = new GeometryNode("bound", cube);
  bounder->set_material(l_bound->bound()->get_material());
  bounder->translate(Vector3D(bounds[0], bounds[1], bounds[2]));
  bounder->scale(Vector3D(bounds[3] - bounds[0],
                          bounds[4] - bounds[1],
                          bounds[5] - bounds[2]));
  bounder->set_transform(get_transform() * bounder->get_transform());
  BoundingNode* boundNode = new BoundingNode(
      "bound", bounder, this, true,
      Point3D(bounds[0], bounds[1], bounds[2]),
      Point3D(bounds[3], bounds[4], bounds[5]));
  return boundNode;
}
