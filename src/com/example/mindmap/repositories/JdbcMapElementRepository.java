package com.example.mindmap.repositories;

import com.example.mindmap.db.JdbcConnectionProvider;
import com.example.mindmap.entities.ImageNode;
import com.example.mindmap.entities.MapElement;
import com.example.mindmap.entities.TextNode;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcMapElementRepository implements MapElementRepository {

    private final JdbcConnectionProvider connectionProvider;

    public JdbcMapElementRepository(JdbcConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public List<MapElement> findByMapId(int mapId) {
        List<MapElement> result = new ArrayList<>();

        String sql = "SELECT * FROM MapElements WHERE map_id = ?";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, mapId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String type = rs.getString("element_type");
                    float x = rs.getFloat("x_coord");
                    float y = rs.getFloat("y_coord");
                    int id = rs.getInt("id");

                    if ("TEXT".equalsIgnoreCase(type)) {
                        String text = rs.getString("text_content");
                        int fontSize = rs.getInt("font_size");
                        String shapeType = rs.getString("shape_type");

                        TextNode node = new TextNode(id, x, y, null, text, fontSize, shapeType);
                        result.add(node);
                    } else if ("IMAGE".equalsIgnoreCase(type)) {
                        String url = rs.getString("image_url");
                        int width = rs.getInt("width");
                        int height = rs.getInt("height");

                        ImageNode img = new ImageNode(id, x, y, null, url, width, height);
                        result.add(img);
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error loading map elements", e);
        }

        return result;
    }

    @Override
    public void save(MapElement element, int mapId) {
        String sql = "INSERT INTO MapElements (" +
                "map_id, x_coord, y_coord, element_type, " +
                "text_content, font_size, shape_type, " +
                "image_url, width, height) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, mapId);
            ps.setFloat(2, element.getX());
            ps.setFloat(3, element.getY());
            ps.setString(4, element.getType()); // "TEXT" / "IMAGE"

            if ("TEXT".equalsIgnoreCase(element.getType())) {
                TextNode t = (TextNode) element;
                ps.setString(5, t.getTextContent());
                ps.setInt(6, t.getFontSize());
                ps.setString(7, t.getShapeType());
                ps.setNull(8, Types.VARCHAR);
                ps.setNull(9, Types.INTEGER);
                ps.setNull(10, Types.INTEGER);
            } else if ("IMAGE".equalsIgnoreCase(element.getType())) {
                ImageNode img = (ImageNode) element;
                ps.setNull(5, Types.VARCHAR);
                ps.setNull(6, Types.INTEGER);
                ps.setNull(7, Types.VARCHAR);
                ps.setString(8, img.getImageUrl());
                ps.setInt(9, img.getWidthPx());
                ps.setInt(10, img.getHeightPx());
            } else {
                // запасний варіант
                ps.setNull(5, Types.VARCHAR);
                ps.setNull(6, Types.INTEGER);
                ps.setNull(7, Types.VARCHAR);
                ps.setNull(8, Types.VARCHAR);
                ps.setNull(9, Types.INTEGER);
                ps.setNull(10, Types.INTEGER);
            }

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    element.setId(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving map element", e);
        }
    }

    @Override
    public void update(MapElement element) {
        String sql = "UPDATE MapElements SET " +
                "x_coord = ?, y_coord = ?, " +
                "text_content = ?, font_size = ?, shape_type = ?, " +
                "image_url = ?, width = ?, height = ? " +
                "WHERE id = ?";

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if ("TEXT".equalsIgnoreCase(element.getType())) {
                TextNode t = (TextNode) element;
                ps.setFloat(1, element.getX());
                ps.setFloat(2, element.getY());
                ps.setString(3, t.getTextContent());
                ps.setInt(4, t.getFontSize());
                ps.setString(5, t.getShapeType());
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.INTEGER);
                ps.setNull(8, Types.INTEGER);
            } else if ("IMAGE".equalsIgnoreCase(element.getType())) {
                ImageNode img = (ImageNode) element;
                ps.setFloat(1, element.getX());
                ps.setFloat(2, element.getY());
                ps.setNull(3, Types.VARCHAR);
                ps.setNull(4, Types.INTEGER);
                ps.setNull(5, Types.VARCHAR);
                ps.setString(6, img.getImageUrl());
                ps.setInt(7, img.getWidthPx());
                ps.setInt(8, img.getHeightPx());
            } else {
                ps.setFloat(1, element.getX());
                ps.setFloat(2, element.getY());
                ps.setNull(3, Types.VARCHAR);
                ps.setNull(4, Types.INTEGER);
                ps.setNull(5, Types.VARCHAR);
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.INTEGER);
                ps.setNull(8, Types.INTEGER);
            }

            ps.setInt(9, element.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating map element", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM MapElements WHERE id = ?";
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting map element", e);
        }
    }
}
