#include "primitive.hpp"
#include "polyroots.hpp"
#include <iostream>

#define PI 3.14159265
#define EPSILON 0.00001

Primitive::~Primitive()
{
}

Intersections Primitive::ray_intersect(Ray r) {
  // Suppress unused parameter warning
  (void) r;
  return Intersections();
}

double* Primitive::getBoundingBox() {
  return NULL;
}

///////////////
//   Torus   //
///////////////

Torus::~Torus() {}

Intersections Torus::ray_intersect(Ray ray) {
  ray.normalize();
  // x
  double x = ray.eye[0];
  double xt = ray.dir[0];
  // y
  double y = ray.eye[1];
  double yt = ray.dir[1];
  // z
  double z = ray.eye[2];
  double zt = ray.dir[2];

  // d dot d
  double dd = xt * xt + yt * yt + zt * zt;
  // p dot p
  double pp = x * x + y * y + z * z;
  // p dot d
  double pd = x * xt + y * yt + z * zt;
  // s
  double s = pp - r * r - 1;

  double a = dd * dd;
  double b = 4 * dd * pd;
  b /= a;
  double c = 4 * pd * pd + 2 * dd * s + 4 * zt * zt;
  c /= a;
  double d = 4 * pd * s + 8 * z * zt;
  d /= a;
  double e = s * s + 4 * z * z - 4 * r * r;
  e /= a;

  double root[4];
  size_t roots = quarticRoots(b, c, d, e, root);

  double xi, yi, zi;
  Intersections rets;
  Intersection ret;
  ret.map_y = 0;
  for (unsigned int i = 0; i < roots; i++) {
    xi = ray.eye[0] + root[i] * ray.dir[0];
    yi = ray.eye[1] + root[i] * ray.dir[1];
    zi = ray.eye[2] + root[i] * ray.dir[2];
    if (root[i] > EPSILON) {
      ret.hit = true;
      // Texture map
      Vector3D up(0, 1, 0);
      Vector3D through(0, 0, 1);
      Vector3D around(xi, yi, 0);
      around.normalize();
      double theta = acos( up.dot(around) ) / (2 * PI);
      if (through.cross(up).dot(around) > 0) {
        ret.map_x = theta;
      } else {
        ret.map_x = 1 - theta;
      }

      Point3D inTorus(around[0], around[1], around[2]);
      Point3D point(xi, yi, zi);
      Vector3D p = point - inTorus;
      Vector3D tangent = through.cross(around);
      p.normalize();
      tangent.normalize();
      double phi = acos( through.dot(p) ) / (2 * PI);
      if (tangent.cross(through).dot(p) > 0) {
        ret.map_y = phi;
      } else {
        ret.map_y = 1 - phi;
      }

      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      double n = xi * xi + yi * yi + zi * zi - r * r - 1;
      ret.normal[0] = 4 * xi * n;
      ret.normal[1] = 4 * yi * n;
      ret.normal[2] = 4 * zi * n + 8 * zi;
      rets.addInter(ret);
    }
  }
  return rets;
}

///////////////
// Cylinder  //
///////////////

Cylinder::~Cylinder() {}

