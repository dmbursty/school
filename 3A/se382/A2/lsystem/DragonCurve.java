/*
 *
 */
package lsystem;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Vector;

public class DragonCurve extends AbstractIteratedSystem {

    private TurtleGraphics turtle = new TurtleGraphics();
    Vector<Point2D.Double> points = new Vector<Point2D.Double>();
    String                 curString = "";
    
    public DragonCurve() {
        super(15);
        this.reset();
    }

    public String getName() {
        return "Dragon Curve";
    }

    public Vector<Double> getLines(Rectangle displayArea) {
        return this.scalePointsToArea(points, displayArea);
    }

    public void reset() {
        super.reset();
        curString = "FX";
        turtle.setLocation(new Point2D.Double(0,0));
        turtle.pointRight();
        points.clear();
        points.add(turtle.getLocation());
    }

    protected void iterateSystem() {
        StringBuffer newString = new StringBuffer();
        
        turtle.setLocation(new Point2D.Double(0,0));
        turtle.pointRight();
        points.clear();
        points.add(turtle.getLocation());
        
        for (int i = 0; i < curString.length(); i++) {
            switch (curString.charAt(i)) {
                case 'F':
                    points.add(turtle.moveForward());
                    newString.append(curString.charAt(i));
                    break;
                case 'X':
                    newString.append("X+YF+");
                    break;
                case 'Y':
                    newString.append("-FX-Y");
                    break;
                case '-':
                    turtle.turn(-90);
                    newString.append(curString.charAt(i));
                    break;
                case '+':
                    turtle.turn(90);
                    newString.append(curString.charAt(i));
                    break;
            }
        }
        curString = newString.toString();
    }
}
