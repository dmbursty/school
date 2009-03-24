import java.awt.Rectangle;

import javax.swing.ImageIcon;
public class RHand extends BodyPart {
  public RHand() {
    super();
    this.rect = new Rectangle(-15,5,35,65);
    this.img = new ImageIcon("r_hand.png");
    this.minTheta = -1 * Math.PI * (35.0/180);
    this.maxTheta = Math.PI * (35.0/180);
  }
}

