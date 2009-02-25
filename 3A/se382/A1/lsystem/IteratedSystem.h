// Author: Michael Terry

#ifndef ITERATEDSYSTEM_H_
#define ITERATEDSYSTEM_H_

#include <vector>
#include <string>
using namespace std;

#include "../AffineTransform.h"
#include "../Point.h"
#include "../Rectangle.h"

namespace cs349
{

/*
 * This is the interface through which you'll interact to get points, lines, and line segments to
 * draw on the screen. Most methods should be self-explanatory, but to emphasize some finer points:
 * - Iterate() causes the system to step forward once. This causes the system to "grow"
 * - IterateToStep(int) causes the system to iterate to the specified step number, from the initial state
 * - Reset() resets the system to its initial state
 * - GetPoints/Lines/LineSegments(Rectangle) return the points and lines representing the current state of the system.
 *   You pass in a rectangle representing the area in which you will display the system; it will automatically transform
 *   all of the points and lines to display precisely within the area specified. All you'll need to do is pass the points
 *   and lines on to the appropriate Graphics methods
 * - The system does not specify whether the system displays itself in points, lines, or line segments -- call all three
 *   methods, passing the results on to the appropriate Graphics methods
 * - The system can only iterate a limited number of times, after which it becomes too large and complex to efficiently and
 *   realistically display. Check the GetMaxNumIterations() and GetNumIterations() function to retrieve the maximum number
 *   of iterations for that particular system, and the current number of iterations
 */
class IteratedSystem
{
 private:
  int maxNumIterations;

protected:
  int curNumIterations;

  IteratedSystem(int maxNumIterations);

  virtual vector<Point>       TransformPoints(const vector<Point> & points, const AffineTransform & t) const;
  virtual vector<Point>       ScalePointsToArea(const vector<Point> & points, const Rectangle & destArea) const;
  virtual vector<vector<Point> >  ScaleLineSegmentsToArea(const vector<vector<Point> > & lines, const Rectangle & destArea) const;
  virtual vector<Point>       ScalePointsToArea(const vector<Point> & points, const Rectangle & srcArea, const Rectangle & destArea) const;

  // Subclasses override this
  virtual void           IterateSystem() = 0;

 public:
  virtual ~IteratedSystem();

  virtual string       GetName() const = 0;

  virtual int        GetMaxNumIterations() const;
  virtual int        GetNumIterations() const;

  virtual void       Reset();
  virtual void       Iterate();
  virtual bool       IterateIfPossible();
  virtual void       IterateToStep(int stepNum);

  virtual vector<Point>        GetPoints(const Rectangle & displayArea) const;
  virtual vector<vector<Point> >     GetLineSegments(const Rectangle & displayArea) const;
  virtual vector<Point>        GetLines(const Rectangle & displayArea) const;

  // These are not required for the assignment, but are of use for an extension to the basic requirements of the assignment
  virtual int       GetNumParams() const;
  virtual int       GetParamValue() const;
  virtual pair<int, int>  GetParamMinMax(int paramNum) const;
  virtual string       GetParamName(int paramNum) const;
  virtual void       SetParamValue(int paramNum, int value) = 0;

};

}

#endif /*ITERATEDSYSTEM_H_*/
