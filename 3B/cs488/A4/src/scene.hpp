#ifndef SCENE_HPP
#define SCENE_HPP

#include <list>
#include "algebra.hpp"
#include "primitive.hpp"
#include "material.hpp"
#include "ray.hpp"

class BoundingNode;

class SceneNode {
public:
  SceneNode(const std::string& name);
  virtual ~SceneNode();

  virtual Intersection ray_intersect(Ray r);

  const Matrix4x4& get_transform() const { return m_trans; }
  const Matrix4x4& get_inverse() const { return m_invtrans; }
  
  void set_transform(const Matrix4x4& m)
  {
    m_trans = m;
    m_invtrans = m.invert();
  }

  void set_transform(const Matrix4x4& m, const Matrix4x4& i)
  {
    m_trans = m;
    m_invtrans = i;
  }

  void add_child(SceneNode* child)
  {
    m_children.push_back(child);
  }

  void remove_child(SceneNode* child)
  {
    m_children.remove(child);
  }

  std::string get_name() { return m_name; }

  // Callbacks to be implemented.
  // These will be called from Lua.
  void rotate(char axis, double angle, bool right = true);
  void scale(const Vector3D& amount);
  void translate(const Vector3D& amount, bool right = true);

  // Returns true if and only if this node is a JointNode
  virtual bool is_joint() const;

  virtual BoundingNode* generateBounds();
  
protected:
  
  // Useful for picking
  int m_id;
  std::string m_name;

  // Transformations
  Matrix4x4 m_trans;
  Matrix4x4 m_invtrans;

  // Hierarchy
  typedef std::list<SceneNode*> ChildList;
  ChildList m_children;
};

class JointNode : public SceneNode {
public:
  JointNode(const std::string& name);
  virtual ~JointNode();

  virtual bool is_joint() const;

  void set_joint_x(double min, double init, double max);
  void set_joint_y(double min, double init, double max);

  struct JointRange {
    double min, init, max;
  };

  
protected:

  JointRange m_joint_x, m_joint_y;
};

class GeometryNode : public SceneNode {
public:
  GeometryNode(const std::string& name,
               Primitive* primitive);
  virtual ~GeometryNode();

  virtual Intersection ray_intersect(Ray r);
  virtual BoundingNode* generateBounds();

  const Material* get_material() const;
  Material* get_material()
  {
    return m_material;
  }

  void set_material(Material* material)
  {
    m_material = material;
  }

  void set_primitive(Primitive* primitive)
  {
    m_primitive = primitive;
  }

protected:
  Material* m_material;
  Primitive* m_primitive;
};

class BoundingNode : public SceneNode {
public:
  BoundingNode(const std::string& name,
               GeometryNode* bound,
               GeometryNode* obj);
  virtual ~BoundingNode();

  virtual Intersection ray_intersect(Ray r);

protected:
  GeometryNode* m_bound;
  GeometryNode* m_obj;
};

#endif