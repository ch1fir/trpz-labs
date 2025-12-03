package com.example.mindmap.services.commands;

import com.example.mindmap.entities.MapElement;
import com.example.mindmap.entities.MindMap;
import com.example.mindmap.services.MindMapService;

import java.util.List;

public class DeleteNodeCommand implements Command {

    private final MindMap mindMap;
    private final MindMapService mindMapService;
    private final List<MapElement> elements;

    private final MapElement target;
    private float x, y;
    private String text;

    public DeleteNodeCommand(
            MindMap mindMap,
            MindMapService mindMapService,
            List<MapElement> elements,
            MapElement target
    ) {
        this.mindMap = mindMap;
        this.mindMapService = mindMapService;
        this.elements = elements;
        this.target = target;
    }

    @Override
    public void execute() {
        // Збережемо дані для undo
        x = target.getX();
        y = target.getY();
        text = target.getTextForDisplay();

        elements.remove(target);
        mindMapService.deleteElement(target);
    }

    @Override
    public void undo() {
        MapElement restored = mindMapService.addTextNode(mindMap, x, y, text);
        elements.add(restored);
    }
}
