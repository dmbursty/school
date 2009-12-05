#include "scene.hpp"
#include <iostream>
#include <limits>

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

Intersections GeometryNode::ray_intersect(Ray r) {
  r.transform(get_inverse());
  Intersections i = m_primitive->ray_intersect(r);
  for (int j = 0; j < i.size(); j++) {
    if (BUMPMAP) {
      if (m_material->bumpmap() != NULL) {
        int map_x = m_material->bumpmap()->width() * i[j].map_x;
        int map_y = m_material->bumpmap()->height() * i[j].map_y;
        double x_perturb = (*m_material->bumpmap())(map_x - 1, map_y, 0)
                          - (*m_material->bumpmap())(map_x + 1, map_y, 0);
        double y_perturb = (*m_material->bumpmap())(map_x, map_y - 1, 0)
                          - (*m_material->bumpmap())(map_x, map_y + 1, 0);
        i[j].normal[0] += x_perturb * BUMPMAP;
        i[j].normal[1] += y_perturb * BUMPMAP;
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

Intersections BoundingNode::ray_intersect(Ray r) {
  Intersections i = m_bound->ray_intersect(r);
  if (BOUNDS) {
    return i;
  }
  for (int j = 0; j < i.size(); j++) {
    Intersections ii = m_obj->ray_intersect(r);
    // Check if our child wants to texture map using the
    // map coords from the bounding primitive
    for (int jj = 0; jj < ii.size(); jj++) {
      if (ii[jj].map_x == -1 || ii[jj].map_y == -1) {
        // We need to cast a ray along the normal, from the intersection point
        Point3D new_eye = i[j].pt + ( 1000 * i[j].normal );
        Vector3D new_ray = -1 * i[j].normal;
        Ray mapper(ii[jj].pt + ii[jj].normal, -1 * ii[jj].normal);
        Intersection mapped = m_bound->ray_intersect(mapper)[0];
        ii[jj].map_x = mapped.map_x;
        ii[jj].map_y = mapped.map_y;
      }
    }
    return ii;
  }
  return i;
}
