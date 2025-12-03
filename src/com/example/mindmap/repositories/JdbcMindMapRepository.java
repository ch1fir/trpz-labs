package com.example.mindmap.repositories;

import com.example.mindmap.db.JdbcConnectionProvider;
import com.example.mindmap.entities.MindMap;
import com.example.mindmap.entities.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMindMapRepository implements Repository<MindMap, Integer> {

    private final JdbcConnectionProvider connectionProvider;

    public JdbcMindMapRepository(JdbcConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public MindMap save(MindMap entity) {
        if (entity.getId() == 0) {
            String sql = "INSERT INTO MindMaps(title, created_at, is_favorite, preview_image, user_id) " +
                    "VALUES(?, ?, ?, ?, ?)";
            try (Connection conn = connectionProvider.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, entity.getTitle());
                ps.setTimestamp(2, Timestamp.valueOf(entity.getCreatedAt()));
                ps.setBoolean(3, entity.isFavorite());
                ps.setString(4, entity.getPreviewImage());
                ps.setInt(5, entity.getOwner().getId());
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
            String sql = "UPDATE MindMaps SET title = ?, is_favorite = ?, preview_image = ? WHERE id = ?";
            try (Connection conn = connectionProvider.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, entity.getTitle());
                ps.setBoolean(2, entity.isFavorite());
                ps.setString(3, entity.getPreviewImage());
                ps.setInt(4, entity.getId());
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return entity;
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM MindMaps WHERE id = ?";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<MindMap> getById(Integer id) {
        String sql = "SELECT id, title, created_at, is_favorite, preview_image, user_id FROM MindMaps WHERE id = ?";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MindMap map = new MindMap();
                    map.setId(rs.getInt("id"));
                    map.setTitle(rs.getString("title"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    map.setOwner(new User(rs.getInt("user_id"), null, null));
                    if (ts != null) {
                        // createdAt ми ставили в конструктор, але тут можемо перезаписати
                        // (потрібен сеттер, який ми вже додали)
                        // в нашому класі createdAt тільки геттер, можна додати сеттер або пропустити.
                    }
                    if (rs.getBoolean("is_favorite")) {
                        map.markAsFavorite();
                    }
                    map.setPreviewImage(rs.getString("preview_image"));
                    return Optional.of(map);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<MindMap> getAll() {
        throw new UnsupportedOperationException("Not needed yet");
    }

    public List<MindMap> getAllByUser(User user) {
        String sql = "SELECT id, title, created_at, is_favorite, preview_image " +
                "FROM MindMaps WHERE user_id = ?";
        List<MindMap> maps = new ArrayList<>();
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, user.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MindMap map = new MindMap();
                    map.setId(rs.getInt("id"));
                    map.setTitle(rs.getString("title"));
                    map.setOwner(user);
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) {
                        // можна проігнорити або додати сеттер для createdAt
                    }
                    if (rs.getBoolean("is_favorite")) {
                        map.markAsFavorite();
                    }
                    map.setPreviewImage(rs.getString("preview_image"));
                    maps.add(map);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return maps;
    }
}
