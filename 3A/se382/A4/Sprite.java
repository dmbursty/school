import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Vector;

/**
 * A building block for creating your own shapes that can be
 * transformed and that can respond to input. This class is
 * provided as an example; you will likely need to modify it
 * to meet the assignment requirements.
 * 
 * Michael Terry
 */
public abstract class Sprite {
    
    /**
     * Tracks our current interaction mode after a mouse-down
     */
    protected enum InteractionMode {
        IDLE,
        DRAGGING,
        SCALING,
        ROTATING
    }
    
    private final double MAX_SCALE = 2.5;
    private final double MIN_SCALE = 0.4;

    private     Vector<Sprite>      children            = new Vector<Sprite>();     // Holds all of our children
    private     Sprite              parent              = null;                     // Pointer to our parent
    private     AffineTransform     transform           = new AffineTransform();    // Our transformation matrix
    private     Vector<Sprite>      scaleBuddies        = new Vector<Sprite>();     // When we get scaled, scale these buddies
    
    private Point2D translate = new Point2D.Double(0,0);
    private double rotation = 0;
    private Point2D scale = new Point2D.Double(1,1);
    
    protected double minTheta = -1 * Math.PI;
    protected double maxTheta = Math.PI;

    protected   Point2D startPoint;
    protected   double startRotation;
    protected   AffineTransform startTransform;
    
    protected   Point2D             lastPoint           = null;                     // Last mouse point
    protected   InteractionMode     interactionMode     = InteractionMode.IDLE;     // Current interaction mode

    
    public Sprite() {
        ; // no-op
    }
    
    public Sprite(Sprite parent) {
        if (parent != null) {
            parent.addChild(this);
        }
    }

    public void addChild(Sprite s) {
        children.add(s);
        s.setParent(this);
    }
    public Sprite getParent() {
        return parent;
    }
    private void setParent(Sprite s) {
        this.parent = s;
    }

    /**
     * Test whether a point, in world coordinates, is within our sprite.
     */
    public abstract boolean pointInside(Point2D p);

    public void addScaleBuddy(Sprite s) {
      this.scaleBuddies.add(s);
    }

    protected void handleMouseWheelMovedEvent(MouseWheelEvent e) {
      double new_sy = scale.getY() + (-0.1 * e.getWheelRotation());
      if (new_sy < MIN_SCALE) new_sy = MIN_SCALE;
      if (new_sy > MAX_SCALE) new_sy = MAX_SCALE;
      scale(scale.getX(), new_sy);
      for(Sprite s : scaleBuddies) {
        s.scale(scale.getX(), new_sy);
      }
    }
    
    /**
     * Handles a mouse down event, assuming that the event has already
     * been tested to ensure the mouse point is within our sprite.
     */
    protected void handleMouseDownEvent(MouseEvent e) {
        lastPoint = e.getPoint();
        startPoint = lastPoint;
        startRotation = rotation;
        try {
          startTransform = parent == null ? new AffineTransform() : this.getNonScaleFullTransform().createInverse();
        } catch (NoninvertibleTransformException e1) {}
        if (e.getButton() == MouseEvent.BUTTON1) {
            interactionMode = InteractionMode.DRAGGING;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
          interactionMode = InteractionMode.ROTATING;
        }
        // Handle rotation, scaling mode depending on input
    }
    
    /**
     * Handle mouse drag event, with the assumption that we have already
     * been "selected" as the sprite to interact with.
     * This is a very simple method that only works because we
     * assume that the coordinate system has not been modified
     * by scales or rotations. You will need to modify this method
     * appropriately so it can handle arbitrary transformations.
     */
    protected void handleMouseDragEvent(MouseEvent e) {
        
        Point2D oldPoint = lastPoint;
        Point2D newPoint = e.getPoint();
        lastPoint = e.getPoint();
        
        switch (interactionMode) {
            case IDLE:
                ; // no-op (shouldn't get here)
                break;
            case DRAGGING:
              oldPoint = toParent(oldPoint);
              newPoint = toParent(newPoint);
              double x_diff = newPoint.getX() - oldPoint.getX();
              double y_diff = newPoint.getY() - oldPoint.getY();

              x_diff /= (parent == null ? 1 : parent.scale.getX());
              y_diff /= (parent == null ? 1 : parent.scale.getY());
              
              translate(x_diff, y_diff);
              break;
            case ROTATING:;
              oldPoint = toStart(startPoint);
              newPoint = toStart(newPoint);
              double theta = startRotation + angleBetween(oldPoint, newPoint);
              while(theta > Math.PI) {theta -= 2*Math.PI;}
              while(theta < -1*Math.PI) {theta += 2*Math.PI;}
              if (theta < minTheta) theta = minTheta;
              if (theta > maxTheta) theta = maxTheta;
              rotate(theta);
              break;
            case SCALING:
                ; // Provide scaling code here
                break;
                
        }
        // Save our last point, if it's needed next time around
    }
    
    protected void handleMouseUp(MouseEvent e) {
        interactionMode = InteractionMode.IDLE;
        // Do any other interaction handling necessary here
    }
    
