package com.example.mindmap.entities;

public class ImageNode extends MapElement {
    private String imageUrl;
    private int width;
    private int height;

    public ImageNode() {
    }

    public ImageNode(int id, float x, float y, MindMap map,
                     String imageUrl, int width, int height) {
        super(id, x, y, map);
        this.imageUrl = imageUrl;
        this.width = width;
        this.height = height;
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public String getType() {
        return "IMAGE";
    }

    // гетери/сетери
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
