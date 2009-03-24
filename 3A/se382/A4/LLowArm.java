import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class LLowArm extends BodyPart {
  public LLowArm() {
    super();
    this.rect = new Rectangle(-20,0,40,90);
    this.img = new ImageIcon("low_arm.png");
    this.minTheta = -1 * Math.PI * (135.0/180);
    this.maxTheta = Math.PI * (135.0/180);
  }
}
