#include "IteratedSystem.h"

namespace cs349
{

IteratedSystem::IteratedSystem(int maxNumIterations)
{
  this->maxNumIterations = maxNumIterations;
  this->curNumIterations = 0;
}

IteratedSystem::~IteratedSystem()
{
}

vector<Point> IteratedSystem::GetPoints(const Rectangle & displayArea) const
{
  return vector<Point>();
}

vector<vector<Point> > IteratedSystem::GetLineSegments(const Rectangle & displayArea) const
{
  return vector<vector<Point> >();
}

vector<Point> IteratedSystem::GetLines(const Rectangle & displayArea) const
{
  return vector<Point>();
}

int IteratedSystem::GetMaxNumIterations() const
{
  return this->maxNumIterations;
}

int IteratedSystem::GetNumIterations() const
{
  return this->curNumIterations;
}

int IteratedSystem::GetNumParams() const
{
  return 0;
}

int IteratedSystem::GetParamValue() const
{
  return 0;
}

pair<int, int>  IteratedSystem::GetParamMinMax(int paramNum) const
{
  return pair<int,int>(0,0);
}

string IteratedSystem::GetParamName(int paramNum) const
{
  return string("");
}

void IteratedSystem::Iterate()
{
  if (this->GetNumIterations() >= this->GetMaxNumIterations()) {
    return;
  }
  this->IterateSystem();
  this->curNumIterations++;
}

bool IteratedSystem::IterateIfPossible()
{
  if (this->GetNumIterations() >= this->GetMaxNumIterations()) {
    return false;
  }
  this->IterateSystem();
  this->curNumIterations++;
  return true;
}

void IteratedSystem::IterateToStep(int stepNum)
{
  if (stepNum >= this->GetNumIterations()) {
    int diff = stepNum - this->GetNumIterations();
    while (diff--) {
      this->Iterate();
    }
  } else {
    this->Reset();
    this->IterateToStep(stepNum);
  }
}

void IteratedSystem::Reset() {
  this->curNumIterations = 0;
}

vector<vector<Point> > IteratedSystem::ScaleLineSegmentsToArea(const vector<vector<Point> > & lines, const Rectangle & destArea) const
{
  if (lines.size() > 0) {
    double minX, minY, maxX, maxY;
    Point p;
    bool initialized = false;

    for (vector<vector<Point> >::const_iterator line_iter=lines.begin(); line_iter != lines.end(); line_iter++) {
      const vector<Point> & these_lines = (*line_iter);
      for (vector<Point>::const_iterator point_iter=these_lines.begin(); point_iter != these_lines.end(); point_iter++) {
        p = (*point_iter);
        if (!initialized) {
          initialized = true;
          minX = maxX = p.x;
          minY = maxY = p.y;
        } else {
          minX = min(minX, p.x);
          maxX = max(maxX, p.x);
          minY = min(minY, p.y);
          maxY = max(maxY, p.y);
        }
      }
    }
    double width  = maxX - minX;
    double height = maxY - minY;
    Rectangle srcArea(minX, minY, width, height);

    vector<vector<Point> > newLines;
    for (vector<vector<Point> >::const_iterator line_iter=lines.begin(); line_iter != lines.end(); line_iter++) {
      const vector<Point> & these_lines = (*line_iter);
      newLines.push_back(this->ScalePointsToArea(these_lines, srcArea, destArea));
    }
    return newLines;
  }
  return lines;
}

vector<Point> IteratedSystem::ScalePointsToArea(const vector<Point> & points, const Rectangle & srcArea, const Rectangle & destArea) const
{
  AffineTransform t;
  double scale = 1.0;

  if (srcArea.width > 0 && srcArea.height > 0 && destArea.width > 0 && destArea.height > 0) {
    scale = destArea.width / srcArea.width;
    scale = min(scale, destArea.height / srcArea.height);
  }
  double newWidth = srcArea.width * scale;
  double newHeight = srcArea.height * scale;

//  t.Translate(destArea.width / 2, destArea.height / 2);
//  t.RotateInDegrees(180);
//  t.Translate(destArea.width / -2, destArea.height / -2);
  t.Translate(destArea.x + (destArea.width - newWidth)/2, destArea.y + (destArea.height - newHeight)/2);
  t.Scale(scale, scale);
  t.Translate(srcArea.x * -1, srcArea.y * -1);
  return this->TransformPoints(points, t);
}

vector<Point> IteratedSystem::ScalePointsToArea(const vector<Point> & points, const Rectangle & destArea) const
{
  if (points.size() > 0) {
    double minX, minY, maxX, maxY;

    // init min/max values
    Point p = points.front();
    minX = maxX = p.x;
    minY = maxY = p.y;

    for (vector<Point>::const_iterator iter=points.begin(); iter != points.end(); iter++) {
      p = (*iter);
      minX = min(minX, p.x);
      maxX = max(maxX, p.x);
      minY = min(minY, p.y);
      maxY = max(maxY, p.y);
    }
    double width  = maxX - minX;
    double height = maxY - minY;
    return this->ScalePointsToArea(points, Rectangle(minX, minY, width, height), destArea);
  }
  return points;
}

vector<Point> IteratedSystem::TransformPoints(const vector<Point> & points, const AffineTransform & t) const
{
  vector<Point> newPoints;
  for (vector<Point>::const_iterator iter=points.begin(); iter != points.end(); iter++) {
    newPoints.push_back(t.Transform(*iter));
  }
  return newPoints;
}


}
