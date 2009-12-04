#ifndef CONFIG_HPP
#define CONFIG_HPP

#include "image.hpp"
#include "algebra.hpp"
#include "scene.hpp"
#include "light.hpp"
#include "queue.hpp"

struct RenderConfig {
  // Image Parameters
  int height, width;
  Image* img;
  Image* bg;

  // Viewing Parameters
  Point3D eye;
  Vector3D view;
  Vector3D up;
  Vector3D side;
  double fov;

  // Scene Parameters
  SceneNode* root;
  const Colour& ambient;
  const std::list<Light*>& lights;

  // Threading Parameters
  Queue& queue;

  // Misc Parameters
  double x_inc, y_inc;

  RenderConfig(const Colour& ambient, const std::list<Light*>& lights, Queue& queue)
      : ambient(ambient), lights(lights), queue(queue) {}
};

#endif
