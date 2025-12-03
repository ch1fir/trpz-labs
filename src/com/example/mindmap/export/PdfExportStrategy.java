package com.example.mindmap.export;

import com.example.mindmap.entities.MindMap;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PdfExportStrategy implements ExportStrategy {

    @Override
    public void export(MindMap map, JComponent canvas, File targetFile) throws IOException {
        // Спрощена реалізація: текстовий псевдо-PDF.
        // Для реального PDF потрібна окрема бібліотека.
        try (FileWriter writer = new FileWriter(targetFile)) {
            writer.write("Mind Map Export (pseudo PDF)\n");
            writer.write("Title: " + map.getTitle() + "\n");
            writer.write("Canvas size: " + canvas.getWidth() + "x" + canvas.getHeight() + "\n");
            writer.write("Note: For real PDF export a PDF library should be used.\n");
        }
    }
}
