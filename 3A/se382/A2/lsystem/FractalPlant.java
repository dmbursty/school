/*
 *
 */
package lsystem;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Vector;

public class FractalPlant extends AbstractIteratedSystem {

    private String curString = null;
    private TurtleGraphics turtle = new TurtleGraphics();
    private Vector<Vector<Point2D.Double>> lines = new Vector<Vector<Point2D.Double>>();
    private int angleParam;

    public FractalPlant() {
        super(7);
        angleParam = 25;
        this.reset();
    }
    
    public String getName() {
        return "Fractal Plant";
    }
    
    public void reset() {
        super.reset();
        curString = "X";
        turtle.setLocation(new Point2D.Double(0,0));
        turtle.pointUp();
        turtle.turn(20);
        lines.clear();
    }

    protected void iterateSystem() {
        // Algorithm source: http://en.wikipedia.org/wiki/L-system

        StringBuffer newString = new StringBuffer();

        this.turtle.setLocation(new Point2D.Double(0,0));
        this.turtle.pointUp();
        this.turtle.turn(20);
        this.lines.clear();
        
        Vector<Point2D.Double> positionStack = new Vector<Point2D.Double>();
        Vector<Point2D.Double> angleStack = new Vector<Point2D.Double>();

        Vector<Point2D.Double> thisSet = new Vector<Point2D.Double>();

        thisSet.add(new Point2D.Double(0,0));
        for (int i = 0; i < curString.length(); i++) {
            char c = curString.charAt(i);
            if (c == 'F') {
                thisSet.add(this.turtle.moveForward());
                newString.append("FF");
            } else if (c == 'X') {
                newString.append("F-[[X]+X]+F[+FX]-X");
            } else if (c == '[') {
                positionStack.insertElementAt(this.turtle.getLocation(), 0);
                angleStack.insertElementAt(this.turtle.getAngleVector(), 0);
                newString.append(c);
            } else if (c == ']') {
                this.turtle.setLocation(positionStack.elementAt(0));
                this.turtle.setAngleVector(angleStack.elementAt(0));
                positionStack.removeElementAt(0);
                angleStack.removeElementAt(0);
                newString.append(c);
                this.lines.add(thisSet);
                thisSet = new Vector<Point2D.Double>();
                thisSet.add(this.turtle.getLocation());
            } else if (c == '-') {
                this.turtle.turn(-1*this.angleParam);
                newString.append(c);
            } else if (c == '+') {
                this.turtle.turn(this.angleParam);
                newString.append(c);
            }
        }
        this.curString = newString.toString();
    }

    public Vector<Vector<Double>> getLineSegments(Rectangle displayArea) {
        return this.scaleLineSegmentsToArea(lines, displayArea);
    }
}
