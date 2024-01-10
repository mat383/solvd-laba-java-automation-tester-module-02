package com.solvd.laba.football.persistence;

import com.solvd.laba.football.domain.GoalAttempt;

import java.util.List;

public interface GoalAttemptRepository extends IRepository<GoalAttempt> {

    List<GoalAttempt> findByDefenderPerformanceId(long id);

    List<GoalAttempt> findByAttackerPerformanceId(long id);

    /**
     * find GoalAttempts where either attacker id or defender id
     * equals to id
     *
     * @param id
     * @return
     */
    List<GoalAttempt> findByRelatedPerformanceId(long id);
}
