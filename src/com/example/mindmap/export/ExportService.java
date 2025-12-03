package com.example.mindmap.export;

import com.example.mindmap.entities.MindMap;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class ExportService {

    private ExportStrategy strategy;

    public void setStrategy(ExportStrategy strategy) {
        this.strategy = strategy;
    }

    public void export(MindMap map, JComponent canvas, File targetFile) throws IOException {
        if (strategy == null) {
            throw new IllegalStateException("Export strategy is not set");
        }
        strategy.export(map, canvas, targetFile);
    }
}
