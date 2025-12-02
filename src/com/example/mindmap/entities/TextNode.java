package com.example.mindmap.entities;

public class TextNode extends MapElement {
    private String textContent;
    private int fontSize;
    private String shapeType;

    public TextNode() {
    }

    public TextNode(int id, float x, float y, MindMap map,
                    String textContent, int fontSize, String shapeType) {
        super(id, x, y, map);
        this.textContent = textContent;
        this.fontSize = fontSize;
        this.shapeType = shapeType;
    }

    public void editContent(String newText) {
        this.textContent = newText;
    }

    @Override
    public String getType() {
        return "TEXT";
    }

    // гетери/сетери
    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getShapeType() {
        return shapeType;
    }

    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }
}
