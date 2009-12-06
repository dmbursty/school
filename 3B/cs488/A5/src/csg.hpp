#ifndef CSG_HPP
#define CSG_HPP

#include "scene.hpp"

class CSGNode : public SceneNode {
 public:
  CSGNode(const std::string& name, SceneNode* lchild, SceneNode* rchild)
    : SceneNode(name), lchild(lchild), rchild(rchild) {}

  virtual Intersections ray_intersect(Ray r);

 protected:
  SceneNode* lchild;
  SceneNode* rchild;

  virtual void handle(Intersections& inters, bool in_l, bool in_r) = 0;
};

class UnionNode : public CSGNode {
 public:
  UnionNode(const std::string& name, SceneNode* lchild, SceneNode* rchild)
    : CSGNode(name, lchild, rchild) {}

  virtual void handle(Intersections& inters, bool in_l, bool in_r);
  virtual BoundingNode* generateBounds();
};

class IntersectNode : public CSGNode {
 public:
  IntersectNode(const std::string& name, SceneNode* lchild, SceneNode* rchild)
    : CSGNode(name, lchild, rchild) {}

  virtual void handle(Intersections& inters, bool in_l, bool in_r);
  virtual BoundingNode* generateBounds();
};

class DifferenceNode : public CSGNode {
 public:
  DifferenceNode(const std::string& name, SceneNode* lchild, SceneNode* rchild)
    : CSGNode(name, lchild, rchild) {}

  virtual void handle(Intersections& inters, bool in_l, bool in_r);
  virtual BoundingNode* generateBounds();
};




#endif
