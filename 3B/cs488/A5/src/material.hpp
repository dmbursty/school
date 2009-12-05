#ifndef CS488_MATERIAL_HPP
#define CS488_MATERIAL_HPP

#include "algebra.hpp"
#include "image.hpp"

class Material {
public:
  Material(const Colour& kd, const Colour& ks, double shininess,
           double reflect, double refract, double gloss);
  virtual ~Material();

  virtual void apply_gl() const;

  // Getters
  Colour diffuse() { return m_kd; }
  Colour specular() { return m_ks; }
  double shininess() { return m_shininess; }
  double reflect() { return m_reflect; }
  double refract() { return m_refract; }
  double gloss() { return m_gloss; }
  Image* texture() { return m_texture; }
  Image* bumpmap() { return m_bumpmap; }

  // Setters
  void set_reflect(double r) { m_reflect = r; }
  void set_refract(double r) { m_refract = r; }
  void set_gloss(double g) { m_gloss = g; }
  void set_texture(Image* texture) { m_texture = texture; }
  void set_bumpmap(Image* bumpmap) { m_bumpmap = bumpmap; }

private:
  Colour m_kd;
  Colour m_ks;

  double m_shininess;
  double m_reflect;
  double m_refract;
  double m_gloss;

  Image* m_texture;
  Image* m_bumpmap;
};


#endif
