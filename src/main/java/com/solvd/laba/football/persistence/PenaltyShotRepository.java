package com.solvd.laba.football.persistence;

import com.solvd.laba.football.domain.PenaltyShot;

import java.util.List;
import java.util.Optional;

public interface PenaltyShotRepository {

    void create(PenaltyShot penaltyShot, long goalkeeperPerformanceId, long shooterPerformanceId);

    void update(PenaltyShot penaltyShot, long goalkeeperPerformanceId, long shooterPerformanceId);

    void delete(PenaltyShot penaltyShot);

    Optional<PenaltyShot> findById(long id);

    List<PenaltyShot> findAll();

    List<PenaltyShot> findByGoalkeeperPerformanceId(long id);

    List<PenaltyShot> findByShooterPerformanceId(long id);

    /**
     * find GoalAttempts where either attacker id or goalkeeper id
     * equals to id
     *
     * @param id
     * @return
     */
    List<PenaltyShot> findByRelatedPerformanceId(long id);
}
