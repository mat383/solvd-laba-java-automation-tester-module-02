package com.solvd.laba.football.service;

import com.solvd.laba.football.domain.GoalAttempt;

import java.util.List;
import java.util.Optional;

public interface GoalAttemptService {
    void create(GoalAttempt goalAttempt);

    void update(GoalAttempt goalAttempt);

    void delete(GoalAttempt goalAttempt);

    /**
     * finds GoalAttempt with specified id,
     * this will return GoalAttempt, but PlayerPerformances
     * that are inside will not be fully filled, and will be
     * containing only id
     *
     * @param id
     * @return
     */
    Optional<GoalAttempt> findById(long id);

    /**
     * finds GoalAttempt with specified defender performance id,
     * this will return list of GoalAttempt, but PlayerPerformances
     * that are inside will not be fully filled, and will be
     * containing only id
     *
     * @param id
     * @return
     */
    List<GoalAttempt> findByDefenderPerformanceId(long id);

    /**
     * finds GoalAttempt with specified attacker performance id,
     * this will return list of GoalAttempt, but PlayerPerformances
     * that are inside will not be fully filled, and will be
     * containing only id
     *
     * @param id
     * @return
     */
    List<GoalAttempt> findByAttackerPerformanceId(long id);

    /**
     * find GoalAttempts where either attacker id or defender id
     * equals to id
     * this will return list of GoalAttempt, but PlayerPerformances
     * that are inside will not be fully filled, and will be
     * containing only id
     *
     * @param id
     * @return
     */
    List<GoalAttempt> findByRelatedPerformanceId(long id);

    List<GoalAttempt> findAll();
}
