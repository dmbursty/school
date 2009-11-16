#include "scene.hpp"
#include <iostream>
#include <limits>

//#define BOUNDS 1

SceneNode::SceneNode(const std::string& name)
  : m_name(name)
{
}

SceneNode::~SceneNode()
{
}

Intersection SceneNode::ray_intersect(Ray r) {
  r.transform(get_inverse());
  double closest = std::numeric_limits<double>::max();
  Intersection ret;
  for (ChildList::iterator it = m_children.begin(); it != m_children.end(); it++) {
    Intersection i = (*it)->ray_intersect(r);
    if (i.hit) {
      double dist = (i.pt - r.eye).length();
      if (dist < closest) {
        closest = dist;
        i.transform(get_transform(), get_inverse());
        ret = i;
      }
    }
  }
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
  for (ChildList::iterator it = m_children.begin(); it != m_children.end(); it++) {
    BoundingNode* bound = (*it)->generateBounds();
    if (bound != NULL) {
      it = m_children.erase(it);
      it = m_children.insert(it, bound);
    }
  }
  return NULL;
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

Intersection GeometryNode::ray_intersect(Ray r) {
  r.transform(get_inverse());
  Intersection i = m_primitive->ray_intersect(r);
  if (i.hit) {
    i.node = this;
    i.transform(get_transform(), get_inverse());
  }
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
    BoundingNode* boundNode = new BoundingNode("bound", bound, this);
    delete bounds;
    return boundNode;
  }
  return NULL;
}

BoundingNode::BoundingNode(const std::string& name,
                           GeometryNode* bound, GeometryNode* obj)
  : SceneNode(name), m_bound(bound), m_obj(obj) {}

BoundingNode::~BoundingNode() {}

Intersection BoundingNode::ray_intersect(Ray r) {
  Intersection i = m_bound->ray_intersect(r);
#ifdef BOUNDS
  return i;
#endif
  if (i.hit) {
    return m_obj->ray_intersect(r);
  }
  return i;
}
