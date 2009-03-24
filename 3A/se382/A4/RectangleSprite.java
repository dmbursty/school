import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;


/**
 * A simple demo of how to create a rectangular sprite.
 * 
 * Michael Terry
 */
public class RectangleSprite extends Sprite {

    private Rectangle2D rect = null;

    /**
     * Creates a rectangle based at the origin with the specified
     * width and height
     */
    public RectangleSprite(int width, int height) {
        super();
        this.initialize(width, height);
    }
    /**
     * Creates a rectangle based at the origin with the specified
     * width, height, and parent
     */
    public RectangleSprite(int width, int height, Sprite parentSprite) {
        super(parentSprite);
        this.initialize(width, height);
    }
    private void initialize(int width, int height) {
        rect = new Rectangle2D.Double(0, 0, width, height);
    }
    
    /**
     * Test if our rectangle contains the point specified.
     */
    public boolean pointInside(Point2D p) {
        AffineTransform fullTransform = this.getFullTransform();
        AffineTransform inverseTransform = null;
        try {
            inverseTransform = fullTransform.createInverse();
        } catch (NoninvertibleTransformException e) {
            e.printStackTrace();
        }
        Point2D newPoint = (Point2D)p.clone();
        inverseTransform.transform(newPoint, newPoint);
        return rect.contains(newPoint);
    }

    protected void drawSprite(Graphics2D g) {
        g.setColor(Color.BLACK);
        //g.draw(rect);
        g.drawImage(new ImageIcon("ball.png").getImage(), (int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight(), null);
//        g.draw(this.getFullTransform().createTransformedShape(rect));
    }
    
    public String toString() {
        return "RectangleSprite: " + rect;
    }
}
