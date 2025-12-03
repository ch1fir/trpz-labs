package com.example.mindmap.services.commands;

import com.example.mindmap.entities.MindMap;
import com.example.mindmap.entities.MapElement;
import com.example.mindmap.services.MindMapService;

import java.util.List;

public class AddNodeCommand implements Command {

    private final MindMap mindMap;
    private final MindMapService mindMapService;
    private final List<MapElement> elements; // список CanvasPanel
    private final float x, y;
    private final String text;

    private MapElement createdElement;

    public AddNodeCommand(
            MindMap mindMap,
            MindMapService mindMapService,
            List<MapElement> elements,
            float x,
            float y,
            String text
    ) {
        this.mindMap = mindMap;
        this.mindMapService = mindMapService;
        this.elements = elements;
        this.x = x;
        this.y = y;
        this.text = text;
    }

    @Override
    public void execute() {
        createdElement = mindMapService.addTextNode(mindMap, x, y, text);
        elements.add(createdElement);
    }

    @Override
    public void undo() {
        elements.remove(createdElement);
        mindMapService.deleteElement(createdElement);
    }
}
