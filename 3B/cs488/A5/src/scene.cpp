#include "scene.hpp"
#include <iostream>
#include <vector>

extern unsigned int HIER_BOUND;
extern unsigned int ALL_BOUNDS;
extern unsigned int BOUNDS;
extern unsigned int BUMPMAP;

SceneNode::SceneNode(const std::string& name)
  : m_name(name)
{
}

SceneNode::~SceneNode()
{
}

Intersections SceneNode::ray_intersect(Ray r) {
  r.transform(get_inverse());
  Intersections ret;
  for (ChildList::iterator it = m_children.begin(); it != m_children.end(); it++) {
    Intersections i = (*it)->ray_intersect(r);
    for (int j = 0; j < i.size(); j++) {
      ret.addInter(i[j]);
    }
  }
  ret.transform(get_transform(), get_inverse());
  return ret;
}

void SceneNode::rotate(char axis, double angle, bool right)
{
  // Convert angle from degrees to radians
  double r = angle * 0.0174532925;

  if (axis == 'x') {
    Matrix4x4 rotx(Vector4D(1, 0, 0, 0),
                   Vector4D(0, cos(r), -1 * sin(r), 0),
                   Vector4D(0, sin(r), cos(r), 0),
                   Vector4D(0, 0, 0, 1));
    if (right) {
      set_transform(m_trans * rotx);
    } else {
      set_transform(rotx * m_trans);
    }
  } else if (axis == 'y') {
    Matrix4x4 roty(Vector4D(cos(r), 0, sin(r), 0),
                   Vector4D(0, 1, 0, 0),
                   Vector4D(-1 * sin(r), 0, cos(r), 0),
                   Vector4D(0, 0, 0, 1));
    if (right) {
      set_transform(m_trans * roty);
    } else {
      set_transform(roty * m_trans);
    }
  } else if (axis == 'z') {
    Matrix4x4 rotz(Vector4D(cos(r), -1 * sin(r), 0, 0),
                   Vector4D(sin(r), cos(r), 0, 0),
                   Vector4D(0, 0, 1, 0),
                   Vector4D(0, 0, 0, 1));
    if (right) {
      set_transform(m_trans * rotz);
    } else {
      set_transform(rotz * m_trans);
    }
  }
}

void SceneNode::scale(const Vector3D& amount)
{
  Matrix4x4 scale(Vector4D(amount[0], 0, 0, 0),
                  Vector4D(0, amount[1], 0, 0),
                  Vector4D(0, 0, amount[2], 0),
                  Vector4D(0, 0, 0, 1));
  set_transform(m_trans * scale);
}

void SceneNode::translate(const Vector3D& amount, bool right)
{
  Matrix4x4 trans(Vector4D(1, 0, 0, amount[0]),
                  Vector4D(0, 1, 0, amount[1]),
                  Vector4D(0, 0, 1, amount[2]),
                  Vector4D(0, 0, 0, 1));
  if (right) {
    set_transform(m_trans * trans);
  } else {
    set_transform(trans * m_trans);
  }
}

bool SceneNode::is_joint() const
{
  return false;
}

