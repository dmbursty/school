/*
 *
 */
package lsystem;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.Vector;

public interface IteratedSystem {
  String getName();

  int getMaxNumIterations();
  int getNumIterations();

  void reset();
  void iterate();
  void iterateToStep(int stepNum);

  Vector<Point2D.Double> getPoints(Rectangle displayArea);
  Vector<Vector<Point2D.Double>> getLineSegments(Rectangle displayArea);
  Vector<Point2D.Double> getLines(Rectangle displayArea);

  // These are not required for the assignment, but are of use for an extension to the basic requirements of the assignment
  int getNumParams();
  int getParamValue();
  int getParamMin(int paramNum);
  int getParamMax(int paramNum);
  String getParamName(int paramNum);
  void setParamValue(int paramNum, int value);
}
