/*
 *
 */
package lsystem;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Vector;

public abstract class AbstractIteratedSystem implements IteratedSystem {
    protected int numIterations = 0;
    protected int maxNumIterations = 0;

    public AbstractIteratedSystem(int maxNumIterations) {
        this.maxNumIterations = maxNumIterations;
    }
    public Vector<Vector<Point2D.Double>> getLineSegments(Rectangle displayArea) {
        return new Vector<Vector<Point2D.Double>>();
    }

    public Vector<Point2D.Double> getLines(Rectangle displayArea) {
        return new Vector<Point2D.Double>();
    }

    public int getMaxNumIterations() {
        return this.maxNumIterations;
    }

    public String getName() {
        return "Abstract Iterated System";
    }

    public int getNumIterations() {
        return numIterations;
    }

    public int getNumParams() {
        return 0;
    }

    public int getParamMax(int paramNum) {
        return 0;
    }

    public int getParamMin(int paramNum) {
        return 0;
    }

    public String getParamName(int paramNum) {
        return "";
    }

    public int getParamValue() {
        return 0;
    }

    public Vector<Point2D.Double> getPoints(Rectangle displayArea) {
        return new Vector<Point2D.Double>();
    }

    public void iterate() {
        if (this.getNumIterations() >= this.getMaxNumIterations()) {
            return;
        }
        this.iterateSystem();
        this.numIterations++;
    }

    protected abstract void iterateSystem();
    
    public void iterateToStep(int stepNum) {
        if (stepNum >= this.getNumIterations()) {
            int diff = stepNum - this.getNumIterations();
            while (diff-- > 0) {
                this.iterate();
            }
        } else {
            this.reset();
            this.iterateToStep(stepNum);
        }
    }

    public void reset() {
        this.numIterations = 0;
    }

    public void setParamValue(int paramNum, int value) {
        ; // no-op
    }

    protected Vector<Vector<Point2D.Double> > scaleLineSegmentsToArea(Vector<Vector<Point2D.Double> > lines, Rectangle destArea)
    {
        if (lines.size() > 0) {
            double minX = 0;
            double minY = 0;
            double maxX = 0;
            double maxY = 0;
            boolean initialized = false;
        
            for (Vector<Point2D.Double> points : lines) {
                for (Point2D.Double p : points) {
                    if (!initialized) {
                        initialized = true;
                        minX = maxX = p.x;
                        minY = maxY = p.y;
                    } else {
                        minX = Math.min(minX, p.x);
                        maxX = Math.max(maxX, p.x);
                        minY = Math.min(minY, p.y);
                        maxY = Math.max(maxY, p.y);
                    }
                }
            }
            double width  = maxX - minX;
            double height = maxY - minY;
            Rectangle srcArea = new Rectangle((int)minX, (int)minY, (int)width, (int)height);

            Vector<Vector<Point2D.Double> > newLines = new Vector<Vector<Point2D.Double>>();
            for (Vector<Point2D.Double> points : lines) {
                newLines.add(this.scalePointsToArea(points, srcArea, destArea));
            }
            return newLines;
        }
        return lines;
    }

    protected Vector<Point2D.Double> scalePointsToArea(Vector<Point2D.Double> points, Rectangle srcArea, Rectangle destArea)
    {
        AffineTransform t = new AffineTransform();
        double scale = 1.0;
        
        if (srcArea.width > 0 && srcArea.height > 0 && destArea.width > 0 && destArea.height > 0) {
            scale = 1.0 * destArea.width / srcArea.width;
            scale = Math.min(scale, 1.0 * destArea.height / srcArea.height);
        }
        double newWidth = srcArea.width * scale;
        double newHeight = srcArea.height * scale;
        
        t.translate(destArea.x + (destArea.width - newWidth)/2, destArea.y + (destArea.height - newHeight)/2);
        t.scale(scale, scale);
        t.translate(srcArea.x * -1, srcArea.y * -1);
        return this.transformPoints(points, t);
    }

    protected Vector<Point2D.Double> scalePointsToArea(Vector<Point2D.Double> points, Rectangle destArea)
    {
        if (points.size() > 0) {
            double minX, minY, maxX, maxY;
        
            // init min/max values
            Point2D.Double firstPoint = points.elementAt(0);
            minX = maxX = firstPoint.x;
            minY = maxY = firstPoint.y;
        
            for (Point2D.Double p : points) {
                minX = Math.min(minX, p.x);
                maxX = Math.max(maxX, p.x);
                minY = Math.min(minY, p.y);
                maxY = Math.max(maxY, p.y);
            }   
            double width  = maxX - minX;
            double height = maxY - minY;
            return this.scalePointsToArea(points, new Rectangle((int)minX, (int)minY, (int)width, (int)height), destArea);
        }
        return points;
    }

    protected Vector<Point2D.Double> transformPoints(Vector<Point2D.Double> points, AffineTransform t)
    {
        Vector<Point2D.Double> newPoints = new Vector<Point2D.Double>();
        for (Point2D.Double p : points) {
            Point2D.Double newPoint = new Point2D.Double();
            t.transform(p, newPoint);
            newPoints.add(newPoint);
        }
        return newPoints;
    }
}
