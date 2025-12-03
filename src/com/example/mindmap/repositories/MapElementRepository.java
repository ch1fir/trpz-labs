package com.example.mindmap.repositories;

import com.example.mindmap.entities.MapElement;

import java.util.List;

public interface MapElementRepository {

    List<MapElement> findByMapId(int mapId);

    void save(MapElement element, int mapId);  // INSERT

    void update(MapElement element);           // UPDATE

    void delete(int id);
}
