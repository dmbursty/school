import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
public class Body extends BodyPart {
  public Body() {
    super();
    this.rect = new Rectangle(0,0,160,220);
    this.img = new ImageIcon("body.png");
  }
  
  protected void handleMouseDownEvent(MouseEvent e) {
    super.handleMouseDownEvent(e);
    interactionMode = InteractionMode.DRAGGING;
  }
}

