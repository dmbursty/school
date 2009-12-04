#include "material.hpp"

Material::Material(const Colour& kd, const Colour& ks, double shininess,
                   double reflect, double refract)
  : m_kd(kd), m_ks(ks), m_shininess(shininess),
    m_reflect(reflect), m_refract(refract), _texture(NULL), _bumpmap(NULL)
{
}

Material::~Material()
{
}

void Material::apply_gl() const
{
  // Perform OpenGL calls necessary to set up this material.
}
