package com.example.mindmap.repositories;

import com.example.mindmap.entities.User;

import java.util.*;

public class UserRepository implements Repository<User, Integer> {

    private final Map<Integer, User> storage = new HashMap<>();
    private int nextId = 1;

    @Override
    public User save(User entity) {
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
    public Optional<User> getById(Integer id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<User> getByUsername(String username) {
        return storage.values().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }
}
