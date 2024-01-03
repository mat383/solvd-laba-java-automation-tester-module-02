package com.solvd.laba.football.persistence;

import java.util.List;
import java.util.Optional;

public interface IRepository<T> {
    void create(T object);

    void update(T object);

    void delete(T object);

    Optional<T> findById(long id);

    List<T> findAll();
}