    /**
     * Locates the sprite that was hit by the given event.
     * You *may* need to modify this method, depending on
     * how you modify other parts of the class.
     * 
     * @return The sprite that was hit, or null if on sprite was hit
     */
    public Sprite getSpriteHit(MouseEvent e) {
        for (Sprite sprite : children) {
            Sprite s = sprite.getSpriteHit(e);
            if (s != null) {
                return s;
            }
        }
        if (this.pointInside(e.getPoint())) {
            return this;
        }
        return null;
    }
    
    /*
     * Important note: How transforms are handled here are only an example. You will
     * likely need to modify this code for it to work for your assignment.
     */
    
    public Point2D toParent(Point2D p) {
      Point2D ret = new Point();
      AffineTransform t = null;
      try {
        t = parent == null ? new AffineTransform() : parent.getNonScaleFullTransform().createInverse();
      } catch (NoninvertibleTransformException e1) {}
      t.transform(p, ret);
      return ret;
    }
    
    public Point2D toLocal(Point2D p) {
      Point2D ret = new Point();
      AffineTransform t = null;
      try {
        t = parent == null ? new AffineTransform() : this.getFullTransform().createInverse();
      } catch (NoninvertibleTransformException e1) {}
      t.transform(p, ret);
      return ret;
    }
    
    public Point2D toStart(Point2D p) {
      Point2D ret = new Point();
      startTransform.transform(p, ret);
      return ret;
    }
    
    /**
     * Returns the full transform to this object from the root
     */
    public AffineTransform getFullTransform() {
        AffineTransform returnTransform = getLocalTransform();
        Sprite curSprite = this.parent;
        while (curSprite != null) {
            returnTransform.preConcatenate(curSprite.getNonScaleTransform());
            curSprite = curSprite.getParent();
        }
        return returnTransform;
    }
    
    public AffineTransform getNonScaleFullTransform() {
      AffineTransform returnTransform = new AffineTransform();
      Sprite curSprite = this;
      while (curSprite != null) {
          returnTransform.preConcatenate(curSprite.getNonScaleTransform());
          curSprite = curSprite.getParent();
      }
      return returnTransform;
  }

    /**
     * Returns our local transform
     */
    public AffineTransform getLocalTransform() {
        AffineTransform ret = new AffineTransform();
        double mod_x = translate.getX() * (parent == null ? 1 : parent.scale.getX());
        double mod_y = translate.getY() * (parent == null ? 1 : parent.scale.getY());
        ret.concatenate(AffineTransform.getTranslateInstance(mod_x, mod_y));
        ret.concatenate(AffineTransform.getRotateInstance(rotation));
        ret.concatenate(AffineTransform.getScaleInstance(scale.getX(), scale.getY()));
        return ret;
    }
    
    public AffineTransform getNonScaleTransform() {
      AffineTransform ret = new AffineTransform();
      double mod_x = translate.getX() * (parent == null ? 1 : parent.scale.getX());
      double mod_y = translate.getY() * (parent == null ? 1 : parent.scale.getY());
      ret.concatenate(AffineTransform.getTranslateInstance(mod_x, mod_y));
      ret.concatenate(AffineTransform.getRotateInstance(rotation));
      return ret;
    }
    
    public void rotate(double theta) {
      rotation = theta;
    }
    
    public void translate(double x_diff, double y_diff) {
      translate.setLocation(translate.getX() + x_diff, translate.getY() + y_diff);
    }
    
    public void scale(double sx, double sy) {
      scale.setLocation(sx, sy);
    }
    
    /**
     * Performs an arbitrary transform on this sprite
     */
    public void transform(AffineTransform t) {
        transform.concatenate(t);
    }

    /**
     * Draws the sprite. This method will call drawSprite after
     * the transform has been set up for this sprite.
     */
    public void draw(Graphics2D g) {
        AffineTransform oldTransform = (AffineTransform) g.getTransform().clone();

        AffineTransform trans = g.getTransform();
        trans.concatenate(getFullTransform());
        // Set to our transform
        g.setTransform(trans);
        
        // Draw the sprite (delegated to sub-classes)
        this.drawSprite(g);
        
        // Restore original transform
        g.setTransform(oldTransform);

        // Draw children
        for (Sprite sprite : children) {
            sprite.draw(g);
        }
    }
    
    static public double angleBetween(Point2D a, Point2D b) {
      double dotProduct = a.getX() * b.getX() + a.getY() * b.getY();
      double a_normal = Math.sqrt(a.getX()*a.getX() + a.getY()*a.getY());
      double b_normal = Math.sqrt(b.getX()*b.getX() + b.getY()*b.getY());
      if (a_normal * b_normal == 0) return 0;
      double theta = dotProduct / (a_normal * b_normal);
      if (theta > 1) theta = 1;
      if (theta < -1) theta = -1;
      double crossProduct = a.getX() * b.getY() - a.getY() * b.getX();
      theta = Math.acos(theta) * (crossProduct < 0 ? -1 : 1);
      while(theta > Math.PI) {theta -= 2*Math.PI;}
      while(theta < -1*Math.PI) {theta += 2*Math.PI;}
      return theta;
      // -pi <= return <= pi
    }
    
    /**
     * The method that actually does the sprite drawing. This method
     * is called after the transform has been set up in the draw() method.
     * Sub-classes should override this method to perform the drawing.
     */
    protected abstract void drawSprite(Graphics2D g);
}
