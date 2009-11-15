#include "primitive.hpp"
#include <iostream>
#include <limits>

#define EPSILON 0.1

Primitive::~Primitive()
{
}

Intersection Primitive::ray_intersect(Ray r) {
  return Intersection();
}

Sphere::~Sphere()
{
}

Intersection Sphere::ray_intersect(Ray r) {
  r.normalize();
  // x
  double x = r.eye[0];
  double xt = r.dir[0];
  // y
  double y = r.eye[1];
  double yt = r.dir[1];
  // z
  double z = r.eye[2];
  double zt = r.dir[2];

  // t^2 term
  double a = xt * xt + yt * yt + zt * zt;
  // t term
  double b = 2 * (x * xt + y * yt + z * zt);
  // constant term
  double c = x * x + y * y + z * z - 1;

  // Check discriminant
  double d = b * b - 4 * a * c;

  if (d > 0) {
    double root = (-b - sqrt(d)) / (2 * a);
    if (root < EPSILON) return Intersection();
    x = r.eye[0] + root * r.dir[0];
    y = r.eye[1] + root * r.dir[1];
    z = r.eye[2] + root * r.dir[2];
    Intersection ret(x, y, z);
    ret.normal[0] = x;
    ret.normal[1] = y;
    ret.normal[2] = z;
    return ret;
  }
  return Intersection();
}

Cube::~Cube()
{
}

Intersection Cube::ray_intersect(Ray r) {
  r.normalize();
  // x
  double x = r.eye[0];
  double xt = r.dir[0];
  // y
  double y = r.eye[1];
  double yt = r.dir[1];
  // z
  double z = r.eye[2];
  double zt = r.dir[2];

  double xi, yi, zi, t, dist;
  Intersection ret;
  double closest = std::numeric_limits<double>::max();

  // Check front face
  if (zt != 0) {
    t = (1 - z) / zt;
    xi = x + xt * t;
    yi = y + yt * t;
    zi = z + zt * t;
    dist = (Point3D(xi, yi, zi) - r.eye).length();
    if (xi >= 0 && xi <= 1 &&
        yi >= 0 && yi <= 1 &&
        dist < closest && t > EPSILON) {
      closest = dist;
      ret.hit = true;
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = 0;
      ret.normal[1] = 0;
      ret.normal[2] = 1;
    }
    // Check Back face
    t = (0 - z) / zt;
    xi = x + xt * t;
    yi = y + yt * t;
    zi = z + zt * t;
    dist = (Point3D(xi, yi, zi) - r.eye).length();
    if (xi >= 0 && xi <= 1 &&
        yi >= 0 && yi <= 1 &&
        dist < closest && t > EPSILON) {
      closest = dist;
      ret.hit = true;
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = 0;
      ret.normal[1] = 0;
      ret.normal[2] = -1;
    }
  }

  // Check side faces
  if (xt != 0) {
    // Check Right face
    t = (1 - x) / xt;
    xi = x + xt * t;
    yi = y + yt * t;
    zi = z + zt * t;
    dist = (Point3D(xi, yi, zi) - r.eye).length();
    if (zi >= 0 && zi <= 1 &&
        yi >= 0 && yi <= 1 &&
        dist < closest && t > EPSILON) {
      closest = dist;
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = 1;
      ret.normal[1] = 0;
      ret.normal[2] = 0;
      ret.hit = true;
    }
    // Check Left face
    t = (0 - x) / xt;
    xi = x + xt * t;
    yi = y + yt * t;
    zi = z + zt * t;
    dist = (Point3D(xi, yi, zi) - r.eye).length();
    if (zi >= 0 && zi <= 1 &&
        yi >= 0 && yi <= 1 &&
        dist < closest && t > EPSILON) {
      closest = dist;
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = -1;
      ret.normal[1] = 0;
      ret.normal[2] = 0;
      ret.hit = true;
    }
  }

  if (yt != 0) {
    // Check Top face
    t = (1 - y) / yt;
    xi = x + xt * t;
    yi = y + yt * t;
    zi = z + zt * t;
    dist = (Point3D(xi, yi, zi) - r.eye).length();
    if (zi >= 0 && zi <= 1 &&
        xi >= 0 && xi <= 1 &&
        dist < closest && t > EPSILON) {
      closest = dist;
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = 0;
      ret.normal[1] = 1;
      ret.normal[2] = 0;
      ret.hit = true;
    }
    // Check Bottom face
    t = (0 - y) / yt;
    xi = x + xt * t;
    yi = y + yt * t;
    zi = z + zt * t;
    dist = (Point3D(xi, yi, zi) - r.eye).length();
    if (zi >= 0 && zi <= 1 &&
        xi >= 0 && xi <= 1 &&
        dist < closest && t > EPSILON) {
      closest = dist;
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = 0;
      ret.normal[1] = -1;
      ret.normal[2] = 0;
      ret.hit = true;
    }
  }

  return ret;
}

NonhierSphere::~NonhierSphere()
{
}

