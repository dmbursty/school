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

  double map_x, map_y;
  bool inside;
};

struct Pixel {
  Pixel() : r(0), g(0), b(0), reflect(0), refract(0), empty(false) {}
  // Base Colour
  double r, g, b;

  // Reflection intensity
  double reflect;
  Ray reflect_ray;

  // Refraction intensity
  double refract;
  Ray refract_ray;
  double index;

  // Whether we hit something or not
  bool empty;
};

#endif
