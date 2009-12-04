#ifndef CS488_MATERIAL_HPP
#define CS488_MATERIAL_HPP

#include "algebra.hpp"
#include "image.hpp"

class Material {
public:
  Material(const Colour& kd, const Colour& ks, double shininess,
           double reflect, double refract);
  virtual ~Material();

  virtual void apply_gl() const;
  Colour diffuse() { return m_kd; }
  Colour specular() { return m_ks; }
  double shininess() { return m_shininess; }
  double reflect() { return m_reflect; }
  double refract() { return m_refract; }

  void set_texture(Image* texture) { _texture = texture; }
  void set_bumpmap(Image* bumpmap) { _bumpmap = bumpmap; }
  Image* texture() { return _texture; }
  Image* bumpmap() { return _bumpmap; }

private:
  Colour m_kd;
  Colour m_ks;

  double m_shininess;
  double m_reflect;
  double m_refract;

  Image* _texture;
  Image* _bumpmap;
};


#endif