Intersections Cylinder::ray_intersect(Ray r) {
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

  // Cylinder: x^2 + z^2 = 1

  // t^2 term
  double a = xt * xt + zt * zt;
  // t term
  double b = 2 * (x * xt + z * zt);
  // constant term
  double c = x * x + z * z - 1;

  double xi, yi, zi, t;
  Intersections rets;
  Intersection ret;

  double roots[2];
  size_t n = quadraticRoots(a, b, c, roots);

  ret.map_x = 0;
  ret.map_y = 0;

  if (n > 0) {
    // Check first root
    double root = roots[0];
    xi = r.eye[0] + root * r.dir[0];
    yi = r.eye[1] + root * r.dir[1];
    zi = r.eye[2] + root * r.dir[2];
    if (yi >= 0 && yi <= 1 && root > EPSILON) {
      ret.hit = true;
      Vector3D north(0, 1, 0);
      Vector3D equator(0, 0, -1);
      Vector3D point(xi, 0, zi);
      point.normalize();
      double phi = acos(-1 * north.dot(point));
      ret.map_y = yi;
      double theta = ( acos( point.dot(equator) / sin(phi)) ) / (2 * PI);
      if (north.cross(equator).dot(point) > 0) {
        ret.map_x = theta;
      } else {
        ret.map_x = 1 - theta;
      }
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = xi;
      ret.normal[1] = 0;
      ret.normal[2] = zi;
      rets.addInter(ret);
    }
    // Check second root
    root = roots[1];
    xi = r.eye[0] + root * r.dir[0];
    yi = r.eye[1] + root * r.dir[1];
    zi = r.eye[2] + root * r.dir[2];
    if (yi >= 0 && yi <= 1 && root > EPSILON) {
      ret.hit = true;
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = xi;
      ret.normal[1] = 0;
      ret.normal[2] = zi;
      ret.inside = true;
      rets.addInter(ret);
    }
    // Check bottom plane intersection (y = 0)
    t = -y / yt;
    xi = x + xt * t;
    yi = y + yt * t;
    zi = z + zt * t;
    if ((xi * xi + zi * zi) < 1 && t > EPSILON) {
      ret.hit = true;
      ret.map_x = (xi+1) / 4; // Divide by two cause our texture is too long
      ret.map_y = (zi+1) / 2;
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = 0;
      ret.normal[1] = -1;
      ret.normal[2] = 0;
      rets.addInter(ret);
    }
    // Check top plane intersection (y = 1)
    t = (1-y) / yt;
    xi = x + xt * t;
    yi = y + yt * t;
    zi = z + zt * t;
    if ((xi * xi + zi * zi) < 1 && t > EPSILON) {
      ret.hit = true;
      ret.map_x = (xi+1) / 4; // Divide by two cause our texture is too long
      ret.map_y = (zi+1) / 2;
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = 0;
      ret.normal[1] = 1;
      ret.normal[2] = 0;
      rets.addInter(ret);
    }
  }
  return rets;
}

///////////////
//   Cone    //
///////////////

Cone::~Cone() {}

Intersections Cone::ray_intersect(Ray r) {
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
  double a = xt * xt + zt * zt - yt * yt;
  // t term
  double b = 2 * (x * xt + z * zt - y * yt);
  // constant term
  double c = x * x + z * z - y * y;

  // Check discriminant
  double d = b * b - 4 * a * c;

  double xi, yi, zi, t;
  Intersections rets;
  Intersection ret;

  ret.map_x = 0;
  ret.map_y = 0;

  if (d > 0) {
    // Check first root
    double root = (-b - sqrt(d)) / (2 * a);
    xi = r.eye[0] + root * r.dir[0];
    yi = r.eye[1] + root * r.dir[1];
    zi = r.eye[2] + root * r.dir[2];
    if (yi >= 0 && yi <= 1 && root > EPSILON) {
      ret.hit = true;
      // Find longitude
      Vector3D p(xi, 0, zi);
      p.normalize();
      Vector3D equator(0, 0, -1);
      Vector3D north(0, 1, 0);
      double theta = acos( equator.dot(p) ) / (2 * PI);
      if (north.cross(equator).dot(p) > 0) {
        ret.map_x = theta;
      } else {
        ret.map_x = 1 - theta;
      }
      ret.map_y = yi;
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = xi;
      ret.normal[1] = -yi;
      ret.normal[2] = zi;
      rets.addInter(ret);
    }
    // Check second root
    root = (-b + sqrt(d)) / (2 * a);
    xi = r.eye[0] + root * r.dir[0];
    yi = r.eye[1] + root * r.dir[1];
    zi = r.eye[2] + root * r.dir[2];
    if (yi >= 0 && yi <= 1 && root > EPSILON) {
      ret.hit = true;
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = xi;
      ret.normal[1] = -yi;
      ret.normal[2] = zi;
      rets.addInter(ret);
    }
    // Check bottom plane intersection (y = 1)
    t = (1-y) / yt;
    xi = x + xt * t;
    yi = y + yt * t;
    zi = z + zt * t;
    if ((xi * xi + zi * zi) < 1 && t > EPSILON) {
      ret.hit = true;
      ret.map_x = (xi+1) / 4; // Divide by two cause our texture is too long
      ret.map_y = (zi+1) / 2;
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = 0;
      ret.normal[1] = 1;
      ret.normal[2] = 0;
      rets.addInter(ret);
    }
  }
  return rets;
}

