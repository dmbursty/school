package a3;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Vector;

import a3.util.C3DParser;
import a3.util.Matrix3D;

public class MarkerSet implements Serializable {

    public static final String FILE_VERSION = "UWM 1.0";
    public static final String MARKER_SET_PROPERTY = "MARKER_SET_PROPERTY";
    public static final String AZIMUTH_PROPERTY = "AZIMUTH_PROPERTY";
    public static final String ZENITH_PROPERTY = "ZENITH_PROPERTY";
    public static final String SCALE_PROPERTY = "SCALE_PROPERTY";
    public static final String HOR_TRANSLATION_PROPERTY = "HOR_TRANSLATION_PROPERTY";
    public static final String VER_TRANSLATION_PROPERTY = "VER_TRANSLATION_PROPERTY";
    public static final String CUR_FRAME_PROPERTY = "CUR_FRAME_PROPERTY";
    
    private static final long serialVersionUID = 0;
    
    private static final int SUB_SAMPLE_FACTOR = 4;

    private Vector<Vector<Point3D>> frames = null;
    private double azimuth = 0.0;
    private double zenith = 0.0;
    private double scale = 1.0;
    private double horTranslation = 0.0;
    private double verTranslation = 0.0;
    private Point3D dimensions = null;
    private int curFrame = 0;
    private transient PropertyChangeSupport listeners = null;

