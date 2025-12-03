package com.example.mindmap.export;

import com.example.mindmap.entities.MindMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PngExportStrategy implements ExportStrategy {

    @Override
    public void export(MindMap map, JComponent canvas, File targetFile) throws IOException {
        int w = canvas.getWidth();
        int h = canvas.getHeight();

        if (w <= 0 || h <= 0) {
            throw new IOException("Canvas has invalid size");
        }

        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        canvas.printAll(g2);
        g2.dispose();

        ImageIO.write(image, "png", targetFile);
    }
}
