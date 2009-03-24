package a3;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class TriSlider extends JComponent {
  int min, max;
  int left, right;
  int curr;
  Marker dragging;
  boolean notifying;
  Vector<CurrentFrameListener> currentListeners = new Vector<CurrentFrameListener>();
  myFrame viewer;
  
  
  private enum Marker {LEFT, RIGHT, CURR, INVALID}
  
  // Width of handle triangles
  private int getPadding() {
    return getSize().height/3;
  }
  
  // Gets pixel position of a frame based on the padding on either side and the width of the slider
  private int getNorm(int frame) {
    return (int)(((double)frame/(max - min))*(getSize().width - 2*getPadding())) + getPadding();
  }
  
  private int unNorm(int x) {
    if (x <= getPadding()) return min;
    if (x >= getSize().width - getPadding()) return max;
    double ratio = (double)(x - getPadding()) / (getSize().width - 2*getPadding());
    return (int)Math.round(min + ratio*(max-min));
  }
  
  private Polygon getLeftMarker() {
    int normL = getNorm(left);
    int height = getSize().height;
    return new Polygon(new int[] {normL - getPadding(), normL, normL}, new int[] {height, height - getPadding(), height}, 3);
  }
  
  private Polygon getRightMarker() {
    int normR = getNorm(right);
    int height = getSize().height;
    return new Polygon(new int[] {normR + getPadding(), normR, normR}, new int[] {height, height - getPadding(), height}, 3);
  }
  
  private Polygon getCurrMarker() {
    int normC = getNorm(curr);
    return new Polygon(new int[] {normC - getPadding()/2, normC + getPadding()/2, normC}, new int[] {0, 0, getPadding()}, 3);
  }

  public void setMin(int newmin) {min = newmin;}
  public void setMax(int newmax) {max = newmax;}
  public void setLeft(int newleft) {left = newleft;}
  public void setRight(int newright) {right = newright;}
  public void setCurr(int newcurr) {
    curr = newcurr;
    notifyCurrentFrameListeners();
    repaint();
  }
  
  public void delete() {
    // Delete the slider range
    int diff = right - left;
    setCurr(left);
    setMax(max - diff - 1);
    if (max < min) max = min;
    setRight(left);
    repaint();
  }
  
  public void paste(int numFrames) {
    setRight(right + numFrames);
    setMax(max + numFrames);
    repaint();
  }

  public void addCurrentFrameListener(CurrentFrameListener l) {
    currentListeners.add(l);
  }
  private void notifyCurrentFrameListeners() {
    if(notifying) return;
    notifying = true;
    for(int i = 0; i < currentListeners.size(); i++) {
      currentListeners.get(i).update();
    }
    notifying = false;
  }
  
  public void newMset(int numFrames) {
    if (numFrames < 0) numFrames = 0;
    this.min = 0;
    this.max = numFrames;
    this.setCurr(0);
    this.left = 0;
    this.right = numFrames;
    repaint();
  }
  
  public TriSlider(myFrame mviewer, int min, int max, int current) {
    if (max < min) max = min;
    this.min = min;
    this.max = max;
    this.curr = current;
    this.left = min;
    this.right = max;
    this.viewer = mviewer;
    
    addMouseListener(new MouseListener() {    
      public void mouseClicked(MouseEvent e) {
        gotClick(e.getX());
      }
      public void mousePressed(MouseEvent e) {
        viewer.getAnimator().hold();
        if (getLeftMarker().contains(e.getPoint())) {dragging = Marker.LEFT;}
        else if (getRightMarker().contains(e.getPoint())) {dragging = Marker.RIGHT;}
        else if (getCurrMarker().contains(e.getPoint())) {dragging = Marker.CURR;}
        else dragging = Marker.INVALID;
      }
      public void mouseReleased(MouseEvent e) {viewer.getAnimator().unhold();}
      public void mouseExited(MouseEvent e) {}
      public void mouseEntered(MouseEvent e) {}
    });
    
    addMouseMotionListener(new MouseMotionListener() {
      public void mouseDragged(MouseEvent e) {
        gotDrag(e.getX());
      }
      public void mouseMoved(MouseEvent e) {}
    });
  }
  
  public void paint(Graphics g) {
    // Draw Left Marker
    g.drawLine(getNorm(left), 0, getNorm(left), getSize().height);
    g.fillPolygon(getLeftMarker());

    // Draw Right Marker
    g.drawLine(getNorm(right), 0, getNorm(right), getSize().height);
    g.fillPolygon(getRightMarker());
    
    // Draw Current Marker
    g.drawLine(getNorm(curr), 0, getNorm(curr), getSize().height);
    g.fillPolygon(getCurrMarker());
    
    int midline = getSize().height/2;
    g.drawLine(getPadding(), midline, getSize().width - getPadding(), midline);
    g.fillRect(getNorm(left), midline - getPadding()/3, getNorm(right) - getNorm(left), (getPadding()*2)/3);
  }
  
  private void gotClick(int x) {
    if (x < getNorm(min)) {left = min;}
    else if (x <= getNorm(left)) {left = unNorm(x);}
    else if (x < getNorm(right)) {setCurr(unNorm(x));}
    else if (x <= getNorm(max)) {right = unNorm(x);}
    else {right = max;}
    repaint();
  }
  
  private void gotDrag(int x) {
    if (dragging == Marker.INVALID) return;
    // Left marker
    else if (dragging == Marker.LEFT) {
      int target = unNorm(x);
      if (target > right) target = right;
      if (curr < target) setCurr(target);
      if (left != target) {
        setLeft(target);
        repaint();
      }
    } else if (dragging == Marker.RIGHT) {
      int target = unNorm(x);
      if (target < left) target = left;
      if (curr > target) setCurr(target);
      if (right != target) {
        setRight(target);
        repaint();
      }
    } else if (dragging == Marker.CURR) {
      int target = unNorm(x);
      if (target < left) target = left;
      if (target > right) target = right;
      if (curr != target) {
        setCurr(target);
        repaint();
      }
    }
  }
}

