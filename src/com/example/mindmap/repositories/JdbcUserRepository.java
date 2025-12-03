package com.example.mindmap.repositories;

import com.example.mindmap.db.JdbcConnectionProvider;
import com.example.mindmap.entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcUserRepository implements Repository<User, Integer> {

    private final JdbcConnectionProvider connectionProvider;

    public JdbcUserRepository(JdbcConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public User save(User entity) {
        if (entity.getId() == 0) {
            // INSERT
            String sql = "INSERT INTO Users(username, password_hash) VALUES(?, ?)";
            try (Connection conn = connectionProvider.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, entity.getUsername());
                ps.setString(2, entity.getPasswordHash());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        entity.setId(rs.getInt(1));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            // UPDATE
            String sql = "UPDATE Users SET username = ?, password_hash = ? WHERE id = ?";
            try (Connection conn = connectionProvider.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, entity.getUsername());
                ps.setString(2, entity.getPasswordHash());
                ps.setInt(3, entity.getId());
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return entity;
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Users WHERE id = ?";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> getById(Integer id) {
        String sql = "SELECT id, username, password_hash FROM Users WHERE id = ?";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password_hash")
                    );
                    return Optional.of(u);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT id, username, password_hash FROM Users";
        List<User> users = new ArrayList<>();
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password_hash")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public Optional<User> getByUsername(String username) {
        String sql = "SELECT id, username, password_hash FROM Users WHERE username = ?";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password_hash")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
