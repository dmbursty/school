import java.awt.*;
import javax.swing.*;

public class HappyFrame extends JFrame
{
  public HappyFrame()
  {
    HappyPanel happyPanel = new HappyPanel();
    this.getContentPane().add(happyPanel);
    this.setSize(400, 350);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);
  }
      public static void main(String[] args)
  {
    HappyFrame happy = new HappyFrame();
  }
}