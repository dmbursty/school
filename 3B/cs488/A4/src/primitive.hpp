#ifndef CS488_PRIMITIVE_HPP
#define CS488_PRIMITIVE_HPP

#include "algebra.hpp"
#include "ray.hpp"

class Primitive {
public:
  virtual ~Primitive();
  virtual Intersection ray_intersect(Ray r);
};

class Sphere : public Primitive {
public:
  virtual ~Sphere();
  virtual Intersection ray_intersect(Ray r);
};

class Cube : public Primitive {
public:
  virtual ~Cube();
  virtual Intersection ray_intersect(Ray r);
};

class NonhierSphere : public Primitive {
public:
  NonhierSphere(const Point3D& pos, double radius)
    : m_pos(pos), m_radius(radius)
  {
  }
  virtual ~NonhierSphere();
  virtual Intersection ray_intersect(Ray r);

private:
  Point3D m_pos;
  double m_radius;
};

class NonhierBox : public Primitive {
public:
  NonhierBox(const Point3D& pos, double size)
    : m_pos(pos), m_size(size)
  {
  }
  
  virtual ~NonhierBox();
  virtual Intersection ray_intersect(Ray r);

private:
  Point3D m_pos;
  double m_size;
};

#endif
