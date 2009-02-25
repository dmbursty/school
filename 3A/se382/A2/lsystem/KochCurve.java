/*
 *
 */
package lsystem;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Vector;


public class KochCurve extends AbstractIteratedSystem {

    private String curString = null;
    private TurtleGraphics turtle = new TurtleGraphics();
    private Vector<Point2D.Double> points = new Vector<Point2D.Double>();
    
    public KochCurve() {
        super(7);
        this.reset();
    }
    
    public String getName() {
        return "Koch Curve";
    }
    
    public void reset() {
        super.reset();
        curString = "F";
        turtle.setLocation(new Point2D.Double(0,0));
        turtle.pointRight();
        points.clear();
        points.add(turtle.getLocation());
    }
    
    protected void iterateSystem() {
        // Algorithm source: http://en.wikipedia.org/wiki/L-system

        StringBuffer newString = new StringBuffer();

        this.turtle.setLocation(new Point2D.Double(0,0));
        this.turtle.pointRight();
        this.points.clear();
        this.points.add(this.turtle.getLocation());

        for (int i = 0; i < curString.length(); i++) {
            switch (curString.charAt(i)) {
                case 'F':
                    this.points.add(this.turtle.moveForward());
                    newString.append("F+F-F-F+F");
                    break;
                case '-':
                    this.turtle.turn(90);
                    newString.append(curString.charAt(i));
                    break;
                case '+':
                    this.turtle.turn(-90);
                    newString.append(curString.charAt(i));
                    break;
            }
        }
        this.curString = newString.toString();
    }

    public Vector<Double> getLines(Rectangle displayArea) {
        return this.scalePointsToArea(points, displayArea);
    }
}