///////////////
//  Sphere   //
///////////////

Sphere::~Sphere()
{
}

Intersections Sphere::ray_intersect(Ray r) {
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

  // Numerical instability causing weird pixels so fix here
  // if (xt == 0) xt += EPSILON;

  // t^2 term
  double a = xt * xt + yt * yt + zt * zt;
  // t term
  double b = 2 * (x * xt + y * yt + z * zt);
  // constant term
  double c = x * x + y * y + z * z - 1;

  double xi, yi, zi;
  Intersections rets;
  Intersection ret;

  double roots[2];
  size_t n = quadraticRoots(a, b, c, roots);

  ret.map_x = 0;
  ret.map_y = 0;

  if (n == 1) std::cout << "TANGENT" << std::endl;

  if (n > 0) {
    // Check first root
    double root = roots[1];
    xi = r.eye[0] + root * r.dir[0];
    yi = r.eye[1] + root * r.dir[1];
    zi = r.eye[2] + root * r.dir[2];
    if (root > EPSILON) {
      ret.hit = true;
      Vector3D north(0, 1, 0);
      Vector3D equator(0, 0, -1);
      Vector3D point(xi, yi, zi);
      double phi = acos(-1 * north.dot(point));
      ret.map_y = 1 - phi / PI;
      double theta = ( acos( point.dot(equator) / sin(phi)) ) / (2 * PI);
      if (north.cross(equator).dot(point) > 0) {
        ret.map_x = theta;
      } else {
        ret.map_x = 1 - theta;
      }
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = xi;
      ret.normal[1] = yi;
      ret.normal[2] = zi;
      rets.addInter(ret);
    }
    // Check second root
    root = roots[0];
    xi = r.eye[0] + root * r.dir[0];
    yi = r.eye[1] + root * r.dir[1];
    zi = r.eye[2] + root * r.dir[2];
    if (root > EPSILON) {
      ret.hit = true;
      Vector3D north(0, 1, 0);
      Vector3D equator(0, 0, -1);
      Vector3D point(xi, yi, zi);
      double phi = acos(-1 * north.dot(point));
      ret.map_y = 1 - phi / PI;
      double theta = ( acos( point.dot(equator) / sin(phi)) ) / (2 * PI);
      if (north.cross(equator).dot(point) > 0) {
        ret.map_x = theta;
      } else {
        ret.map_x = 1 - theta;
      }
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = xi;
      ret.normal[1] = yi;
      ret.normal[2] = zi;
      ret.inside = true;
      rets.addInter(ret);
    }
    return rets;
  }
  return Intersections();
}

///////////////
//   Cube    //
///////////////

Cube::~Cube()
{
}

