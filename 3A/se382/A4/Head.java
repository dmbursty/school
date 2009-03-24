import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Head extends BodyPart {
  public Head() {
    super();
    this.rect = new Rectangle(-55,-80,110,110);
    this.img = new ImageIcon("head.png");
    this.minTheta = -1 * Math.PI * (5.0/18);
    this.maxTheta = Math.PI * (5.0/18);
  }
}
