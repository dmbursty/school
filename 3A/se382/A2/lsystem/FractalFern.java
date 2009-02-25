/*
 *
 */
package lsystem;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Vector;

public class FractalFern extends AbstractIteratedSystem {

    private Point2D.Double lastPoint = new Point2D.Double();
    private Vector<Point2D.Double> points = new Vector<Point2D.Double>();
    
    public FractalFern() {
        super(100);
    }
    
    public String getName() {
        return "Fractal Fern";
    }
    
    public void reset() {
        super.reset();
        this.points.clear();
        this.lastPoint = new Point2D.Double();
    }

    protected void iterateSystem() {
        // Code derived from http://en.wikipedia.org/wiki/Iterated_function_system
        final int NUM_ITERATIONS = 100;
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            int value = (int)(Math.random() * 100);
            Point2D.Double p = new Point2D.Double();
            if (value <= 1) {
                p.x = 0;
                p.y = 0.16 * this.lastPoint.y;
            } else if (value <= 8) {
                p.x = 0.2 * this.lastPoint.x - 0.26 * this.lastPoint.y;
                p.y = 0.23 * this.lastPoint.x  + 0.22 * this.lastPoint.y + 1.6;
            } else if (value <= 15) {
                p.x = -0.15 * this.lastPoint.x + 0.28 * this.lastPoint.y;
                p.y = 0.26 * this.lastPoint.x + 0.24 * this.lastPoint.y + 0.44;
            } else {
                p.x = 0.85 * this.lastPoint.x + 0.04 * this.lastPoint.y;
                p.y = -0.04 * this.lastPoint.x + 0.85 * this.lastPoint.y + 1.6;
            }
            this.points.add(p);
            this.lastPoint = p;
        }

    }

    public Vector<Double> getPoints(Rectangle displayArea) {
        // From article ref'ed above, all points will fall within this region
        Rectangle srcArea = new Rectangle(-3, 0, 6, 10);
        return this.scalePointsToArea(this.points, srcArea, displayArea);
    }
}