BoundingNode* SceneNode::generateBounds() {
  std::cerr << "Building bounding box for \"" << get_name() << "\"" <<std::endl;
  bool set = false;
  Material* bound_mat;
  double bounds[6];
  for (ChildList::iterator it = m_children.begin(); it != m_children.end(); it++) {
    BoundingNode* bound = (*it)->generateBounds();
    if (bound != NULL){
      // Keep track of our bounds
      //std::cout << get_name() << " Child was on " << bound->get_min() << " to " << bound->get_max() << " - " << (*it)->get_name() << std::endl;
      // Construct the 8 bounding points in the scene's coordinate system
      Point3D minpt = bound->get_min();
      Point3D maxpt = bound->get_max();
      std::vector<Point3D> points;
      points.push_back(Point3D(minpt[0], minpt[1], minpt[2]));
      points.push_back(Point3D(minpt[0], maxpt[1], minpt[2]));
      points.push_back(Point3D(maxpt[0], minpt[1], minpt[2]));
      points.push_back(Point3D(maxpt[0], maxpt[1], minpt[2]));
      points.push_back(Point3D(minpt[0], minpt[1], maxpt[2]));
      points.push_back(Point3D(minpt[0], maxpt[1], maxpt[2]));
      points.push_back(Point3D(maxpt[0], minpt[1], maxpt[2]));
      points.push_back(Point3D(maxpt[0], maxpt[1], maxpt[2]));
      // Transform the eight points and find min/max in scene coordinates
      for (int i = 0; i < 8; i++) {
        Point3D pt = (*it)->get_transform() * points[i];
        if (set) {
          bounds[0] = std::min(pt[0], bounds[0]);
          bounds[1] = std::min(pt[1], bounds[1]);
          bounds[2] = std::min(pt[2], bounds[2]);
          bounds[3] = std::max(pt[0], bounds[3]);
          bounds[4] = std::max(pt[1], bounds[4]);
          bounds[5] = std::max(pt[2], bounds[5]);
        } else {
          set = true;
          bound_mat = bound->bound()->get_material();
          bounds[0] = pt[0];
          bounds[1] = pt[1];
          bounds[2] = pt[2];
          bounds[3] = pt[0] + 0.0001;
          bounds[4] = pt[1] + 0.0001;
          bounds[5] = pt[2] + 0.0001;
        }
        //std::cout << "My new bounds " <<
            //bounds[0] << ", " << bounds[1] << ", " << bounds[2] << " to " <<
            //bounds[3] << ", " << bounds[4] << ", " << bounds[5] << std::endl;
      }
      // Insert the bounding node into the scene tree if it is useful
      if (ALL_BOUNDS || bound->useful()) {
        it = m_children.erase(it);
        it = m_children.insert(it, bound);
      } else {
        // We no longer need the bounding node
        delete bound;
      }
    }
  }
  if (!HIER_BOUND) return NULL;
  if (set) {
    // Construct a new bounding node for this scene
    Cube* cube = new Cube();
    GeometryNode* bounder = new GeometryNode("bound", cube);
    bounder->set_material(bound_mat);
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
  } else {
    return NULL;
  }
}

std::vector<BoundingNode*> SceneNode::getCausticObjects() {
  std::vector<BoundingNode*> ret;
  for (ChildList::iterator it = m_children.begin(); it != m_children.end(); it++) {
    std::vector<BoundingNode*> objects = (*it)->getCausticObjects();
    for (unsigned int j = 0; j < objects.size(); j++) {
      objects[j]->bound()->set_transform(
        get_transform() * objects[j]->bound()->get_transform());
      ret.push_back(objects[j]);
    }
  }
  return ret;
}

JointNode::JointNode(const std::string& name)
  : SceneNode(name)
{
}

JointNode::~JointNode()
{
}

bool JointNode::is_joint() const
{
  return true;
}

void JointNode::set_joint_x(double min, double init, double max)
{
  m_joint_x.min = min;
  m_joint_x.init = init;
  m_joint_x.max = max;
}

void JointNode::set_joint_y(double min, double init, double max)
{
  m_joint_y.min = min;
  m_joint_y.init = init;
  m_joint_y.max = max;
}

GeometryNode::GeometryNode(const std::string& name, Primitive* primitive)
  : SceneNode(name),
    m_primitive(primitive)
{
}

GeometryNode::~GeometryNode()
{
}

Intersections GeometryNode::ray_intersect(Ray r) {
  r.transform(get_inverse());
  Intersections i = m_primitive->ray_intersect(r);
  for (int j = 0; j < i.size(); j++) {
    if (BUMPMAP) {
      if (m_material->bumpmap() != NULL) {
        // Perturb in a cone
        // Generate a vector that is perpendicular to the normal
        Vector3D a;
        if (i[j].normal[0] != 0) {
          a[0] = (i[j].normal[1] + i[j].normal[2]) / i[j].normal[0];
          a[1] = -1;
          a[2] = -1;
        } else if (i[j].normal[1] != 0) {
          a[0] = -1;
          a[1] = (i[j].normal[0] + i[j].normal[2]) / i[j].normal[1];
          a[2] = -1;
        } else if (i[j].normal[2] != 0) {
          a[0] = -1;
          a[1] = -1;
          a[2] = (i[j].normal[0] + i[j].normal[1]) / i[j].normal[2];
        }
        // Generate second vector perpendicular to the two other vectors
        a.normalize();
        Vector3D b = a.cross(i[j].normal);
        b.normalize();

        // a and b now are an orthonormal basis of the plane defined by normal
        int map_x = m_material->bumpmap()->width() * i[j].map_x;
        int map_y = m_material->bumpmap()->height() * i[j].map_y;
        double x_perturb = (*m_material->bumpmap())(map_x - 1, map_y, 0)
                          - (*m_material->bumpmap())(map_x + 1, map_y, 0);
        double y_perturb = (*m_material->bumpmap())(map_x, map_y - 1, 0)
                          - (*m_material->bumpmap())(map_x, map_y + 1, 0);
        i[j].normal = i[j].normal + x_perturb * BUMPMAP * a;
        i[j].normal = i[j].normal + y_perturb * BUMPMAP * b;
      }
    }
    i[j].node = this;
  }
  i.transform(get_transform(), get_inverse());
  return i;
}

