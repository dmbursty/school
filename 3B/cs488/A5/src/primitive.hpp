#ifndef CS488_PRIMITIVE_HPP
#define CS488_PRIMITIVE_HPP

#include "algebra.hpp"
#include "ray.hpp"

class Primitive {
public:
  virtual ~Primitive();
  virtual Intersections ray_intersect(Ray r);
  virtual double* getBoundingBox();
};

class Torus : public Primitive {
public:
  Torus(double r) : r(r) {}
  virtual ~Torus();
  virtual Intersections ray_intersect(Ray ray);
  virtual double* getBoundingBox();
private:
  double r;
};

class Cylinder : public Primitive {
public:
  virtual ~Cylinder();
  virtual Intersections ray_intersect(Ray r);
  virtual double* getBoundingBox();
};

class Cone : public Primitive {
public:
  virtual ~Cone();
  virtual Intersections ray_intersect(Ray r);
  virtual double* getBoundingBox();
};

class Sphere : public Primitive {
public:
  virtual ~Sphere();
  virtual Intersections ray_intersect(Ray r);
  virtual double* getBoundingBox();
};

class Cube : public Primitive {
public:
  virtual ~Cube();
  virtual Intersections ray_intersect(Ray r);
  virtual double* getBoundingBox();
};

#endif
