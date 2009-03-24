import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class Canvas extends JPanel {
  
  private Vector<Sprite> sprites = new Vector<Sprite>();
  
  public void paint(Graphics g) {
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, this.getWidth(), this.getHeight());
    g.setColor(Color.BLACK);
    for (Sprite sprite : sprites) {
        sprite.draw((Graphics2D)g);
    }
}
  
  public static void main(String[] args) {
    Canvas c = new Canvas();
    
    JFrame f = new JFrame("Paper Doll");
    f.getContentPane().add(c);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setSize(800,600);
    f.setVisible(true);
  }
}
