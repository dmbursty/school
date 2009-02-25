/*
 *
 */
package lsystem;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class TurtleGraphics {

    private Point2D.Double curLocation = new Point2D.Double();
    private Point2D.Double curAngle    = new Point2D.Double();
    
    public Point2D.Double getLocation() {
        return (Point2D.Double)curLocation.clone();
    }
    
    public void setLocation(Point2D.Double location) {
        curLocation.setLocation(location);
    }
    
    public Point2D.Double getAngleVector() {
        return (Point2D.Double)curAngle.clone();
    }
    public void setAngleVector(Point2D.Double angle) {
        curAngle.setLocation(angle);
    }
    
    public Point2D.Double moveForward() {
        curLocation.x = curLocation.x + curAngle.x;
        curLocation.y = curLocation.y + curAngle.y;
        return this.getLocation();
    }
    
    public void pointLeft() {
        curAngle.setLocation(-1, 0);
    }
    public void pointRight() {
        curAngle.setLocation(1, 0);
    }
    public void pointUp() {
        curAngle.setLocation(0, -1);
    }
    public void pointDown() {
        curAngle.setLocation(0, 1);
    }
    public void turn(double angle) {
        AffineTransform t = new AffineTransform();
        t.rotate(Math.toRadians(angle));
        t.transform(this.curAngle, this.curAngle);
    }
}
