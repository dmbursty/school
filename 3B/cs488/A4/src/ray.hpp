#ifndef RAY_HPP
#define RAY_HPP

#include "algebra.hpp"

class GeometryNode;

struct Ray {
  Ray();
  Ray(Point3D eye, Vector3D dir);

  void transform(const Matrix4x4& m);
  void normalize();

  Point3D eye;
  Vector3D dir;
};

struct Intersection {
  Intersection();
  Intersection(double x, double y, double z);
  Intersection(double x, double y, double z, GeometryNode* node);

  double dist_from(Point3D& p);
  void transform(const Matrix4x4& trans, const Matrix4x4& inv);

  bool hit;
  Point3D pt;
  Vector3D normal;
  GeometryNode* node;
};

#endif
