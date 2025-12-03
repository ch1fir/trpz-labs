package com.example.mindmap.services.commands;

public interface Command {
    void execute();
    void undo();
}
