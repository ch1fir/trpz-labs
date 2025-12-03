package com.example.mindmap.export;

import com.example.mindmap.entities.MindMap;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public interface ExportStrategy {
    void export(MindMap map, JComponent canvas, File targetFile) throws IOException;
}
