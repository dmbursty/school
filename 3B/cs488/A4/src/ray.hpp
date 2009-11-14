#ifndef RAY_HPP
#define RAY_HPP

#include "algebra.hpp"

class GeometryNode;

struct Ray {
 public:
  Ray();
  Ray(Point3D eye, Vector3D dir);

  void transform(const Matrix4x4& m);
  void normalize();

  Point3D eye;
  Vector3D dir;
};

struct Intersection {
 public:
  Intersection();
  Intersection(double x, double y, double z);
  Intersection(double x, double y, double z, GeometryNode* node);

  bool hit;
  Point3D pt;
  Vector3D normal;
  GeometryNode* node;
};

#endif
