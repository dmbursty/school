package a3.util;


import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import a3.MarkerRenderer;
import a3.MarkerSet;

public class MarkerPreviewRenderer {
    public static Image getPreview(MarkerSet markers, MarkerRenderer renderer, Dimension previewSize) {
        BufferedImage image = new BufferedImage(previewSize.width, previewSize.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        Rectangle destArea = new Rectangle(0, 0, previewSize.width, previewSize.height);
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
        if (markers.getNumFrames() > 0) {
            renderer.renderMarkers((Graphics2D)g, markers.getFrame(0, t), destArea);
        }
        return image;
    }
}
