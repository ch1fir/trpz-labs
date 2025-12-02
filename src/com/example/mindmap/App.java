package com.example.mindmap;

import com.example.mindmap.entities.*;
import com.example.mindmap.repositories.*;

public class App {
    public static void main(String[] args) {
        UserRepository userRepo = new UserRepository();
        MindMapRepository mapRepo = new MindMapRepository();

        // Створюємо користувача
        User user = new User(0, "student", "hash123");
        userRepo.save(user);

        // Створюємо мапу
        MindMap map = new MindMap(0, "План курсової", user);
        mapRepo.save(map);

        // Додаємо елементи
        TextNode title = new TextNode(0, 100, 100, map,
                "Mind-mapping software", 18, "RECTANGLE");
        map.addElement(title);

        ImageNode img = new ImageNode(0, 200, 150, map,
                "/images/diagram.png", 320, 240);
        map.addElement(img);

        // Позначаємо як обрану
        map.markAsFavorite();

        System.out.println("Користувач: " + user.getUsername());
        System.out.println("Мапа: " + map.getTitle());
        System.out.println("Елементів на мапі: " + map.getElements().size());
    }
}
