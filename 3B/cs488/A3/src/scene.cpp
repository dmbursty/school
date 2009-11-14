#include "scene.hpp"
#include <iostream>
#include <GL/glu.h>

int SceneNode::next_id = 0;

SceneNode::SceneNode(const std::string& name)
  : m_name(name), m_id(next_id++)
{
}

SceneNode::~SceneNode()
{
}

void SceneNode::walk_gl(bool picking)
{
  glPushMatrix();
  glMultMatrixd(m_trans.transpose().getAsArray());

  for (ChildList::iterator it = m_children.begin(); it != m_children.end(); it++) {
    (*it)->walk_gl(picking);
  }

  glPopMatrix();
}

bool SceneNode::pick(JointNode*& node, int id) {
  for (ChildList::iterator it = m_children.begin(); it != m_children.end(); it++) {
    bool res = (*it)->pick(node, id);
    if (res) return true;
  }
  return false;
}

void SceneNode::reset() {
  for (ChildList::iterator it = m_children.begin(); it != m_children.end(); it++) {
   (*it)->reset();
  }
}

void SceneNode::togglePicked() {
  for (ChildList::iterator it = m_children.begin(); it != m_children.end(); it++) {
    if (!(*it)->is_joint()) {
      (*it)->togglePicked();
    }
  }
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

JointNode::JointNode(const std::string& name)
  : SceneNode(name), picked(false)
{
}

JointNode::~JointNode()
{
}

void JointNode::walk_gl(bool picking)
{
  // Fill me in
  glPushMatrix();
  glMultMatrixd(m_trans.transpose().getAsArray());
  glRotated(y, 0, 1, 0);
  glRotated(x, 1, 0, 0);
  for (ChildList::iterator it = m_children.begin(); it != m_children.end(); it++) {
    (*it)->walk_gl(picking);
  }
  glPopMatrix();
}

bool JointNode::pick(JointNode*& node, int id) {
  for (ChildList::iterator it = m_children.begin(); it != m_children.end(); it++) {
    bool res = (*it)->pick(node, id);
    if (res) {
      if (node == NULL) node = this;
      return true;
    }
  }
  return false;
}

void JointNode::reset() {
  x = m_joint_x.init;
  y = m_joint_y.init;
  for (ChildList::iterator it = m_children.begin(); it != m_children.end(); it++) {
   (*it)->reset();
  }
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
  x = init;
}

void JointNode::set_joint_y(double min, double init, double max)
{
  m_joint_y.min = min;
  m_joint_y.init = init;
  m_joint_y.max = max;
  y = init;
}

double JointNode::rotate_x(double angle) {
  double oldx = x;
  x += angle;
  if (x > m_joint_x.max) x = m_joint_x.max;
  if (x < m_joint_x.min) x = m_joint_x.min;
  return x - oldx;
}

double JointNode::rotate_y(double angle) {
  double oldy = y;
  y += angle;
  if (y > m_joint_y.max) y = m_joint_y.max;
  if (y < m_joint_y.min) y = m_joint_y.min;
  return y - oldy;
}

GeometryNode::GeometryNode(const std::string& name, Primitive* primitive)
  : SceneNode(name), m_primitive(primitive), picked(false)
{
}

GeometryNode::~GeometryNode()
{
}

void GeometryNode::walk_gl(bool picking)
{
  glPushMatrix();
  glMultMatrixd(m_trans.transpose().getAsArray());

  glPushName(m_id);

  if (picked) {
    PhongMaterial m(Colour(0.0, 1.0, 0.0), Colour(0.0, 1.0, 0.0), 5);
    m.apply_gl();
  } else {
    m_material->apply_gl();
  }
  m_primitive->walk_gl(picking);

  for (ChildList::iterator it = m_children.begin(); it != m_children.end(); it++) {
    (*it)->walk_gl(picking);
  }

  glPopName();

  glPopMatrix();
}

bool GeometryNode::pick(JointNode*& node, int id) {
  if (m_id == id) {
    return true;
  }
  return false;
}

void GeometryNode::togglePicked() {
  picked = !picked;
}
