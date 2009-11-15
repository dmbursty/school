#include "a4.hpp"
#include "image.hpp"
#include "ray.hpp"

#define DEG2RAD 0.0174532925

// Lighting options
#define AMBIENT 1
#define DIFFUSE 1
#define SPECULAR 1
#define SHADOWS 1
#define ANTIALIAS 5
//#define BOUNDS 1
//#define NORMAL 1

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
  // Colour Background
  for (int x = 0; x < width; x++) {
    for (int y = 0; y < height; y++) {
      double xx = x / (double)width;
      double yy = y / (double)height;
      img(x, y, 0) = 1 - xx - yy;
      img(width - x - 1, y, 2) = 1 - xx - yy;
    }
  }

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
  int processedPixels = 0;

  for (int y = -height / 2; y < height / 2; y++) {
    for (int x = -width / 2; x < width / 2; x++) {
      // Print current progress
      int percent = 100 * (++processedPixels / (double)numPixels);
      std::cerr << percent << "%\r";

      double r_sum = 0;
      double g_sum = 0;
      double b_sum = 0;

      for (int supersample = 0; supersample < ANTIALIAS; supersample++) {
        // Cast ray
        if (supersample == 0) {
          direction = norm_view +
                      y_inc * y * norm_up +
                      x_inc * x * side;
        } else {
          double x_rand = ((rand() % 2001) / 1000) - 1;
          double y_rand = ((rand() % 2001) / 1000) - 1;
          direction = norm_view +
                      y_inc * (y + y_rand) * norm_up +
                      x_inc * (x + x_rand) * side;
        }

        Ray r(eye, direction);
        Intersection i = root->ray_intersect(r);

#ifdef NORMAL
        /*  Normal Shading */
        i.normal.normalize();
        img(x + width/2, height/2 - y - 1, 0) = i.normal[0];
        img(x + width/2, height/2 - y - 1, 1) = i.normal[1];
        img(x + width/2, height/2 - y - 1, 2) = i.normal[2];
        continue;
#endif

        if (i.hit) {
          PhongMaterial* m = dynamic_cast<PhongMaterial*>(i.node->get_material());
          // Lighting calculations
          double r = 0;
          double b = 0;
          double g = 0;
          // Ambient lighting
#ifdef AMBIENT
          r += ambient.R() * m->diffuse().R();
          g += ambient.G() * m->diffuse().G();
          b += ambient.B() * m->diffuse().B();
#endif

          for (std::list<Light*>::const_iterator I = lights.begin(); I != lights.end(); ++I) {
            // Get light vector
            Light* light = *I;
            Vector3D light_dir = light->position - i.pt;
            double dist = light_dir.length();
            light_dir.normalize();
            i.normal.normalize();

            // Check intersection
#ifdef SHADOWS
            Ray light_ray(i.pt, light_dir);
            Intersection light_inter = root->ray_intersect(light_ray);
            if (light_inter.hit) {
              //std::cout << i.node->get_name() << " in shadow of " << light_inter.node->get_name() << std::endl;
              continue;
            }
#endif

            // Diffuse
            double surf = i.normal.dot(light_dir);
#ifdef DIFFUSE
            if (surf > 0) {
              double attenuation = light->falloff[0] +
                                   light->falloff[1] * dist +
                                   light->falloff[2] * dist * dist;
              r += (light->colour.R() * m->diffuse().R() * surf) / attenuation;
              g += (light->colour.G() * m->diffuse().G() * surf) / attenuation;
              b += (light->colour.B() * m->diffuse().B() * surf) / attenuation;
            }
#endif

            // Specular
#ifdef SPECULAR
            Vector3D reflect = (-1 * light_dir) + 2 * (surf) * i.normal;
            reflect.normalize();
            double reflective = reflect.dot(-1 * norm_view);
            if (reflective > 0) {
              double spec = pow(reflective, m->shininess());
              r += light->colour.R() * spec * m->specular().R();
              g += light->colour.G() * spec * m->specular().G();
              b += light->colour.B() * spec * m->specular().B();
            }
#endif
          } // For each light

          r_sum += r;
          g_sum += g;
          b_sum += b;
        } else {
          // On no hit, pull colour from background
          r_sum += 0;//img(x + width/2, height/2 - y - 1, 0);
          g_sum += 0;//img(x + width/2, height/2 - y - 1, 0);
          b_sum += 0;//img(x + width/2, height/2 - y - 1, 0);
        } // if hit
      } // For each supersampled ray
      img(x + width/2, height/2 - y - 1, 0) = r_sum / ANTIALIAS;
      img(x + width/2, height/2 - y - 1, 1) = g_sum / ANTIALIAS;
      img(x + width/2, height/2 - y - 1, 2) = b_sum / ANTIALIAS;
    }  // for x
  }  // for y
  std::cerr << std::endl;

  // Save image
  img.savePng(filename);
}
