import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class RFoot extends BodyPart {
  public RFoot() {
    super();
    this.rect = new Rectangle(-20,-10,90,65);
    this.img = new ImageIcon("r_foot.png");
    this.minTheta = -1 * Math.PI * (35.0/180);
    this.maxTheta = Math.PI * (35.0/180);
  }
}
