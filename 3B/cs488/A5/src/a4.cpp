#include <pthread.h>
#include "a4.hpp"
#include "image.hpp"
#include "ray.hpp"
#include "queue.hpp"
#include "config.hpp"

#define DEG2RAD 0.0174532925
#define NUM_THREADS 4

// Lighting options
// Variables
unsigned int BUMPMAP = 100;
unsigned int ANTIALIAS = 10;
unsigned int RECURSE = 10;
unsigned int GLOSSY = 1;
// Regular
unsigned int AMBIENT = 1;
unsigned int DIFFUSE = 1;
unsigned int TEXTURE = 1;
unsigned int SPECULAR = 1;
unsigned int SHADOWS = 1;
unsigned int FRESNEL = 1;
// Special rendering
unsigned int NORMAL = 0;
unsigned int BOUNDS = 0;

// Forward declare
void* threadRender(void* arg);
Pixel ray_trace_recurse(int x, int y, Ray ray, RenderConfig* data,
                        double index = 1, double weight = 1,
                        unsigned int recursion = 1);
Pixel ray_trace(int x, int y, Ray r, RenderConfig* data, double index);

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

  // Generate Bounding Volumes
  root->generateBounds();

  // Make render_data that the threads use
  Image img(width, height, 3);
  Queue queue;

  // Colour Background
  Image bg(width, height, 3);
  for (int x = 0; x < width; x++) {
    for (int y = 0; y < height; y++) {
      double xx = ( 1 + x / (double)width );
      bg(x, y, 0) = 0.15 * xx;
      bg(x, y, 1) = 0.15 * xx;
      bg(x, y, 2) = 0.35 * xx;

      //double yy = y / (double)height;
      //bg(x, y, 0) = 1 - xx - yy;
      //bg(width - x - 1, y, 2) = 1 - xx - yy;
    }
  }

  double aspect = (double)height / width;
  double x_inc = tan((fov/2) * DEG2RAD) / (width / 2.0);
  double y_inc = tan(((fov * aspect)/2) * DEG2RAD) / (height / 2.0);

  Vector3D norm_view = view;
  Vector3D norm_up = up;
  Vector3D side = view.cross(up);

  norm_view.normalize();
  norm_up.normalize();
  side.normalize();

  // Push work onto queue
  for (int y = -height / 2; y < height / 2; y++) {
    queue.push(y);
  }

  // Populate config struct
  RenderConfig* config = new RenderConfig(ambient, lights, queue);
  config->height = height;
  config->width = width;
  config->img = &img;
  config->bg = &bg;
  config->eye = eye;
  config->view = norm_view;
  config->up = norm_up;
  config->side = side;
  config->root = root;
  config->x_inc = x_inc;
  config->y_inc = y_inc;

  // Start threads
  pthread_t threads[NUM_THREADS];
  for (int t = 0; t < NUM_THREADS; t++) {
    pthread_create(&threads[t], NULL, threadRender, (void*) config);
  }

  // Wait for threads to finish
  for (int t = 0; t < NUM_THREADS; t++) {
    void* status;
    pthread_join(threads[t], &status);
  }

  std::cerr << std::endl;

  // Save image
  img.savePng(filename);
  pthread_exit(NULL);
}

void* threadRender(void* arg) {
  RenderConfig* data = (RenderConfig*)arg;
  while (true) {
    data->queue.acquire();
    if (data->queue.size() == 0) break;
    // Get y value from work queue
    int y = data->queue.pop();
    int percent = 100 * (((double)(y+1) / data->height) + 0.5);
    // Print current progress
    std::cerr << percent << "%\r";
    data->queue.release();

    // Rendering code
    for (int x = -data->width / 2; x < data->width / 2; x++) {
      double r_sum = 0;
      double g_sum = 0;
      double b_sum = 0;

      for (unsigned int supersample = 0; supersample < ANTIALIAS; supersample++) {
        // Cast ray
        double x_diff, y_diff;
        if (supersample == 0) {
          x_diff = 0;
          y_diff = 0;
        } else {
          x_diff = ((rand() % 1001) / 1000.0) - 0.5;
          y_diff = ((rand() % 1001) / 1000.0) - 0.5;
        }

        Vector3D direction;
        direction = data->view +
                    data->y_inc * (y + y_diff) * data->up +
                    data->x_inc * (x + x_diff) * data->side;

        Ray r(data->eye, direction);
        Pixel pix = ray_trace_recurse(x, y, r, data);
        if (pix.empty) {
          // If we didn't hit, pull from background
          r_sum += (*data->bg)(x + data->width/2, data->height/2 - y - 1, 0);
          g_sum += (*data->bg)(x + data->width/2, data->height/2 - y - 1, 1);
          b_sum += (*data->bg)(x + data->width/2, data->height/2 - y - 1, 2);
        } else {
          r_sum += pix.r;
          g_sum += pix.g;
          b_sum += pix.b;
        }

      } // For each supersampled ray

      (*data->img)(x + data->width/2, data->height/2 - y - 1, 0) = r_sum / ANTIALIAS;
      (*data->img)(x + data->width/2, data->height/2 - y - 1, 1) = g_sum / ANTIALIAS;
      (*data->img)(x + data->width/2, data->height/2 - y - 1, 2) = b_sum / ANTIALIAS;
    }  // for x
  }
  data->queue.release();
  pthread_exit(NULL);
}