Intersections Cube::ray_intersect(Ray r) {
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

  double xi, yi, zi, t;
  Intersections rets;
  Intersection ret;
  ret.map_x = 0;
  ret.map_y = 0;

  // Check front face
  if (zt != 0) {
    t = (1 - z) / zt;
    xi = x + xt * t;
    yi = y + yt * t;
    zi = z + zt * t;
    if (xi >= 0 && xi <= 1 &&
        yi >= 0 && yi <= 1 &&
        t > EPSILON) {
      ret.hit = true;
      ret.map_x = xi;
      ret.map_y = yi;
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = 0;
      ret.normal[1] = 0;
      ret.normal[2] = 1;
      rets.addInter(ret);
    }
    // Check Back face
    t = (0 - z) / zt;
    xi = x + xt * t;
    yi = y + yt * t;
    zi = z + zt * t;
    if (xi >= 0 && xi <= 1 &&
        yi >= 0 && yi <= 1 &&
        t > EPSILON) {
      ret.hit = true;
      ret.map_x = xi;
      ret.map_y = yi;
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = 0;
      ret.normal[1] = 0;
      ret.normal[2] = -1;
      rets.addInter(ret);
    }
  }

  // Check side faces
  if (xt != 0) {
    // Check Right face
    t = (1 - x) / xt;
    xi = x + xt * t;
    yi = y + yt * t;
    zi = z + zt * t;
    if (zi >= 0 && zi <= 1 &&
        yi >= 0 && yi <= 1 &&
        t > EPSILON) {
      ret.map_x = zi;
      ret.map_y = yi;
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = 1;
      ret.normal[1] = 0;
      ret.normal[2] = 0;
      ret.hit = true;
      rets.addInter(ret);
    }
    // Check Left face
    t = (0 - x) / xt;
    xi = x + xt * t;
    yi = y + yt * t;
    zi = z + zt * t;
    if (zi >= 0 && zi <= 1 &&
        yi >= 0 && yi <= 1 &&
        t > EPSILON) {
      ret.map_x = 1-zi;
      ret.map_y = yi;
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = -1;
      ret.normal[1] = 0;
      ret.normal[2] = 0;
      ret.hit = true;
      rets.addInter(ret);
    }
  }

  if (yt != 0) {
    // Check Top face
    t = (1 - y) / yt;
    xi = x + xt * t;
    yi = y + yt * t;
    zi = z + zt * t;
    if (zi >= 0 && zi <= 1 &&
        xi >= 0 && xi <= 1 &&
        t > EPSILON) {
      ret.map_x = xi;
      ret.map_y = zi;
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = 0;
      ret.normal[1] = 1;
      ret.normal[2] = 0;
      ret.hit = true;
      rets.addInter(ret);
    }
    // Check Bottom face
    t = (0 - y) / yt;
    xi = x + xt * t;
    yi = y + yt * t;
    zi = z + zt * t;
    if (zi >= 0 && zi <= 1 &&
        xi >= 0 && xi <= 1 &&
        t > EPSILON) {
      ret.map_x = xi;
      ret.map_y = 1-zi;
      ret.pt[0] = xi;
      ret.pt[1] = yi;
      ret.pt[2] = zi;
      ret.normal[0] = 0;
      ret.normal[1] = -1;
      ret.normal[2] = 0;
      ret.hit = true;
      rets.addInter(ret);
    }
  }

  return rets;
}

////////////////////
// Bounding Boxes //
////////////////////

double* Sphere::getBoundingBox() {
  double* bounds = new double[7];
  bounds[0] = -1; bounds[1] = -1; bounds[2] = -1;
  bounds[3] = 1; bounds[4] = 1; bounds[5] = 1;
  bounds[6] = 0;
  return bounds;
}

double* Cube::getBoundingBox() {
  double* bounds = new double[7];
  bounds[0] = 0; bounds[1] = 0; bounds[2] = 0;
  bounds[3] = 1; bounds[4] = 1; bounds[5] = 1;
  bounds[6] = 0;
  return bounds;
}

double* Cylinder::getBoundingBox() {
  double* bounds = new double[7];
  bounds[0] = -1; bounds[1] = 0; bounds[2] = -1;
  bounds[3] = 1; bounds[4] = 1; bounds[5] = 1;
  bounds[6] = 0;
  return bounds;
}

double* Cone::getBoundingBox() {
  double* bounds = new double[7];
  bounds[0] = -1; bounds[1] = 0; bounds[2] = -1;
  bounds[3] = 1; bounds[4] = 1; bounds[5] = 1;
  bounds[6] = 0;
  return bounds;
}

double* Torus::getBoundingBox() {
  double* bounds = new double[7];
  bounds[0] = -1 - r; bounds[1] = -1 - r; bounds[2] = -r;
  bounds[3] = 1 + r; bounds[4] = 1 + r; bounds[5] = r;
  bounds[6] = 0;
  return bounds;
}
