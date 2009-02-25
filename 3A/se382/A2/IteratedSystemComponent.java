import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D.Double;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class IteratedSystemComponent extends JPanel implements Observer, ZoomListener {
  public final static int BASE_WIDTH = 400;
  public final static int BASE_HEIGHT = 400;
  
  private Point startDrag = new Point(0,0);
  private Point endDrag = new Point(0,0);
  private boolean dragging = false;
  
  private JScrollPane scroller;
  
  public IteratedSystemComponent() {
    super();
    IteratedSystemManager.getInstance().addObserver(this);
    IteratedSystemManager.getInstance().addZoomListener(this);
    setPreferredSize(new Dimension(BASE_WIDTH, BASE_HEIGHT));
    
    addMouseListener(new MouseListener() {
      public void mouseReleased(MouseEvent e) {
        dragging = false;
        // Take zoom effect
        int diffx = Math.abs(startDrag.x - endDrag.x);
        int diffy = Math.abs(startDrag.y - endDrag.y);
        int x = (int) (Math.min(startDrag.x, endDrag.x) + diffx/2.0);
        int y = (int) (Math.min(startDrag.y, endDrag.y) + diffy/2.0);
        int buffwidth = (int) ((scroller.getWidth() - BASE_WIDTH * getZoom()) / 2);
        int buffheight = (int) ((scroller.getHeight() - BASE_HEIGHT * getZoom()) / 2);
        System.out.println("Buffers are " + buffwidth + " x " + buffheight);
        System.out.println(startDrag + " to " + endDrag);
        
        if (diffx <= 3 || diffy <= 3) {
          repaint();
          return;
        }

        System.out.println(new Double((x - buffwidth)/(BASE_WIDTH * getZoom()), (y - buffheight)/(BASE_HEIGHT * getZoom())));
        IteratedSystemManager.getInstance().setPan(new Double((x - buffwidth)/(BASE_WIDTH * getZoom()), (y - buffheight)/(BASE_HEIGHT * getZoom())));
        double xzoom = (double)(scroller.getWidth() * getZoom()) / diffx;
        double yzoom = (double)(scroller.getHeight() * getZoom()) / diffy;
        IteratedSystemManager.getInstance().setZoomWithoutCentre(Math.min(xzoom, yzoom));
        
        repaint();
      }
      public void mousePressed(MouseEvent e) {
        dragging = true;
        startDrag = e.getPoint();
        endDrag = e.getPoint();
      }
      public void mouseClicked(MouseEvent arg0) {
        IteratedSystemManager.getInstance().setZoom(1.0);
      }
      public void mouseExited(MouseEvent arg0) {}
      public void mouseEntered(MouseEvent arg0) {}
    });
    
    addMouseMotionListener(new MouseMotionListener() {
      public void mouseDragged(MouseEvent e) {
        endDrag = e.getPoint();
        repaint();  // Draw Rect
      }
      public void mouseMoved(MouseEvent e) {}
    });
  }
  
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    // Paint Dots
    Vector<Double> points = IteratedSystemManager.getInstance().getPoints(getDrawingBounds());
    for(int i = 0; i < points.size(); i++) {
      g.drawRect((int)points.get(i).x, (int)points.get(i).y, 0, 0);
    }
    // Paint Lines
    Vector<Double> lines = IteratedSystemManager.getInstance().getLines(getDrawingBounds());
    //System.out.println(getDrawingBounds());
    for(int i = 0; i < lines.size() - 1; i++) {
      g.drawLine((int)lines.get(i).x, (int)lines.get(i).y, (int)lines.get(i+1).x, (int)lines.get(i+1).y);
    }
    // Paint Line Segments
    Vector<Vector<Double>> linesegs = IteratedSystemManager.getInstance().getLineSegments(getDrawingBounds());
    for(int i = 0; i < linesegs.size(); i++) {
      lines = linesegs.get(i);
      for(int j = 0; j < lines.size() - 1; j++) {
        g.drawLine((int)lines.get(j).x, (int)lines.get(j).y, (int)lines.get(j+1).x, (int)lines.get(j+1).y);
      }
    }
    
    if (dragging) {
      g.setXORMode(Color.WHITE);
      int x = Math.min(startDrag.x, endDrag.x);
      int y = Math.min(startDrag.y, endDrag.y);
      int diffx = Math.abs(startDrag.x - endDrag.x);
      int diffy = Math.abs(startDrag.y - endDrag.y);
      g.drawRect(x,y,diffx,diffy);
    }
  }

  public void update(Observable o, Object arg){
    repaint();
  }

  public Rectangle getDrawingBounds() {
    int width = (int) (BASE_WIDTH * getZoom());
    int height = (int) (BASE_HEIGHT * getZoom());
    int x = (int) (getWidth()/2 - width/2);
    int y = (int) (getHeight()/2 - height/2);
    return new Rectangle(x, y, width, height);
  }

  public double getZoom() {
    return IteratedSystemManager.getInstance().getZoom();
  }
  
  public Double getPan() {
    return IteratedSystemManager.getInstance().getPan();
  }

  public void update(double zoom) {
    int width = (int) (BASE_WIDTH * getZoom());
    int height = (int) (BASE_HEIGHT * getZoom());
    int aWidth = (int) getVisibleRect().getWidth() - 10;
    int aHeight = (int) getVisibleRect().getHeight() - 10;
    setPreferredSize(new Dimension(width, height));
    System.out.println("Panning to " + getPan());
    int x = (int)(getPan().x * width - aWidth/2.0);
    int y = (int)(getPan().y * height - aHeight/2.0);
    revalidate();
    scrollRectToVisible(new Rectangle(x, y, scroller.getWidth(), scroller.getHeight()));
    scrollRectToVisible(new Rectangle(x, y, scroller.getWidth(), scroller.getHeight()));
  }

  public void setScroller(JScrollPane scroller) {
    this.scroller = scroller;
  }
}
