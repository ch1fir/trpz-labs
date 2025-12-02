package com.example.mindmap.entities;

public class Category {
    private int id;
    private String title;
    private String color; // HEX
    private User user;    // власник категорії

    public Category() {
    }

    public Category(int id, String title, String color, User user) {
        this.id = id;
        this.title = title;
        this.color = color;
        this.user = user;
    }

    // гетери/сетери
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
