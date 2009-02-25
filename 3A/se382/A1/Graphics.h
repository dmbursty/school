// Author: Michael Terry
#ifndef GRAPHICS_H_
#define GRAPHICS_H_

#include <X11/Xlib.h>
#include <vector>

using namespace std;

namespace cs349
{
  class Graphics;
  class XWindow;
}

#include "AffineTransform.h"
#include "Point.h"
#include "Rectangle.h"

namespace cs349
{

/*
 * This class encapsulates X lib's Graphics Context (GC) and provides the
 * means by which components draw themselves on windows.
 *
 * You can add functionality to this class (for example, there are no
 * methods provided to draw arcs, ellipses, or circles).
 *
 * TODO: More than likely, you will want to add functionality to change
 *       fonts and determine the bounds of a string when drawn to the
 *       screen
 */
class Graphics
{
 private:
  Display* display;
  Window   window;
  GC     gc;
  Rectangle clipRect; // Always stored in window coordinates. This requires care in setting/getting
  AffineTransform transform;
  unsigned long backgroundColor;
  unsigned long foregroundColor;
  int           function;

  friend class XWindow;
  Graphics(Display* display, Window window, GC gc);
  void PrepareGraphicsContext();
  XPoint* GetTransformedXPoints(const vector<Point> & points);

 public:
  virtual ~Graphics();


  virtual void       DrawLine(const Point & p1, const Point & p2);
  virtual void       DrawLineSegments(const vector<vector<Point> > & lines);
  virtual void       DrawPoint(const Point & p);
  virtual void       DrawPoints(const vector<Point> & points);
  virtual void       DrawPolygon(const vector<Point> & vertices);
  virtual void       DrawRect(const Rectangle & r);
  virtual void       DrawText(const Point & p, const char* text);
  virtual void       FillPolygon(const vector<Point> & vertices);
  virtual void       FillRect(const Rectangle & r);


  virtual GC         GetGC(); // Returns X lib's graphics context

  /*
   * The background/foreground colors that will be used for drawing
   */
  virtual unsigned long  GetBackgroundColor() const;
  virtual unsigned long  GetForegroundColor() const;
  virtual void       SetBackgroundColor(unsigned long color);
  virtual void       SetForegroundColor(unsigned long color);


  /*
   * Sets the drawing mode (e.g., copy, xor, etc.). See X documentation for more info.
   */
  virtual int        GetFunction() const;
  virtual void       SetFunction(int function);


  virtual Rectangle     GetClipRect();
  virtual void       SetClipRect(const Rectangle & r);


  /*
   * AffineTransforms can be used to alter how points, lines, rectangles, and polygons
   * are drawn on the screen, by scaling, rotating, and translating them.
   */
  virtual AffineTransform GetTransform() const;
  virtual void       SetTransform(const AffineTransform & t);
  virtual void Invert();
};

}

#endif /*GRAPHICS_H_*/