Intersection NonhierSphere::ray_intersect(Ray r) {
  r.normalize();
  // x
  double x = r.eye[0] - m_pos[0];
  double xt = r.dir[0];
  // y
  double y = r.eye[1] - m_pos[1];
  double yt = r.dir[1];
  // z
  double z = r.eye[2] - m_pos[2];
  double zt = r.dir[2];

  // t^2 term
  double a = xt * xt + yt * yt + zt * zt;
  // t term
  double b = 2 * (x * xt + y * yt + z * zt);
  // constant term
  double c = x * x + y * y + z * z - (m_radius * m_radius);

  // Check discriminant
  double d = b * b - 4 * a * c;

  if (d > 0) {
    double root = (-b - sqrt(d)) / (2 * a);
    if (root < EPSILON) return Intersection();
    x = r.eye[0] + root * r.dir[0];
    y = r.eye[1] + root * r.dir[1];
    z = r.eye[2] + root * r.dir[2];
    Intersection ret(x, y, z);
    ret.normal[0] = x - m_pos[0];
    ret.normal[1] = y - m_pos[1];
    ret.normal[2] = z - m_pos[2];
    return ret;
  }
  return Intersection();
}

NonhierBox::~NonhierBox()
{
}

Intersection NonhierBox::ray_intersect(Ray r) {
  r.normalize();
  // x
  double x = r.eye[0];
  double xt = r.dir[0];
  // y
  double y = r.eye[1];
  double yt = r.dir[1];
  // z
  double z = r.eye[2];
  double zt = r.dir[2];

  double xi, yi, zi, t, dist;
  Intersection ret;
  double closest = std::numeric_limits<double>::max();

  // Check front face
  if (zt != 0) {
    t = (m_pos[2] + m_size - z) / zt;
    xi = x + xt * t;
    yi = y + yt * t;
    zi = z + zt * t;
    dist = (Point3D(xi, yi, zi) - r.eye).length();
    if (xi >= m_pos[0] && xi <= m_pos[0] + m_size &&
        yi >= m_pos[1] && yi <= m_pos[1] + m_size &&
        dist < closest && t > EPSILON) {
      closest = dist;
      ret.hit = true;
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = 0;
      ret.normal[1] = 0;
      ret.normal[2] = 1;
    }
    // Check Back face
    t = (m_pos[2] - z) / zt;
    xi = x + xt * t;
    yi = y + yt * t;
    zi = z + zt * t;
    dist = (Point3D(xi, yi, zi) - r.eye).length();
    if (xi >= m_pos[0] && xi <= m_pos[0] + m_size &&
        yi >= m_pos[1] && yi <= m_pos[1] + m_size &&
        dist < closest && t > EPSILON) {
      closest = dist;
      ret.hit = true;
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = 0;
      ret.normal[1] = 0;
      ret.normal[2] = -1;
    }
  }

  // Check side faces
  if (xt != 0) {
    // Check Right face
    t = (m_pos[0] + m_size - x) / xt;
    xi = x + xt * t;
    yi = y + yt * t;
    zi = z + zt * t;
    dist = (Point3D(xi, yi, zi) - r.eye).length();
    if (zi >= m_pos[2] && zi <= m_pos[2] + m_size &&
        yi >= m_pos[1] && yi <= m_pos[1] + m_size &&
        dist < closest && t > EPSILON) {
      closest = dist;
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = 1;
      ret.normal[1] = 0;
      ret.normal[2] = 0;
      ret.hit = true;
    }
    // Check Left face
    t = (m_pos[0] - x) / xt;
    xi = x + xt * t;
    yi = y + yt * t;
    zi = z + zt * t;
    dist = (Point3D(xi, yi, zi) - r.eye).length();
    if (zi >= m_pos[2] && zi <= m_pos[2] + m_size &&
        yi >= m_pos[1] && yi <= m_pos[1] + m_size &&
        dist < closest && t > EPSILON) {
      closest = dist;
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = -1;
      ret.normal[1] = 0;
      ret.normal[2] = 0;
      ret.hit = true;
    }
  }

  if (yt != 0) {
    // Check Top face
    t = (m_pos[1] + m_size - y) / yt;
    xi = x + xt * t;
    yi = y + yt * t;
    zi = z + zt * t;
    dist = (Point3D(xi, yi, zi) - r.eye).length();
    if (zi >= m_pos[2] && zi <= m_pos[2] + m_size &&
        xi >= m_pos[0] && xi <= m_pos[0] + m_size &&
        dist < closest && t > EPSILON) {
      closest = dist;
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = 0;
      ret.normal[1] = 1;
      ret.normal[2] = 0;
      ret.hit = true;
    }
    // Check Bottom face
    t = (m_pos[1] - y) / yt;
    xi = x + xt * t;
    yi = y + yt * t;
    zi = z + zt * t;
    dist = (Point3D(xi, yi, zi) - r.eye).length();
    if (zi >= m_pos[2] && zi <= m_pos[2] + m_size &&
        xi >= m_pos[0] && xi <= m_pos[0] + m_size &&
        dist < closest && t > EPSILON) {
      closest = dist;
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = 0;
      ret.normal[1] = -1;
      ret.normal[2] = 0;
      ret.hit = true;
    }
  }

  return ret;
}
