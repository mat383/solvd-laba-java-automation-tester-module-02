package com.solvd.laba.football.persistence;

import com.solvd.laba.football.domain.GoalAttempt;

import java.util.List;
import java.util.Optional;

public interface GoalAttemptRepository {

    void create(GoalAttempt goalAttempt, long defenderPerformanceId, long attackerPerformanceId);

    void update(GoalAttempt goalAttempt, long defenderPerformanceId, long attackerPerformanceId);

    void delete(GoalAttempt goalAttempt);

    Optional<GoalAttempt> findById(long id);

    Optional<Long> findDefenderPerformanceIdByGoalAttemptId(long id);

    Optional<Long> findAttackerPerformanceIdByGoalAttemptId(long id);

    List<GoalAttempt> findAll();

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