Pixel ray_trace_recurse(int x, int y, Ray ray, RenderConfig* data, double index, double weight, unsigned int recursion) {
  Pixel ret;

  if (recursion >= RECURSE) return ret;

  Pixel p = ray_trace(x, y, ray, data, index);

  if (p.empty) {
    ret.empty = true;
    return ret;
  }

  if (p.refract > 0.1) {
    // Recurse for refraction
    Pixel refracted = ray_trace_recurse(x, y, p.refract_ray, data, p.index, 1, recursion + 1);
    ret.r += refracted.r * p.refract;
    ret.g += refracted.g * p.refract;
    ret.b += refracted.b * p.refract;
  } else {
    // Pull colour from object we hit
    ret.r += p.r;
    ret.g += p.g;
    ret.b += p.b;
  }

  if (p.reflect > 0.1) {
    // If glossy, do glossy reflections
    double ref_r = 0;
    double ref_g = 0;
    double ref_b = 0;
    if (p.gloss > 0) {
      for (unsigned int i = 0; i < GLOSSY; i++) {
        // Jitter reflection ray for glossy reflections
        //double c1 = -1 * ray.dir.dot(p.normal);
        // Jitter the angle here
        //c1 = c1 + (p.gloss / 2) * ((rand() % 2000) / 1000.0 - 1);
        //Vector3D jitter_dir = ray.dir + (2 * c1 * p.normal);
        //Ray jittered_ray(p.reflect_ray.eye, jitter_dir);
        Ray jittered_ray = p.reflect_ray.jitterCone(p.gloss);

// Recurse to get reflection
        Pixel gloss_ref = ray_trace_recurse(x, y, jittered_ray, data, index, p.reflect, recursion + 1);
        ref_r += gloss_ref.r;
        ref_g += gloss_ref.g;
        ref_b += gloss_ref.b;
      }
      ret.r += ref_r / (GLOSSY + 1);
      ret.g += ref_g / (GLOSSY + 1);
      ret.b += ref_b / (GLOSSY + 1);
    } else {
      // Recurse to get reflection
      Pixel reflected = ray_trace_recurse(x, y, p.reflect_ray, data, index, p.reflect, recursion + 1);
      ret.r += reflected.r;
      ret.g += reflected.g;
      ret.b += reflected.b;
    }
  }

  ret.r *= weight;
  ret.b *= weight;
  ret.g *= weight;
  return ret;
}

