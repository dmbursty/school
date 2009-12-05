#include "ray.hpp"
#include "algebra.hpp"

Ray::Ray() {}

Ray::Ray(Point3D eye, Vector3D dir) : eye(eye), dir(dir) {}

void Ray::transform(const Matrix4x4& m) {
  eye = m * eye;
  dir = m * dir;
}

void Ray::normalize() {
  dir.normalize();
}

// Get a ray jittered around this ray within sphere of given radius
Ray Ray::jitterSphere(double radius) {
  // Get random angle from 0 to 2 pi - lattitude
  double lat = ((rand() % 1000) / 1000.0) * 2 * M_PI;
  // Get random angle from 0 to pi - longitude
  double lon = ((rand() % 1000) / 1000.0) * M_PI;
  // Get random distance 0 - 1
  double dist = ((rand() % 1000) / 1000.0);

  // Determine vector to move ray based on lat and lon
  Vector3D diff(-1 * cos(lat) * cos(lon),
                sin(lat),
                cos(lat) * sin(lon));
  diff = (radius * dist) * diff;
  Vector3D new_dir = dir + diff;
  return Ray(eye, new_dir);
}

Ray Ray::jitterCone(double radius) {
  // Generate a vector that is perpendicular to this ray's direction
  Vector3D a;
  if (dir[0] != 0) {
    a[0] = (dir[1] + dir[2]) / dir[0];
    a[1] = -1;
    a[2] = -1;
  } else if (dir[1] != 0) {
    a[0] = -1;
    a[1] = (dir[0] + dir[2]) / dir[1];
    a[2] = -1;
  } else if (dir[2] != 0) {
    a[0] = -1;
    a[1] = -1;
    a[2] = (dir[0] + dir[1]) / dir[2];
  } else {
    // Ray is zero vector! shouldn't happen
    return Ray(eye, dir);
  }

  // Generate second vector perpendicular to the two other vectors
  a.normalize();
  Vector3D b = a.cross(dir);
  b.normalize();

  // a and b now are an orthonormal basis of the plane defined by dir
  //a = radius * ((rand() % 2000) / 1000.0 - 1) * a;
  //b = radius * ((rand() % 2000) / 1000.0 - 1) * b;
  //return Ray(eye, dir + a + b);

  // Get random angle from 0 to 2 pi
  double angle = ((rand() % 1000) / 1000.0) * 2 * M_PI;
  // Get random distance 0 - 1
  double dist = ((rand() % 1000) / 1000.0);
  // Find a vector for the new direction
  Vector3D diff = dist * radius * (cos(angle) * a + sin(angle) * b);
  // Return the new jittered ray
  return Ray(eye, dir + diff);
}

Intersection::Intersection() : hit(false), inside(false), dist(0) {}

Intersection::Intersection(double x, double y, double z) {
  pt[0] = x;
  pt[1] = y;
  pt[2] = z;
  hit = true;
  inside = false;
}

Intersection::Intersection(double x, double y, double z, GeometryNode* node) {
  pt[0] = x;
  pt[1] = y;
  pt[2] = z;
  hit = true;
  inside = false;
  this->node = node;
}

double Intersection::dist_from(Point3D& p) {
  return (p - pt).length();
}

void Intersection::transform(const Matrix4x4& trans, const Matrix4x4& inv) {
  pt = trans * pt;
  Matrix4x4 inv_trans = inv.transpose();
  normal = inv_trans * normal;
}

void Intersections::addInter(Intersection i) {
  inter.push_back(i);
}

void Intersections::sort(Point3D& eye) {
  for (unsigned int i = 0; i < inter.size(); i++) {
    inter[i].dist = inter[i].dist_from(eye);
  }
  intersect_compare c;
  std::sort(inter.begin(), inter.end(), c);
}

void Intersections::transform(const Matrix4x4& trans, const Matrix4x4& inv) {
  for (unsigned int i = 0; i < inter.size(); i++) {
    inter[i].transform(trans, inv);
  }
}
