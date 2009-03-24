package a3;



import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import a3.util.Matrix3D;




@SuppressWarnings("serial")
public class MarkerView extends JComponent {

    private MarkerSet markers = null;
    private MarkerRenderer renderer = null;
    private Point lastPoint = null;
    private PropertyChangeListener markerListener = null;
    
    public MarkerView(MarkerSet markerSet, MarkerRenderer renderer) {
        this.markerListener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                repaint();
            }
        };
        this.setMarkers(markerSet);
        this.renderer = renderer;
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                lastPoint = e.getPoint();
            }
            public void mouseReleased(MouseEvent e) {
                lastPoint = null;
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (lastPoint == null) {
                    lastPoint = e.getPoint();
                } else {
                    Point newPoint = e.getPoint();
                    int dx = newPoint.x - lastPoint.x;
                    int dy = newPoint.y - lastPoint.y;
                    if (e.isShiftDown()) {
                        markers.incrementAzimuth(dx * Math.PI / 90);
                        markers.incrementZenith(dy * Math.PI / 90);
                    } else if (e.isControlDown()){
                        markers.incrementScale(-dy / 20.0);
                    } else {
                        markers.incrementHorTranslation(dx/100.0);
                        markers.incrementVerTranslation(dy/100.0);
                    }
                    lastPoint = newPoint;
                }            
            }
        });
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (markers.getNumFrames() > 0) {
            Rectangle destArea = new Rectangle(0, 0, this.getWidth(), this.getHeight());
            double scale = 0;
            if (destArea.width < destArea.height) {
                scale = destArea.width / markers.getWidth();
                if (scale * markers.getHeight() > destArea.height) {
                    scale = destArea.height / markers.getHeight();
                }
            } else {
                scale = destArea.height / markers.getHeight();
                if (scale * markers.getWidth() > destArea.width) {
                    scale = destArea.width / markers.getWidth();
                }
            }
            Matrix3D t = new Matrix3D();
            t.translate(destArea.x + destArea.width/2, destArea.y + destArea.height/2, 0);
            t.scale(scale, scale, 1);
            t.prependMatrix(markers.getCurrentTransform());
            renderer.renderMarkers((Graphics2D)g, markers.getFrame(markers.getCurFrame(), t), destArea);
        }
    }
    public MarkerSet getMarkers() {
        return markers;
    }
    public void setMarkers(MarkerSet newMarkers) {
        if (this.markers != null) {
            this.markers.removePropertyChangeListener(this.markerListener);
        }
        this.markers = newMarkers;
        if (this.markers != null) {
            this.markers.addPropertyChangeListener(this.markerListener);
        }
    }
}
