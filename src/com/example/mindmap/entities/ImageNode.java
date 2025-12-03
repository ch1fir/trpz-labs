package com.example.mindmap.entities;

public class ImageNode extends MapElement {

    private String imageUrl;
    private int width;
    private int height;

    public ImageNode() {
        super(0, 0, 0, null);
    }

    public ImageNode(int id, float x, float y, MindMap map,
                     String imageUrl, int width, int height) {
        super(id, x, y, map);
        this.imageUrl = imageUrl;
        this.width = width;
        this.height = height;
    }

    // 🔹 Додатковий конструктор для редактора
    public ImageNode(float x, float y, String imageUrl) {
        super(0, x, y, null);
        this.imageUrl = imageUrl;
        this.width = 120;
        this.height = 80;
    }

    @Override
    public String getType() {
        return "IMAGE";
    }

    // 🔥 Реалізуємо абстрактні методи (ImageNode не має тексту)
    @Override
    public String getTextForDisplay() {
        return null; // у зображення нема тексту
    }

    @Override
    public void setTextForDisplay(String text) {
        // нічого не робимо
    }

    // --- getters / setters ---

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getWidthPx() {
        return width;
    }

    public void setWidthPx(int width) {
        this.width = width;
    }

    public int getHeightPx() {
        return height;
    }

    public void setHeightPx(int height) {
        this.height = height;
    }
}
