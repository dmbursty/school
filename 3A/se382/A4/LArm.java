import java.awt.Rectangle;

import javax.swing.ImageIcon;
public class LArm extends BodyPart {
  public LArm() {
    super();
    this.rect = new Rectangle(-25,-30,40,90);
    this.img = new ImageIcon("up_arm.png");
  }
}
