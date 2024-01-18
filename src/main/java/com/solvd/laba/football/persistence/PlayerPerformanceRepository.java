package com.solvd.laba.football.persistence;

import com.solvd.laba.football.domain.PlayerPerformance;

import java.util.List;

public interface PlayerPerformanceRepository extends IRepository<PlayerPerformance> {

    List<PlayerPerformance> findByPlayerId(long id);

    List<PlayerPerformance> findByGameId(long id);
}
