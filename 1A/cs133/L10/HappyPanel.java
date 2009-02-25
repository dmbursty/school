import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class HappyPanel extends JPanel implements MouseListener
{
  public void paintComponent(Graphics canvas)
  {
    // Circle for the face.
    canvas.drawOval(100, 50, 200, 200);
    
    // Two filled ovals for eyes.
    canvas.fillOval(155, 100, 10, 20);
    canvas.fillOval(230, 100, 10, 20);
    
    // Arc for mouth.
    canvas.drawArc(150, 160, 100, 50, 180, 180);
  }
  
  
  
  
  public void mouseClicked(MouseEvent e)
  {
    System.out.println("mouseClicked " + e.getPoint().toString());
  }
  public void mousePressed(MouseEvent e){}
  public void mouseReleased(MouseEvent e){}
  public void mouseEntered(MouseEvent e){}
  public void mouseExited(MouseEvent e){}
  
}
