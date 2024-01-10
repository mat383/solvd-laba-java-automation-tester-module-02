package com.solvd.laba.football.persistence;

import com.solvd.laba.football.domain.PlayerPerformance;

import java.util.List;
import java.util.Optional;

public interface PlayerPerformanceRepository {

    void create(PlayerPerformance playerPerformance, long playerId);

    void update(PlayerPerformance playerPerformance, long playerId);

    void delete(PlayerPerformance playerPerformance);

    Optional<PlayerPerformance> findById(long id);

    List<PlayerPerformance> findByPlayerId(long id);

    List<PlayerPerformance> findByGameId(long id);

    List<PlayerPerformance> findAll();
}
