package com.example.mindmap.entities;

public class TextNode extends MapElement {

    private String textContent;
    private int fontSize;
    private String shapeType;

    public TextNode() {
        super(0, 0, 0, null);
    }

    public TextNode(int id, float x, float y, MindMap map,
                    String textContent, int fontSize, String shapeType) {
        super(id, x, y, map);
        this.textContent = textContent;
        this.fontSize = fontSize;
        this.shapeType = shapeType;
    }

    // 🔹 Додатковий конструктор — зручно для редактора
    public TextNode(float x, float y, String textContent) {
        super(0, x, y, null);      // map поки що null, потім можемо проставляти
        this.textContent = textContent;
        this.fontSize = 14;
        this.shapeType = "RECT";
    }

    public void editContent(String newText) {
        this.textContent = newText;
    }

    @Override
    public String getType() {
        return "TEXT";
    }

    // 🔹 Реалізація "універсального" тексту для Canvas
    @Override
    public String getTextForDisplay() {
        return textContent;
    }

    @Override
    public void setTextForDisplay(String text) {
        this.textContent = text;
    }

    // --- Старі гетери/сетери можна залишити ---

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
