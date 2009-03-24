package a3;



import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.Serializable;

@SuppressWarnings("serial")
public class Point3D extends Point2D.Double implements Serializable {

    public double z = 0.0;
    public Point3D() {
        x = y = z = 0;
    }
    public Point3D(double x, double y, double z) {
        super(x, y);
        this.z = z;
    }
    public Object clone() {
        return copy();
    }
    public Point3D copy() {
        return new Point3D(x, y, z);
    }
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Point3D) {
            Point3D r = (Point3D)obj;
            return x == r.x && y == r.y && z == r.z;
        }
        return false;
    }
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);
    }
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();
    }
}
