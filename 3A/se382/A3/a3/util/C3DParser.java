package a3.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Vector;

import a3.Point3D;

/**
 * Rudimentary c3d file parser for CS 349 / SE 382.
 * Java code written by Michael Terry from C sample code provided
 * on www.c3d.org website written by Bruce A. MacWilliams.
 */

public class C3DParser {

    private static float decToFloat(byte[] bytes) throws IOException {
        byte[] p = new byte[4];
        p[0] = bytes[2];
        p[1] = bytes[3];
        p[2] = bytes[0];
        p[3] = bytes[1];
        if (p[0] != 0 || p[1] != 0 || p[2] != 0 || p[3] != 0) {
            --p[3];
        }
        ByteBuffer bb = ByteBuffer.wrap(p);
        bb.order(ByteOrder.LITTLE_ENDIAN);     
        return bb.getFloat();
    }
    private static int readUnsignedShort(RandomAccessFile is) throws IOException {
        byte[] buf = new byte[2];
        is.read(buf, 0, 2);
        ByteBuffer bb = ByteBuffer.wrap(buf);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getShort();
    }
    private static int readShort(RandomAccessFile is) throws IOException {
        byte[] buf = new byte[2];
        is.read(buf, 0, 2);
        ByteBuffer bb = ByteBuffer.wrap(buf);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getShort();
    }
    private static float readDECFloat(RandomAccessFile is) throws IOException {
        byte[] buf = new byte[4];
        is.read(buf, 0, 4);
        return decToFloat(buf);
    }
    private static float readFloat(RandomAccessFile is) throws IOException {
        byte[] buf = new byte[4];
        is.read(buf, 0, 4);
        ByteBuffer bb = ByteBuffer.wrap(buf);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getFloat();
    }

    /**
     * Reads in a C3D file.
     * @param file The C3D file to open
     * @param subSample If true, will sub-sample the mocap data, choosing only every <em>sampleFactor</em> frames
     * @param sampleFactor The amount to sub-sample if subSample is true. Only every <em>sampleFactor</em> frames will be read in
     * @param includeVolumePoints If true, the last frame will consist of only two points: The first point is the min x/y/z coordinate. The second point is the width, height, and depth of the volume.
     */
    public static Vector<Vector<Point3D>> parseFile(File file, boolean subSample, int sampleFactor, boolean includeVolumePoints) throws IOException
    {
        Vector<Vector<Point3D>> frames = new Vector<Vector<Point3D>>();
        
        RandomAccessFile infile = new RandomAccessFile(file, "r");
        @SuppressWarnings("unused")
        int key = readUnsignedShort(infile); // Reads key. Not used
        int numMarkers = readUnsignedShort(infile);
        int numChannels = readUnsignedShort(infile);
        int firstFrame = readUnsignedShort(infile);
        int lastFrame = readUnsignedShort(infile);
        @SuppressWarnings("unused")
        int maxGap = readUnsignedShort(infile);
        float scaleFactor = readDECFloat(infile);
        int dataStart = readUnsignedShort(infile);
        int framesPerField = readUnsignedShort(infile);
        if (framesPerField != 0) {
            numChannels /= framesPerField;
        }
        byte[] buf = new byte[4];
        infile.read(buf, 0, 2);
        buf[2] = buf[3] = 0;
        @SuppressWarnings("unused")
        float videoRate = decToFloat(buf);
        
        int numFrames = lastFrame - firstFrame + 1;        

        infile.seek((dataStart - 1)* 512);
     
        double minx, miny, minz,
               maxx, maxy, maxz;
        minx = miny = minz = maxx = maxy = maxz = 0;       
        boolean haveFirst = false;
        for (int frameNum = 0; frameNum < numFrames; frameNum++) {
            Vector<Point3D> frame = new Vector<Point3D>();
            for (int j = 0; j < numMarkers; j++) {
                float x = 0;
                float y = 0;
                float z = 0;
                if (scaleFactor < 0) {
                    x = readFloat(infile);
                    z = readFloat(infile);
                    y = readFloat(infile);
                    infile.read(buf, 0, 4); // ignored
                } else {
                    x = readShort(infile) * scaleFactor;
                    z = readShort(infile) * scaleFactor;
                    y = readShort(infile) * scaleFactor;
                    infile.read(buf, 0, 2); // ignored
                }
                if (x != 0 || y != 0 || z != 0) {
                    y = y * -1;
                    if (!haveFirst) {
                        minx = maxx = x;
                        miny = maxy = y;
                        minz = maxz = z;
                        haveFirst = true;
                    } else {
                        minx = Math.min(minx, x);
                        maxx = Math.max(maxx, x);
                        miny = Math.min(miny, y);
                        maxy = Math.max(maxy, y);
                        minz = Math.min(minz, z);
                        maxz = Math.max(maxz, z);
                    }
                    frame.add(new Point3D(x, y, z));
                }
            }
            for (int sample = 1; sample <= framesPerField; sample++) {
                for (int channel = 1; channel <= numChannels; channel++) {
                    infile.read(buf, 0, 2); // ignored
                }
            }
            frames.add(frame);
            if (subSample) {
                if ((frameNum+(sampleFactor-1)) < numFrames) {
                    int numFramesToAdvance = sampleFactor - 1;
                    int markerSize = 0;
                    long bytesPerFrame = 0;
                    if (scaleFactor < 0) {
                        markerSize = 3 * 4 + 4; // 4 bytes for each float, three floats, plus the residual at the end
                    } else {
                        markerSize = 3 * 2 + 2; // 2 bytes for each short, three shorts, plus the residual at the end
                    }
                    long otherStuff = 0;
                    otherStuff = framesPerField * numChannels * 2;
                    bytesPerFrame = markerSize * numMarkers + otherStuff;
                    infile.seek(infile.getFilePointer() + bytesPerFrame * numFramesToAdvance);
                }
                frameNum += (sampleFactor - 1);
            }
        }
        infile.close();
        
        if (includeVolumePoints) {
            double width = maxx - minx;
            double height = maxy - miny;
            double depth = maxz - minz;
            Vector<Point3D> dimensionFrame = new Vector<Point3D>();
            Point3D minPoint = new Point3D(minx, miny, minz);
            Point3D dimensionPoint = new Point3D(width, height, depth);
            dimensionFrame.add(minPoint);
            dimensionFrame.add(dimensionPoint);
            frames.add(dimensionFrame);
        }
        return frames;
    }
}
