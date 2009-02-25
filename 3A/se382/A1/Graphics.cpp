// Author: Michael Terry

#include "Graphics.h"
#include "XWindow.h"

#include <string.h>

namespace cs349
{

Graphics::Graphics(Display* display, Window window, GC gc)
{
  this->display   = display;
  this->window    = window;
  this->gc        = gc;

  this->function  = GXcopy;

  // Set clip to current size of window
  XWindowAttributes attr;
  XGetWindowAttributes(this->display, this->window, &attr);
  this->clipRect.x = 0;
  this->clipRect.y = 0;
  this->clipRect.width = attr.width;
  this->clipRect.height = attr.height;
}

Graphics::~Graphics()
{
}

void Graphics::DrawLine(const Point & p1, const Point & p2)
{
  this->PrepareGraphicsContext();
  Point t1 = this->transform.Transform(p1);
  Point t2 = this->transform.Transform(p2);
  XDrawLine(this->display, this->window, this->gc, (int)t1.x, (int)t1.y, (int)t2.x, (int)t2.y);
}

void Graphics::DrawLineSegments(const vector<vector<Point> > & lines)
{
  for (vector<vector<Point> >::const_iterator iter=lines.begin(); iter != lines.end(); iter++) {
    this->DrawPolygon(*iter);
  }
}

void Graphics::DrawPoint(const Point & p)
{
  this->PrepareGraphicsContext();
  Point newPoint = this->transform.Transform(p);
  XDrawPoint(this->display, this->window, this->gc, (int)newPoint.x, (int)newPoint.y);
}

void Graphics::DrawPoints(const vector<Point> & points)
{
  this->PrepareGraphicsContext();
  XPoint* xPoints = this->GetTransformedXPoints(points);
  XDrawPoints(this->display, this->window, this->gc, xPoints, points.size(), CoordModeOrigin);
  delete xPoints;
}

void Graphics::DrawPolygon(const vector<Point> & vertices)
{
  this->PrepareGraphicsContext();
  XPoint* xPoints = this->GetTransformedXPoints(vertices);
  XDrawLines(this->display, this->window, this->gc, xPoints, vertices.size(), CoordModeOrigin);
  delete xPoints;
}

void Graphics::DrawRect(const Rectangle & r)
{
  Point p1 = Point(r.x, r.y);
  Point p2 = Point(r.x+r.width, r.y);
  Point p3 = Point(r.x+r.width, r.y+r.height);
  Point p4 = Point(r.x, r.y+r.height);
  vector<Point> points;
  points.push_back(p1);
  points.push_back(p2);
  points.push_back(p3);
  points.push_back(p4);
  points.push_back(p1);
  this->DrawPolygon(points);
}

void Graphics::DrawText(const Point & p, const char* text)
{
  if (!text) {
    return;
  }
  this->PrepareGraphicsContext();
  XTextItem xTextItem;
  xTextItem.chars  = const_cast<char*>(text);
  xTextItem.nchars = strlen(text);
  xTextItem.font   = None;
  xTextItem.delta  = 0;
  Point newPoint = this->transform.Transform(p);
  XDrawText(this->display, this->window, this->gc, (int)newPoint.x, (int)newPoint.y, &xTextItem, 1);
}

void Graphics::FillPolygon(const vector<Point> & vertices)
{
  this->PrepareGraphicsContext();
  XPoint* xPoints = this->GetTransformedXPoints(vertices);
  XFillPolygon(this->display, this->window, this->gc, xPoints, vertices.size(), Complex, CoordModeOrigin);
  delete xPoints;
}

void Graphics::FillRect(const Rectangle & r)
{
  Point p1 = Point(r.x, r.y);
  Point p2 = Point(r.x+r.width, r.y);
  Point p3 = Point(r.x+r.width, r.y+r.height);
  Point p4 = Point(r.x, r.y+r.height);
  vector<Point> points;
  points.push_back(p1);
  points.push_back(p2);
  points.push_back(p3);
  points.push_back(p4);
  points.push_back(p1);
  this->FillPolygon(points);
}

unsigned long Graphics::GetBackgroundColor() const
{
  return this->backgroundColor;
}

Rectangle Graphics::GetClipRect()
{
  // Transform from window coordinates to current coordinates
  AffineTransform t = this->transform.GetInverse();
  Point p = t.Transform(this->clipRect.GetTopLeft());
  return Rectangle(p.x, p.y, this->clipRect.width, this->clipRect.height);
}

unsigned long Graphics::GetForegroundColor() const
{
  return this->foregroundColor;
}

int Graphics::GetFunction() const
{
  return this->function;
}

GC Graphics::GetGC()
{
  return this->gc;
}

AffineTransform Graphics::GetTransform() const
{
  return this->transform;
}

XPoint* Graphics::GetTransformedXPoints(const vector<Point> & points)
{
  XPoint* newPoints = new XPoint[points.size()];
  for (unsigned int i = 0; i < points.size(); i++) {
    Point p = this->transform.Transform(points[i]);
    newPoints[i].x = (int)p.x;
    newPoints[i].y = (int)p.y;
  }
  return newPoints;
}

void Graphics::PrepareGraphicsContext()
{
  XRectangle clip;
  XSetBackground(this->display, this->gc, this->backgroundColor);
  XSetForeground(this->display, this->gc, this->foregroundColor);
  XSetFunction(this->display, this->gc, this->function);
  clip.x = (int)this->clipRect.x;
  clip.y = (int)this->clipRect.y;
  clip.width = (int)this->clipRect.width;
  clip.height = (int)this->clipRect.height;
  XSetClipRectangles(this->display, this->gc, 0, 0, &clip, 1, Unsorted);
}

void Graphics::SetBackgroundColor(unsigned long color)
{
  this->backgroundColor = color;
}

void Graphics::SetClipRect(const Rectangle & r)
{
  // Transform into window coordinates
  Point p = this->transform.Transform(r.GetTopLeft());
  this->clipRect = Rectangle(p.x, p.y, r.width, r.height);
}

void Graphics::SetForegroundColor(unsigned long color)
{
  this->foregroundColor = color;
}

void Graphics::SetFunction(int function)
{
  this->function = function;
}

void Graphics::SetTransform(const AffineTransform & t)
{
  this->transform = t;
}

void Graphics::Invert() {
  unsigned long temp = this->foregroundColor;
  SetForegroundColor(this->backgroundColor);
  SetBackgroundColor(temp);
}

}
