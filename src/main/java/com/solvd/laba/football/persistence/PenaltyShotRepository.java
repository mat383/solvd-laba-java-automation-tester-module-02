package com.solvd.laba.football.persistence;

import com.solvd.laba.football.domain.PenaltyShot;

import java.util.List;

public interface PenaltyShotRepository extends IRepository<PenaltyShot> {

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
