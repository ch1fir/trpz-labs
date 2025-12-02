package com.example.mindmap.repositories;

import com.example.mindmap.entities.MindMap;
import com.example.mindmap.entities.User;

import java.util.*;
import java.util.stream.Collectors;

public class MindMapRepository implements Repository<MindMap, Integer> {

    private final Map<Integer, MindMap> storage = new HashMap<>();
    private int nextId = 1;

    @Override
    public MindMap save(MindMap entity) {
        if (entity.getId() == 0) {
            entity.setId(nextId++);
        }
        storage.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public void delete(Integer id) {
        storage.remove(id);
    }

    @Override
    public Optional<MindMap> getById(Integer id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<MindMap> getAll() {
        return new ArrayList<>(storage.values());
    }

    public List<MindMap> getAllByUser(User user) {
        return storage.values().stream()
                .filter(map -> map.getOwner() != null && map.getOwner().equals(user))
                .collect(Collectors.toList());
    }

    public List<MindMap> getFavorites(User user) {
        return storage.values().stream()
                .filter(map -> map.getOwner() != null
                        && map.getOwner().equals(user)
                        && map.isFavorite())
                .collect(Collectors.toList());
    }
}
