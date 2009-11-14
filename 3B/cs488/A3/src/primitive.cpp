#include "primitive.hpp"
#include <GL/glu.h>

Primitive::~Primitive()
{
}

Sphere::~Sphere()
{
}

void Sphere::walk_gl(bool picking)
{
  if (!madeList) {
    m_display_list = glGenLists(1);
    glNewList(m_display_list, GL_COMPILE);
    GLUquadric* gq = gluNewQuadric();
    gluSphere(gq, 1, 20, 20);
    glEndList();
  }
  glCallList(m_display_list);
}