BoundingNode* GeometryNode::generateBounds() {
  double* bounds = m_primitive->getBoundingBox();
  if (bounds != NULL) {
    Cube* cube = new Cube();
    GeometryNode* bound = new GeometryNode(*this);
    bound->set_primitive(cube);
    bound->translate(Vector3D(bounds[0], bounds[1], bounds[2]));
    bound->scale(Vector3D(bounds[3] - bounds[0],
                          bounds[4] - bounds[1],
                          bounds[5] - bounds[2]));
    BoundingNode* boundNode = new BoundingNode(
        "bound", bound, this, bounds[6] != 0,
        Point3D(bounds[0], bounds[1], bounds[2]),
        Point3D(bounds[3], bounds[4], bounds[5]));
    delete bounds;
    return boundNode;
  }
  return NULL;
}

std::vector<BoundingNode*> GeometryNode::getCausticObjects() {
  std::vector<BoundingNode*> ret;
  if (m_material->reflect() > 0 || m_material->refract() > 0) {
    double* bounds = m_primitive->getBoundingBox();
    if (bounds != NULL) {
      Cube* cube = new Cube();
      GeometryNode* bound = new GeometryNode(*this);
      bound->set_primitive(cube);
      bound->translate(Vector3D(bounds[0], bounds[1], bounds[2]));
      bound->scale(Vector3D(bounds[3] - bounds[0],
                            bounds[4] - bounds[1],
                            bounds[5] - bounds[2]));
      BoundingNode* boundNode = new BoundingNode(
          "bound", bound, this, bounds[6] != 0,
          Point3D(bounds[0], bounds[1], bounds[2]),
          Point3D(bounds[3], bounds[4], bounds[5]));
      delete bounds;
      ret.push_back(boundNode);
    }
  }
  return ret;
}

BoundingNode::BoundingNode(const std::string& name,
                           GeometryNode* bound, SceneNode* obj, bool useful,
                           Point3D minpt, Point3D maxpt)
  : SceneNode(name), m_bound(bound), m_obj(obj), m_useful(useful),
    m_min(minpt), m_max(maxpt) {}

BoundingNode::~BoundingNode() {}

Intersections BoundingNode::ray_intersect(Ray r) {
  Intersections i = m_bound->ray_intersect(r);
  if (BOUNDS) {
    return i;
  }
  if (i.size() > 0) {
    i.sort(r.eye);
    Intersections ii = m_obj->ray_intersect(r);
    // Check if our child wants to texture map using the
    // map coords from the bounding primitive
    for (int jj = 0; jj < ii.size(); jj++) {
      if (ii[jj].map_x == -1 || ii[jj].map_y == -1) {
        // We need to cast a ray along the normal, from the intersection point
        //std::cout << ii[jj].pt << " - " << ii[jj].normal << std::endl;
        Ray mapper(ii[jj].pt + 1000 * ii[jj].normal, -1 * ii[jj].normal);
        //std::cout << mapper.eye << " - " << mapper.dir << std::endl;
        Intersection mapped = m_bound->ray_intersect(mapper)[0];
        ii[jj].map_x = mapped.map_x;
        ii[jj].map_y = mapped.map_y;
      }
    }
    return ii;
  }
  return i;
}

std::vector<BoundingNode*> BoundingNode::getCausticObjects() {
  return m_obj->getCausticObjects();
}
