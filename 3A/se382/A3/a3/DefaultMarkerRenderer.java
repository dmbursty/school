package a3;



import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.Vector;

/**
 * Basic, boring point cloud renderer
 */
public class DefaultMarkerRenderer implements MarkerRenderer {

    private int pointSize = 1;
    public DefaultMarkerRenderer(int pointSize) {
        this.pointSize = pointSize;
    }
    public void renderMarkers(Graphics2D g, Vector<Point3D> points, Rectangle destArea) {
        g.setColor(Color.WHITE);
        g.fillRect(destArea.x, destArea.y, destArea.width, destArea.height);
        g.setColor(Color.BLACK);
        for (int i = 0; i < points.size(); i++) {
            Point2D.Double p = points.elementAt(i);
            g.fillOval((int)p.x, (int)p.y, pointSize, pointSize);
        }
    }
}
