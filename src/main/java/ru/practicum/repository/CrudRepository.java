package ru.practicum.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, E> {
    T save(T t);

    T save(T t, E id);

    boolean delete(E id);

    Optional<T> findById(E id);

    List<T> findAll();
}
