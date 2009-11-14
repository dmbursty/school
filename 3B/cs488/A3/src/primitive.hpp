#ifndef CS488_PRIMITIVE_HPP
#define CS488_PRIMITIVE_HPP

#include "algebra.hpp"
#include <GL/glu.h>

class Primitive {
public:
  virtual ~Primitive();
  virtual void walk_gl(bool picking) = 0;
};

class Sphere : public Primitive {
public:
  Sphere() : madeList(false) {}
  virtual ~Sphere();
  virtual void walk_gl(bool picking);

private:
  bool madeList;
  GLuint m_display_list;
};

#endif
