package com.example.mindmap.services.commands;

import com.example.mindmap.entities.MapElement;
import com.example.mindmap.services.MindMapService;

public class MoveNodeCommand implements Command {

    private final MapElement element;
    private final float oldX, oldY;
    private final float newX, newY;
    private final MindMapService mindMapService;

    public MoveNodeCommand(
            MapElement element,
            float oldX,
            float oldY,
            float newX,
            float newY,
            MindMapService mindMapService
    ) {
        this.element = element;
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
        this.mindMapService = mindMapService;
    }

    @Override
    public void execute() {
        element.setX(newX);
        element.setY(newY);
        mindMapService.updateElement(element);
    }

    @Override
    public void undo() {
        element.setX(oldX);
        element.setY(oldY);
        mindMapService.updateElement(element);
    }
}
