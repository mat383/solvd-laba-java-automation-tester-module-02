package com.solvd.laba.football.persistence;

import com.solvd.laba.football.domain.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository {
    void create(Player player, long teamId);

    void update(Player player, long teamId);

    void delete(Player player);

    Optional<Player> findById(long id);

    List<Player> findAll();

    List<Player> findByTeamId(long teamId);
}
