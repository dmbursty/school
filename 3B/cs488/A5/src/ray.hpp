#ifndef RAY_HPP
#define RAY_HPP

#include "algebra.hpp"
#include <vector>

class GeometryNode;

struct Ray {
  Ray();
  Ray(Point3D eye, Vector3D dir);

  void transform(const Matrix4x4& m);
  void normalize();

  Ray jitterSphere(double radius);
  Ray jitterCone(double radius);

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
  double dist;
  bool left;
};

struct Pixel {
  Pixel() : r(0), g(0), b(0), reflect(0), gloss(0), refract(0), empty(false) {}
  // Base Colour
  double r, g, b;

  // Reflection intensity
  double reflect;
  Ray reflect_ray;
  double gloss;
  // normal may be needed for glossy reflections
  Vector3D normal;

  // Refraction intensity
  double refract;
  Ray refract_ray;
  double index;

  // Whether we hit something or not
  bool empty;

  // The world-space coordinate of this pixel
  Point3D pt;
};

struct intersect_compare {
  bool operator() (Intersection i, Intersection j) { return (i.dist < j.dist); }
};

struct Intersections {
  Intersections() {}
  void addInter(Intersection i);
  void sort(Point3D& eye);
  void transform(const Matrix4x4& trans, const Matrix4x4& inv);
  int size() { return inter.size(); }

  std::vector<Intersection> inter;
  Intersection& operator[](size_t i) { return inter[i]; }
};

#endif
