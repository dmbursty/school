#ifndef CSG_HPP
#define CSG_HPP

#include "scene.hpp"

class CSGNode : public SceneNode {
 public:
  CSGNode(const std::string& name, SceneNode* lchild, SceneNode* rchild)
    : SceneNode(name), lchild(lchild), rchild(rchild) {}

 protected:
  SceneNode* lchild;
  SceneNode* rchild;
};

class UnionNode : public CSGNode {
 public:
  UnionNode(const std::string& name, SceneNode* lchild, SceneNode* rchild)
    : CSGNode(name, lchild, rchild) {}

  virtual Intersection ray_intersect(Ray r);
};

#endif
