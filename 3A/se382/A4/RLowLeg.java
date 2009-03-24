import java.awt.Rectangle;

import javax.swing.ImageIcon;
public class RLowLeg extends BodyPart {
  public RLowLeg() {
    super();
    this.rect = new Rectangle(-20,-20,40,110);
    this.img = new ImageIcon("r_low_leg.png");
    this.minTheta = -1 * (Math.PI/2);
    this.maxTheta = Math.PI/2;
    this.canScale = true;
  }
}
