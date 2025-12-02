package com.example.mindmap.repositories;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    T save(T entity);
    void delete(ID id);
    Optional<T> getById(ID id);
    List<T> getAll();
}
