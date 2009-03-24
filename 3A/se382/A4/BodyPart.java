import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;


public abstract class BodyPart extends Sprite {
  
  protected ImageIcon img = new ImageIcon("ball.png");
  protected Rectangle2D rect;
  
  protected boolean canScale = false;
  
  @Override
  protected void handleMouseWheelMovedEvent(MouseWheelEvent e) {
    if (canScale) {
      super.handleMouseWheelMovedEvent(e);
    }
  }
  
  protected void handleMouseDownEvent(MouseEvent e) {
    super.handleMouseDownEvent(e);
    interactionMode = InteractionMode.ROTATING;
  }

  @Override
  protected void drawSprite(Graphics2D g) {
    g.drawImage(img.getImage(), (int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight(), null);
  }

  @Override
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
}
