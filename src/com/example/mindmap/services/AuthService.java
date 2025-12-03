package com.example.mindmap.services;

import com.example.mindmap.entities.User;
import com.example.mindmap.repositories.JdbcUserRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class AuthService {

    private final JdbcUserRepository userRepository;

    public AuthService(JdbcUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ЛОГІН
    public Optional<User> login(String username, String password) {
        String hashed = hashPassword(password);

        return userRepository.getByUsername(username)
                .filter(u -> u.getPasswordHash().equals(hashed));
    }

    // РЕЄСТРАЦІЯ
    public User register(String username, String password) {
        String hashed = hashPassword(password);          // тут хешуємо
        User user = new User(0, username, hashed);      // зберігаємо вже хеш
        return userRepository.save(user);
    }

    // ПРИВАТНИЙ МЕТОД ДЛЯ ХЕШУВАННЯ
    private String hashPassword(String plainText) {
        if (plainText == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(plainText.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b)); // перетворюємо байти в HEX-рядок
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }
    }
}
