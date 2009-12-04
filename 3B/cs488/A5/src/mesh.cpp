#include "mesh.hpp"
#include <iostream>
#include <limits>
#include <algorithm>
using std::max;

#define EPSILON 0.0001

Mesh::Mesh(const std::vector<Point3D>& verts,
           const std::vector< std::vector<int> >& faces)
  : m_verts(verts),
    m_faces(faces)
{
}

std::ostream& operator<<(std::ostream& out, const Mesh& mesh)
{
  std::cerr << "mesh({";
  for (std::vector<Point3D>::const_iterator I = mesh.m_verts.begin(); I != mesh.m_verts.end(); ++I) {
    if (I != mesh.m_verts.begin()) std::cerr << ",\n      ";
    std::cerr << *I;
  }
  std::cerr << "},\n\n     {";
  
  for (std::vector<Mesh::Face>::const_iterator I = mesh.m_faces.begin(); I != mesh.m_faces.end(); ++I) {
    if (I != mesh.m_faces.begin()) std::cerr << ",\n      ";
    std::cerr << "[";
    for (Mesh::Face::const_iterator J = I->begin(); J != I->end(); ++J) {
      if (J != I->begin()) std::cerr << ", ";
      std::cerr << *J;
    }
    std::cerr << "]";
  }
  std::cerr << "});" << std::endl;
  return out;
}

Intersection Mesh::ray_intersect(Ray r) {
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

  Intersection ret;
  double closest = std::numeric_limits<double>::max();

  for (std::vector<Face>::iterator I = m_faces.begin(); I != m_faces.end(); ++I) {
    // Intersect face
    Point3D v1 = m_verts[(*I)[0]];
    Point3D v2 = m_verts[(*I)[1]];
    Point3D v3 = m_verts[(*I)[2]];
    Vector3D normal = (v3 - v2).cross(v1 - v2);
    double D = -1 * (normal[0] * v1[0] + normal[1] * v1[1] + normal[2] * v1[2]);
    // Solve at + b = 0 for planar point
    double b = (normal[0] * x + normal[1] * y + normal[2] * z + D);
    double a = (normal[0] * xt + normal[1] * yt + normal[2] * zt);
    double t = -1 * (b / a);
    if (t < EPSILON) continue;
    Point3D inter(x + xt * t, y + yt * t, z + zt * t);
    int numVert = (*I).size();
    bool inFace = true;
    for (int i = 0; i < numVert; ++i) {
      // Check if point is inside
      int thisVert = (*I)[i];
      int nextVert = (*I)[(i+1) % numVert];
      Vector3D test = (m_verts[nextVert] - m_verts[thisVert]).cross(inter - m_verts[thisVert]);
      if (test.dot(normal) < 0) {
        inFace = false;
        break;
      }
    }
    if (inFace) {
      double dist = (inter - r.eye).length();
      if (dist < closest) {
        closest = dist;
        ret.map_x = -1;
        ret.map_y = -1;
        ret.pt = inter;
        ret.normal = normal;
        ret.hit = true;
      }
    }
  }

  return ret;
}

double* Mesh::getBoundingBox() {
  // Goint to return the box as (x, y, z, r)
  Point3D p = m_verts.front();
  // 0-2 mins, 3-5 maxs
  double* bounds = new double[6];
  bounds[0] = p[0];
  bounds[1] = p[1];
  bounds[2] = p[2];
  bounds[3] = p[0] + 0.001;
  bounds[4] = p[1] + 0.001;
  bounds[5] = p[2] + 0.001;
  for (std::vector<Point3D>::iterator I = m_verts.begin(); I != m_verts.end(); ++I) {
    p = (*I);
    if (p[0] < bounds[0]) bounds[0] = p[0];
    if (p[1] < bounds[1]) bounds[1] = p[1];
    if (p[2] < bounds[2]) bounds[2] = p[2];
    if (p[0] > bounds[3]) bounds[3] = p[0];
    if (p[1] > bounds[4]) bounds[4] = p[1];
    if (p[2] > bounds[5]) bounds[5] = p[2];
  }
  return bounds;
}
