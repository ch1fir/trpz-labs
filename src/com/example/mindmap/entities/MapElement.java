package com.example.mindmap.entities;

public abstract class MapElement {
    private int id;
    private float x;
    private float y;
    private MindMap map;

    public MapElement() {
    }

    public MapElement(int id, float x, float y, MindMap map) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.map = map;
    }

    public void move(float newX, float newY) {
        this.x = newX;
        this.y = newY;
    }

    public abstract String getType();

    // гетери/сетери
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public MindMap getMap() {
        return map;
    }

    public void setMap(MindMap map) {
        this.map = map;
    }
}
