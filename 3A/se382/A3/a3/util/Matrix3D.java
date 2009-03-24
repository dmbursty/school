package a3.util;

import a3.Point3D;



public class Matrix3D {
    private Matrix matrix = null;
    @SuppressWarnings("unused")
    private Matrix3D(double[][] m) {
        this.matrix = new Matrix(m);
    }
    @SuppressWarnings("unused")
    private Matrix3D(Matrix m) {
        this.matrix = m.copy();
    }
    public Matrix3D() {
        matrix = Matrix.identity(4, 4);
    }
    public Matrix3D(Matrix3D m) {
        matrix = m.matrix.copy();
    }
    public Matrix3D copy() {
        return new Matrix3D(this);
    }
    public Point3D transform(Point3D p) {
        Matrix m = new Matrix(new double[][] {{ p.x, p.y, p.z, 1 }});
        Matrix result = m.times(this.matrix);
        return new Point3D(result.get(0,0), result.get(0,1), result.get(0,2));
    }

    public static Matrix3D getTranslationMatrix(double tx, double ty, double tz) {
        Matrix3D m = new Matrix3D();
        m.setToTranslation(tx, ty, tz);
        return m;
    }
    public void setToTranslation(double tx, double ty, double tz) {
        this.matrix = new Matrix(new double[][] {{1, 0, 0, 0},
                                                 {0, 1, 0, 0},
                                                 {0, 0, 1, 0},
                                                 {tx, ty, tz, 1}} );
    }
    public void translate(double tx, double ty, double tz) {
        this.matrix = getTranslationMatrix(tx, ty, tz).matrix.times(this.matrix);
    }

    public static Matrix3D getRotationMatrix(double x, double y, double z) {
        Matrix3D m = new Matrix3D();
        m.setToRotation(x, y, z);
        return m;
    }
    public void setToRotation(double x, double y, double z) {
        double thisCos;
        double thisSin;
        thisCos = Math.cos(z);
        thisSin = Math.sin(z);
        this.matrix = new Matrix(new double[][] { {thisCos, thisSin, 0, 0},
                                                  {-1*thisSin, thisCos, 0, 0},
                                                  {0, 0, 1, 0},
                                                  {0, 0, 0, 1 }});
        thisCos = Math.cos(y);
        thisSin = Math.sin(y);
        Matrix m = new Matrix(new double[][] { {thisCos, 0, thisSin, 0},
                                               {0, 1, 0, 0},
                                               {-1*thisSin, 0, thisCos, 0},
                                               {0, 0, 0, 1}});
        this.matrix = this.matrix.times(m);

        thisCos = Math.cos(x);
        thisSin = Math.sin(x);
        m = new Matrix(new double[][] { {1, 0, 0, 0},
                                        {0, thisCos, -1*thisSin, 0},
                                        {0, thisSin, thisCos, 0},
                                        {0, 0, 0, 1}});
        this.matrix = this.matrix.times(m);
    }
    public void rotate(double x, double y, double z) {
        this.matrix = getRotationMatrix(x, y, z).matrix.times(this.matrix);
    }
    
    public static Matrix3D getScaleMatrix(double sx, double sy, double sz) {
        Matrix3D m = new Matrix3D();
        m.setToScale(sx, sy, sz);
        return m;
    }
    public void setToScale(double sx, double sy, double sz) {
        this.matrix = new Matrix(new double[][] {{sx, 0, 0, 0},
                                                {0, sy, 0, 0},
                                                {0, 0, sz, 0},
                                                {0, 0,  0, 1}});
    }
    public void scale(double sx, double sy, double sz) {
        this.matrix = getScaleMatrix(sx, sy, sz).matrix.times(this.matrix); 
    }
    
    public void prependMatrix(Matrix3D m) {
        this.matrix = m.matrix.times(this.matrix);
    }
    public void appendMatrix(Matrix3D m) {
        this.matrix = this.matrix.times(m.matrix);
    }
    
}
