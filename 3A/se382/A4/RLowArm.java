import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class RLowArm extends BodyPart {
  public RLowArm() {
    super();
    this.rect = new Rectangle(-20,0,40,90);
    this.img = new ImageIcon("r_low_arm.png");
    this.minTheta = -1 * Math.PI * (135.0/180);
    this.maxTheta = Math.PI * (135.0/180);
  }
}
