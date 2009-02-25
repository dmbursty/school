/*
 *
 */
package lsystem;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Vector;

public class SierpinksiTriangle extends AbstractIteratedSystem {

    private String curString = null;
    private TurtleGraphics turtle = new TurtleGraphics();
    private Vector<Point2D.Double> points = new Vector<Point2D.Double>();
    private int curAngle = 0;
    
    public SierpinksiTriangle() {
        super(9);
        this.reset();
    }
    
    public String getName() {
        return "Sierpinski Triangle";
    }

    public void reset() {
        super.reset();
        curString = "A";
        turtle.setLocation(new Point2D.Double(0,0));
        turtle.pointRight();
        points.clear();
        points.add(turtle.getLocation());
        curAngle = 60;
    }
    protected void iterateSystem() {
        StringBuffer newString = new StringBuffer();
        
        turtle.setLocation(new Point2D.Double(0,0));
        turtle.pointRight();
        points.clear();
        points.add(turtle.getLocation());
        
        for (int i = 0; i < this.curString.length(); i++) {
            switch (curString.charAt(i)) {
                case 'A':
                    this.points.add(this.turtle.moveForward());
                    newString.append("B-A-B");
                    break;
                case 'B':
                    this.points.add(this.turtle.moveForward());
                    newString.append("A+B+A");
                    break;
                case '-':
                    this.turtle.turn(this.curAngle);
                    newString.append(curString.charAt(i));
                    break;
                case '+':
                    this.turtle.turn(-1 * this.curAngle);
                    newString.append(curString.charAt(i));
                    break;
            }
        }
        this.curString = newString.toString();
        this.curAngle = this.curAngle * -1;
    }

    public Vector<Double> getLines(Rectangle displayArea) {
        return this.scalePointsToArea(this.points, displayArea);
    }

}
