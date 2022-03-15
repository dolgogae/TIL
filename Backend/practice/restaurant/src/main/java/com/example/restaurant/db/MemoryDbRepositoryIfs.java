package com.example.restaurant.db;

import java.util.List;
import java.util.Optional;

public interface MemoryDbRepositoryIfs<T> {
    public Optional<T> findById(int index);
    public T save(T entity);
    public void deleteById(int index);
    public List<T> findAll();
}
