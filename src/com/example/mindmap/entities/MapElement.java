package com.example.mindmap.entities;

public abstract class MapElement {

    protected int id;
    protected float x;
    protected float y;
    protected MindMap map;

    public MapElement(int id, float x, float y, MindMap map) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.map = map;
    }

    // Тип елемента: "TEXT", "IMAGE" і т.д.
    public abstract String getType();

    // 🔹 Універсальний текст для відображення на Canvas
    public abstract String getTextForDisplay();
    public abstract void setTextForDisplay(String text);

    // --- Гетери / сетери ---

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
