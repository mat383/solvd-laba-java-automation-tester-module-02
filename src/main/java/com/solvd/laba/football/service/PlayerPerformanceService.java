package com.solvd.laba.football.service;

import com.solvd.laba.football.domain.PlayerPerformance;

import java.util.List;
import java.util.Optional;

public interface PlayerPerformanceService {
    void create(PlayerPerformance playerPerformance);

    void update(PlayerPerformance playerPerformance);

    /**
     * this will affect other player performances from same game
     * that share goal attempt or penalty shot
     * (it will delete said goal attempt or penalty shot from them)
     *
     * @param playerPerformance
     */
    void delete(PlayerPerformance playerPerformance);

    Optional<PlayerPerformance> findById(long id);

    List<PlayerPerformance> findByPlayerId(long id);

    List<PlayerPerformance> findByGameId(long id);

    List<PlayerPerformance> findAll();
}
