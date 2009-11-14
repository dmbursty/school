#include "material.hpp"
#include <GL/gl.h>

Material::~Material()
{
}

PhongMaterial::PhongMaterial(const Colour& kd, const Colour& ks, double shininess)
  : m_kd(kd), m_ks(ks), m_shininess(shininess)
{
}

PhongMaterial::~PhongMaterial()
{
}

void PhongMaterial::apply_gl() const
{
  GLfloat d[4] = {m_kd.R(), m_kd.G(), m_kd.B(), 1.0};
  GLfloat s[4] = {m_ks.R(), m_ks.G(), m_ks.B(), 1.0};
  glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, d);
  glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, s);
  glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, m_shininess);
}
