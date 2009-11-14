#include "primitive.hpp"
#include <iostream>

Primitive::~Primitive()
{
}

Intersection Primitive::ray_intersect(Ray r) {
  return Intersection();
}

Sphere::~Sphere()
{
}

Cube::~Cube()
{
}

NonhierSphere::~NonhierSphere()
{
}

Intersection NonhierSphere::ray_intersect(Ray r) {
  r.normalize();
  // x
  double x = r.eye[0] - m_pos[0];
  double xt = r.dir[0];
  // y
  double y = r.eye[1] - m_pos[1];
  double yt = r.dir[1];
  // z
  double z = r.eye[2] - m_pos[2];
  double zt = r.dir[2];

  // t^2 term
  double a = xt * xt + yt * yt + zt * zt;
  // t term
  double b = 2 * (x * xt + y * yt + z * zt);
  // constant term
  double c = x * x + y * y + z * z - (m_radius * m_radius);

  // Check discriminant
  double d = b * b - 4 * a * c;

  if (d > 0) {
    double root = (-b - sqrt(d)) / (2 * a);
    if (root < 0.1) return Intersection();
    x = r.eye[0] + root * r.dir[0];
    y = r.eye[1] + root * r.dir[1];
    z = r.eye[2] + root * r.dir[2];
    Intersection ret(x, y, z);
    ret.normal[0] = x - m_pos[0];
    ret.normal[1] = y - m_pos[1];
    ret.normal[2] = z - m_pos[2];
    return ret;
  }
  return Intersection();
}

NonhierBox::~NonhierBox()
{
}
