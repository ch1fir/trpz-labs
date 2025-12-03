package com.example.mindmap;

import com.example.mindmap.db.JdbcConnectionProvider;
import com.example.mindmap.repositories.JdbcMapElementRepository;
import com.example.mindmap.repositories.JdbcMindMapRepository;
import com.example.mindmap.repositories.JdbcUserRepository;
import com.example.mindmap.services.AuthService;
import com.example.mindmap.services.MindMapService;
import com.example.mindmap.ui.LoginForm;

import javax.swing.*;

public class App {
    public static void main(String[] args) {

        // Системний стиль вигляду
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception ignored) {}

        // Підключення до БД
        JdbcConnectionProvider connectionProvider = new JdbcConnectionProvider();

        // Репозиторії
        JdbcUserRepository userRepo = new JdbcUserRepository(connectionProvider);
        JdbcMindMapRepository mapRepo = new JdbcMindMapRepository(connectionProvider);
        JdbcMapElementRepository elementRepo = new JdbcMapElementRepository(connectionProvider);

        // Сервіси
        AuthService authService = new AuthService(userRepo);
        MindMapService mindMapService = new MindMapService(mapRepo, elementRepo);

        // Стартове вікно (логін/реєстрація)
        SwingUtilities.invokeLater(() ->
                new LoginForm(authService, mindMapService).setVisible(true)
        );
    }
}
