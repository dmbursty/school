package a3;



import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Vector;

/**
 * Renderer for rendering a point cloud
 */
public interface MarkerRenderer {

    public void renderMarkers(Graphics2D g, Vector<Point3D> points, Rectangle destArea);
    
}