Pixel ray_trace(int x, int y, Ray ray, RenderConfig* data, double index) {
        Pixel ret;

        Intersections is = data->root->ray_intersect(ray);
        is.sort(ray.eye);
        Intersection i;

        if (NORMAL) {
          /*  Normal Shading */
          i.normal.normalize();
          ret.r = i.normal[0];
          ret.g = i.normal[1];
          ret.b = i.normal[2];
          return ret;
        }

        if (is.size() > 0) {
          i = is.inter[0];
          Material* m = i.node->get_material();
          // Lighting calculations
          double r = 0;
          double b = 0;
          double g = 0;

          // Check if we need to reflect
          // Possible FIXME: Nothing can be both reflective and refractive
          // because fresnels overwrite the regular reflective strength
          double reflect = m->reflect();
          if (reflect > 0) {
            ret.reflect = m->reflect();
            // Calculate reflect ray
            Vector3D v(ray.dir);
            Vector3D n(i.normal);
            v.normalize();
            n.normalize();
            double c1 = -1 * v.dot(n);
            Vector3D reflect_dir = ray.dir + (2 * c1 * n);
            Ray reflect_ray(i.pt, reflect_dir);
            ret.reflect_ray = reflect_ray;
            ret.gloss = m->gloss();
            // Give back the normal because we may need a different reflection
            // ray for glossy reflections;
            ret.normal = n;
          }

          // Check if we need to refract
          if (m->refract() > 0) {
            Vector3D V(ray.dir);
            Vector3D N(i.normal);
            double n, n1, n2;
            if (index == m->refract()) {
              n1 = index;
              n2 = 1;
              N = -1 * N;
            } else {
              n1 = index;
              n2 = m->refract();
            }
            ret.index = n2;
            n = n1 / n2;
            V.normalize();
            N.normalize();
            double c1 = -1 * V.dot(N);

            // Reflection ray
            Vector3D reflect_dir = ray.dir + (2 * c1 * N);
            Ray reflect_ray(i.pt, reflect_dir);
            ret.reflect_ray = reflect_ray;
            ret.normal = N;

            double c2 = 1 - n * n * (1 - c1 * c1);
            // Total internal reflection
            if ( c2 < 0 ) {
              ret.reflect = 1;
              return ret;
            }
            c2 = sqrt(c2);

            // Refraction ray
            Vector3D refract_dir = (n * V) + (n * c1 - c2) * N;
            Ray refract_ray(i.pt, refract_dir);
            ret.refract_ray = refract_ray;

            // Fresnel
            double nd = -c1;
            double nt = N.dot(refract_dir);
            double r1 = (n2 * nd - n1 * nt) / (n2 * nd + n1 * nt);
            double r2 = (n1 * nd - n2 * nt) / (n1 * nd + n2 * nt);
            ret.reflect = (r1 * r1 + r2 * r2) / 2;
            ret.refract = 1 - ret.reflect;
            ret.gloss = m->gloss();

            return ret;
          }

          // Get Colour info
          Colour c(0);
          if (m->texture() == NULL) {
            if (DIFFUSE) {
              c = m->diffuse();
            }
          } else {
            if (TEXTURE) {
              int map_x = m->texture()->width() * i.map_x;
              int map_y = m->texture()->height() * i.map_y;
              c.set((*m->texture())(map_x, map_y, 0),
                    (*m->texture())(map_x, map_y, 1),
                    (*m->texture())(map_x, map_y, 2));
            }
          }

          // Ambient lighting
          if (AMBIENT) {
            r += data->ambient.R() * c.R();
            g += data->ambient.G() * c.G();
            b += data->ambient.B() * c.B();
          }

          for (std::list<Light*>::const_iterator I = data->lights.begin(); I != data->lights.end(); ++I) {
            // Get light vector
            Light* light = *I;
            Vector3D light_dir = light->position - i.pt;
            double dist = light_dir.length();
            light_dir.normalize();
            i.normal.normalize();

            // Check intersection
            if (SHADOWS) {
              Ray light_ray(i.pt, light_dir);
              Intersections light_inter = data->root->ray_intersect(light_ray);
              if (light_inter.size() > 0) {
                //std::cout << i.node->get_name() << " in shadow of " << light_inter.node->get_name() << std::endl;
                continue;
              }
            }

            // Diffuse
            double surf = i.normal.dot(light_dir);
            if (surf > 0) {
              double attenuation = light->falloff[0] +
                                   light->falloff[1] * dist +
                                   light->falloff[2] * dist * dist;
              r += (light->colour.R() * c.R() * surf) / attenuation;
              g += (light->colour.G() * c.G() * surf) / attenuation;
              b += (light->colour.B() * c.B() * surf) / attenuation;
            }

            // Specular
            if (SPECULAR) {
              Vector3D reflect = (-1 * light_dir) + 2 * (surf) * i.normal;
              reflect.normalize();
              double reflective = reflect.dot(-1 * data->view);
              if (reflective > 0) {
                double spec = pow(reflective, m->shininess());
                r += light->colour.R() * spec * m->specular().R();
                g += light->colour.G() * spec * m->specular().G();
                b += light->colour.B() * spec * m->specular().B();
              }
            }
          } // For each light

          ret.r += r;
          ret.g += g;
          ret.b += b;
        } else {
          // Didn't hit anything
          //ret.empty = true;
          // If we didn't hit, pull from background
          ret.r += (*data->bg)(x + data->width/2, data->height/2 - y - 1, 0);
          ret.g += (*data->bg)(x + data->width/2, data->height/2 - y - 1, 1);
          ret.b += (*data->bg)(x + data->width/2, data->height/2 - y - 1, 2);
        } // if hit
  return ret;
}
