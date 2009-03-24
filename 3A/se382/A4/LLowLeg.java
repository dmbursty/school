import java.awt.Rectangle;

import javax.swing.ImageIcon;
public class LLowLeg extends BodyPart {
  public LLowLeg() {
    super();
    this.rect = new Rectangle(-20,-20,40,110);
    this.img = new ImageIcon("low_leg.png");
    this.minTheta = -1 * (Math.PI/2);
    this.maxTheta = Math.PI/2;
    this.canScale = true;
  }
}
