#include "ray.hpp"

Ray::Ray() {}

Ray::Ray(Point3D eye, Vector3D dir) : eye(eye), dir(dir) {}

void Ray::transform(const Matrix4x4& m) {
  eye = m * eye;
  dir = m * dir;
}

void Ray::normalize() {
  dir.normalize();
}

Intersection::Intersection() : hit(false) {}

Intersection::Intersection(double x, double y, double z) {
  pt[0] = x;
  pt[1] = y;
  pt[2] = z;
  hit = true;
}

Intersection::Intersection(double x, double y, double z, GeometryNode* node) {
  pt[0] = x;
  pt[1] = y;
  pt[2] = z;
  hit = true;
  this->node = node;
}
