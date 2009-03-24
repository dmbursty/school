import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class LFoot extends BodyPart {
  public LFoot() {
    super();
    this.rect = new Rectangle(-70,-10,90,65);
    this.img = new ImageIcon("foot.png");
    this.minTheta = -1 * Math.PI * (35.0/180);
    this.maxTheta = Math.PI * (35.0/180);
  }
}
