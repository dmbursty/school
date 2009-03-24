import java.awt.Rectangle;

import javax.swing.ImageIcon;
public class LHand extends BodyPart {
  public LHand() {
    super();
    this.rect = new Rectangle(-20,5,35,65);
    this.img = new ImageIcon("hand.png");
    this.minTheta = -1 * Math.PI * (35.0/180);
    this.maxTheta = Math.PI * (35.0/180);
  }
}

