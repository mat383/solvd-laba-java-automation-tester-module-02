package com.solvd.laba.football.service;

import com.solvd.laba.football.domain.Position;

import java.util.List;
import java.util.Optional;

public interface PositionService {
    void create(Position position);

    void update(Position position);

    void delete(Position position);

    Optional<Position> findById(long id);

    List<Position> findAll();
}