    public MarkerSet() {
        frames = new Vector<Vector<Point3D>>();
        dimensions = new Point3D();
        listeners = new PropertyChangeSupport(this);
    }
    private MarkerSet(Vector<Vector<Point3D>> data, Point3D dimensions) {
        this.frames = data;
        this.dimensions = dimensions.copy();
        this.listeners = new PropertyChangeSupport(this);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * Assumes last frame is the volume data
     */
    private static Vector<Vector<Point3D>> normalizeData(Vector<Vector<Point3D>> frames) {
        Vector<Point3D> lastFrame = frames.lastElement();
        frames.remove(frames.size()-1);
        Point3D minPoint = lastFrame.elementAt(0);
        Point3D dimensions = lastFrame.elementAt(1);
        double newWidth=0, newHeight=0, newDepth=0;
        double unitScale = 1/Math.max(Math.max(dimensions.x, dimensions.y), dimensions.z);

        newHeight = dimensions.y * unitScale;
        if (dimensions.z > dimensions.x) {
            newWidth = dimensions.z * unitScale;
            newDepth = dimensions.x * unitScale;
        } else {
            newWidth = dimensions.x * unitScale;
            newDepth = dimensions.z * unitScale;
        }
        

        Matrix3D t = new Matrix3D();
        // Scale to unit size then 4:3 aspect ratio
        t.scale(unitScale, unitScale, unitScale);
        // If deeper than wide, then rotate it about y axis
        if (dimensions.z > dimensions.x) {
            t.rotate(0, -Math.PI/2, 0);
        }
        // Translate center to the origin
        t.translate(-minPoint.x - dimensions.x/2, -minPoint.y - dimensions.y/2, -minPoint.z - dimensions.z/2);

        Vector<Vector<Point3D>> newFrames = new Vector<Vector<Point3D>>();
        for (Vector<Point3D> v : frames) {
            Vector<Point3D> newVector = new Vector<Point3D>();
            for (Point3D p : v) {
                newVector.add(t.transform(p));
            }
            newFrames.add(newVector);
        }

        minPoint = t.transform(minPoint);
        dimensions.x = newWidth;
        dimensions.y = newHeight;
        dimensions.z = newDepth;
        Vector<Point3D> sizeFrame = new Vector<Point3D>();
        sizeFrame.add(minPoint);
        sizeFrame.add(dimensions);
        newFrames.add(sizeFrame);
        
        return newFrames;
    }
    
    public static MarkerSet readMarkerSetFromC3D(File c3dFile) throws IOException {
        Vector<Vector<Point3D>> frames = normalizeData(C3DParser.parseFile(c3dFile, true, SUB_SAMPLE_FACTOR, true));
        Vector<Point3D> lastFrame = frames.lastElement();
        frames.remove(frames.size()-1);
        @SuppressWarnings("unused")
        Point3D minPoint = lastFrame.elementAt(0);
        Point3D dimensions = lastFrame.elementAt(1);
        return new MarkerSet(frames, dimensions);
    }
    public static MarkerSet readMarkerSetFromUWMFile(File uwmFile) throws IOException, ClassNotFoundException {
        FileInputStream in = new FileInputStream(uwmFile);
        MarkerSet markers = readMarkerSetFromStream(in);
        in.close();
        return markers;
    }
    public static MarkerSet readMarkerSetFromStream(InputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(in);
        return (MarkerSet)ois.readObject();
    }
    public void writeMarkerSet(File uwmFile) throws IOException {
        FileOutputStream out = new FileOutputStream(uwmFile);
        writeMarkerSet(out);
        out.flush();
        out.close();
    }
    public void writeMarkerSet(OutputStream out) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(this);
        oos.flush();
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(frames);
        out.writeDouble(azimuth);
        out.writeDouble(zenith);
        out.writeDouble(scale);
        out.writeDouble(horTranslation);
        out.writeDouble(verTranslation);
        out.writeObject(dimensions);
        // Write out object version
        out.writeObject(FILE_VERSION);
        // If you have extended this class in some way, then write out any
        // unique member variables here and set the object version above to
        // your own unique version
    }
    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        frames = (Vector<Vector<Point3D>>)in.readObject();
        azimuth = in.readDouble();
        zenith = in.readDouble();
        scale = in.readDouble();
        horTranslation = in.readDouble();
        verTranslation = in.readDouble();
        dimensions = (Point3D)in.readObject();
        @SuppressWarnings("unused")
        String version = (String)in.readObject();
        listeners = new PropertyChangeSupport(this);
        // If you are extending the class, check the object version. If it is yours,
        // then read in your other member variables here. Otherwise, you'll need
        // to create default values for your uninitialized member variables
    }

    public Matrix3D getCurrentTransform() {        
        Matrix3D t = new Matrix3D();
        t.scale(scale, scale, scale);
        t.translate(horTranslation, verTranslation, 0);
        t.rotate(zenith, 0, 0);
        t.rotate(0, azimuth, 0);
        return t;
    }

    public int getNumFrames() {
        return frames.size();
    }
    
    public double getWidth() {
        return dimensions.x;
    }
    public double getHeight() {
        return dimensions.y;
    }
    
    public int getCurFrame() {
        return curFrame;
    }
    public void setCurFrame(int newCurFrame) {
        int oldValue = curFrame;
        this.curFrame = newCurFrame;
        listeners.firePropertyChange(CUR_FRAME_PROPERTY, oldValue, newCurFrame);
    }
    public void setAzimuth(double amount) {
        double oldValue = this.azimuth;
        this.azimuth = amount;
        listeners.firePropertyChange(AZIMUTH_PROPERTY, new Double(oldValue), new Double(azimuth));
    }
    public void incrementAzimuth(double amount) {
        double oldValue = this.azimuth;
        this.azimuth += amount;
        listeners.firePropertyChange(AZIMUTH_PROPERTY, new Double(oldValue), new Double(azimuth));
    }
    public void setZenith(double amount) {
        double oldValue = this.zenith;
        this.zenith = amount;
        listeners.firePropertyChange(ZENITH_PROPERTY, new Double(oldValue), new Double(zenith));
    }
    public void incrementZenith(double amount) {
        double oldValue = this.zenith;
        this.zenith += amount;
        listeners.firePropertyChange(ZENITH_PROPERTY, new Double(oldValue), new Double(zenith));
    }
    public void setHorTranslation(double amount) {
        double oldValue = this.horTranslation;
        this.horTranslation = amount;
        listeners.firePropertyChange(HOR_TRANSLATION_PROPERTY, new Double(oldValue), new Double(horTranslation));
    }
    public void incrementHorTranslation(double amount) {
        double oldValue = this.horTranslation;
        this.horTranslation += amount;
        listeners.firePropertyChange(HOR_TRANSLATION_PROPERTY, new Double(oldValue), new Double(horTranslation));
    }
    public void setVerTranslation(double amount) {
        double oldValue = this.verTranslation;
        this.verTranslation = amount;
        listeners.firePropertyChange(VER_TRANSLATION_PROPERTY, new Double(oldValue), new Double(verTranslation));
    }
    public void incrementVerTranslation(double amount) {
        double oldValue = this.verTranslation;
        this.verTranslation += amount;
        listeners.firePropertyChange(VER_TRANSLATION_PROPERTY, new Double(oldValue), new Double(verTranslation));
    }
    public void setScale(double amount) {
        double oldValue = this.scale;
        this.scale = amount;
        listeners.firePropertyChange(SCALE_PROPERTY, new Double(oldValue), new Double(scale));
    }
    public void incrementScale(double amount) {
        double oldValue = this.scale;
        this.scale += amount;
        listeners.firePropertyChange(SCALE_PROPERTY, new Double(oldValue), new Double(scale));
    }
    
    /**
     * Returns copy of frame transformed through the given transform
     */
    public Vector<Point3D> getFrame(int frameNum, Matrix3D t) {
        if (t == null) {
            t = new Matrix3D();
        }
        Vector<Point3D> frame = new Vector<Point3D>();
        for (Point3D p : frames.get(frameNum)) {
            frame.add(t.transform(p));
        }
        return frame;
    }
    /**
     * Returns a copy of the selected frames as transformed through the matrix t
     */
    public MarkerSet getFrames(int startFrame, int endFrame, Matrix3D t) {
        Vector<Vector<Point3D>> newFrames = new Vector<Vector<Point3D>>();
        for (int i = startFrame; i < endFrame; i++) {
            newFrames.add(getFrame(i, t));
        }
        return new MarkerSet(newFrames, dimensions);
    }
    /**
     * Creates a copy of the specified frames for placing on clipboard
     */
    public MarkerSet copyRange(int startFrame, int endFrame) {
        startFrame = Math.max(0, startFrame);
        endFrame = Math.min(endFrame, frames.size()-1);
        Matrix3D t = this.getCurrentTransform();
        Vector<Vector<Point3D>> newFrames = new Vector<Vector<Point3D>>();
        for (int i = startFrame; i < endFrame; i++) {
            Vector<Point3D> thisFrame = this.frames.get(i);
            Vector<Point3D> newFrame = new Vector<Point3D>();
            for (Point3D p : thisFrame) {
                newFrame.add(t.transform(p));
            }
            newFrames.add(newFrame);
        }
        return new MarkerSet(newFrames, dimensions);
    }
    /**
     * Cuts a range of frames and returns the cut frames. Used for placing on clipboard or undo stack
     */
    public MarkerSet cutRange(int startFrame, int endFrame) {
        int oldNumFrames = getNumFrames();
        startFrame = Math.max(0, startFrame);
        endFrame = Math.min(endFrame, frames.size());
        Vector<Vector<Point3D>> cutRange = new Vector<Vector<Point3D>>(frames.subList(startFrame, endFrame));
        Vector<Vector<Point3D>> newSet = new Vector<Vector<Point3D>>(frames.subList(0, startFrame));
        newSet.addAll(frames.subList(endFrame, frames.size()));
        frames = newSet;
        if (curFrame >= frames.size()) {
            this.setCurFrame(Math.max(0, frames.size()-1));
        }
        listeners.firePropertyChange(MARKER_SET_PROPERTY, oldNumFrames, getNumFrames());
        return new MarkerSet(cutRange, dimensions);
    }
    /**
     * Pastes range of frames at point specified
     */
    public void pasteRange(int insertionPoint, MarkerSet data) {
        int oldNumFrames = getNumFrames();
        frames.addAll(insertionPoint, data.frames);
        if (oldNumFrames == 0) {
            dimensions = data.dimensions.copy();
        }
        listeners.firePropertyChange(MARKER_SET_PROPERTY, oldNumFrames, getNumFrames());
    }
    /**
     * Creates and returns a mirror image of this marker set. Does not modify marker set
     */
    public MarkerSet getMirrorImage(int startFrame, int endFrame, boolean flipAcrossVertical, boolean flipAcrossHorizontal) {
        Matrix3D t = new Matrix3D();
        if (flipAcrossVertical) {
            t.scale(-1, 1, 1);
        }
        if (flipAcrossHorizontal) {
            t.scale(1, -1, 1);
        }
        return getFrames(startFrame, endFrame, t);
    }



}