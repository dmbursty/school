import java.awt.Rectangle;

import javax.swing.ImageIcon;
public class RArm extends BodyPart {
  public RArm() {
    super();
    this.rect = new Rectangle(-15,-30,40,90);
    this.img = new ImageIcon("r_up_arm.png");
  }
}
