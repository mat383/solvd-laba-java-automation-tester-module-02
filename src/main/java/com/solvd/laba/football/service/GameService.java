package com.solvd.laba.football.service;

import com.solvd.laba.football.domain.Game;

import java.util.List;
import java.util.Optional;

public interface GameService {
    void create(Game game);

    void update(Game game);

    void delete(Game game);

    Optional<Game> findById(long id);

    List<Game> findAll();
}
