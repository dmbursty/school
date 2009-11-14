#include "a4.hpp"
#include "image.hpp"
#include "ray.hpp"

#define DEG2RAD 0.0174532925

void a4_render(// What to render
               SceneNode* root,
               // Where to output the image
               const std::string& filename,
               // Image size
               int width, int height,
               // Viewing parameters
               const Point3D& eye, const Vector3D& view,
               const Vector3D& up, double fov,
               // Lighting parameters
               const Colour& ambient,
               const std::list<Light*>& lights
               )
{
  // Fill in raytracing code here.
  Image img(width, height, 3);

  double x_inc = tan((fov/2) * DEG2RAD) / (width / 2.0);
  double y_inc = tan((fov/2) * DEG2RAD) / (height / 2.0);

  Vector3D direction;

  Vector3D norm_view = view;
  Vector3D norm_up = up;
  Vector3D side = view.cross(up);

  norm_view.normalize();
  norm_up.normalize();
  side.normalize();

  int numPixels = height * width;
  int processedRays = 0;

  for (int y = -height / 2; y < height / 2; y++) {
    for (int x = -width / 2; x < width / 2; x++) {
      int percent = 100 * (++processedRays / (double)numPixels);
      std::cerr << percent << "%\r";
      direction = norm_view +
                  y_inc * y * norm_up +
                  x_inc * x * side;

      Ray r(eye, direction);
      Intersection i = root->ray_intersect(r);
      if (i.hit) {
        PhongMaterial* m = dynamic_cast<PhongMaterial*>(i.node->get_material());
        // Lighting calculations
        double r = 0;
        double b = 0;
        double g = 0;
        // Ambient lighting
        r += ambient.R() * m->diffuse().R();
        b += ambient.B() * m->diffuse().B();
        g += ambient.G() * m->diffuse().G();

        for (std::list<Light*>::const_iterator I = lights.begin(); I != lights.end(); ++I) {
          // Get light vector
          Light* light = *I;
          Vector3D light_dir = light->position - i.pt;
          double dist = light_dir.length();
          light_dir.normalize();
          i.normal.normalize();

          // Check intersection
          Ray light_ray(i.pt, light_dir);
          Intersection light_inter = root->ray_intersect(light_ray);
          if (light_inter.hit) continue;

          // Diffuse
          double surf = i.normal.dot(light_dir);
          double attenuation = light->falloff[0] +
                               light->falloff[1] * dist +
                               light->falloff[2] * dist * dist;
          r += (light->colour.R() * m->diffuse().R() * surf) / attenuation;
          g += (light->colour.G() * m->diffuse().G() * surf) / attenuation;
          b += (light->colour.B() * m->diffuse().B() * surf) / attenuation;

          // Specular
          Vector3D reflect = (-1 * light_dir) + 2 * (surf) * i.normal;
          double spec = pow(reflect.dot(-1 * norm_view), m->shininess());
          reflect.normalize();
          r += light->colour.R() * spec * m->specular().R();
          g += light->colour.G() * spec * m->specular().G();
          b += light->colour.B() * spec * m->specular().B();
        }

        img(x + width/2, height/2 - y - 1, 0) = r;
        img(x + width/2, height/2 - y - 1, 1) = g;
        img(x + width/2, height/2 - y - 1, 2) = b;
      }
    }
  }
  std::cerr << std::endl;


  std::cerr << "Stub: a4_render(" << root << ",\n     "
            << filename << ", " << width << ", " << height << ",\n     "
            << eye << ", " << view << ", " << up << ", " << fov << ",\n     "
            << ambient << ",\n     {";

  for (std::list<Light*>::const_iterator I = lights.begin(); I != lights.end(); ++I) {
    if (I != lights.begin()) std::cerr << ", ";
    std::cerr << **I;
  }
  std::cerr << "});" << std::endl;

  // For now, just make a sample image.


  img.savePng(filename);

}
