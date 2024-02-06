package com.solvd.laba.football.service.impl.jdbc;

import com.solvd.laba.football.domain.GoalAttempt;
import com.solvd.laba.football.persistence.GoalAttemptRepository;
import com.solvd.laba.football.service.GoalAttemptService;

import java.util.List;
import java.util.Optional;

public class GoalAttemptServiceJdbc implements GoalAttemptService {
    private final GoalAttemptRepository goalAttemptRepository;

    public GoalAttemptServiceJdbc(GoalAttemptRepository goalAttemptRepository) {
        this.goalAttemptRepository = goalAttemptRepository;
    }


    @Override
    public void create(GoalAttempt goalAttempt, long defenderPerformanceId, long attackerPerformanceId) {
        this.goalAttemptRepository.create(goalAttempt, defenderPerformanceId, attackerPerformanceId);
    }

    @Override
    public void update(GoalAttempt goalAttempt, long defenderPerformanceId, long attackerPerformanceId) {
        this.goalAttemptRepository.update(goalAttempt, defenderPerformanceId, attackerPerformanceId);
    }

    @Override
    public void delete(GoalAttempt goalAttempt) {
        this.goalAttemptRepository.delete(goalAttempt);
    }

    @Override
    public Optional<GoalAttempt> findById(long id) {
        return this.goalAttemptRepository.findById(id);
    }

    @Override
    public Optional<Long> findDefenderPerformanceIdByGoalAttemptId(long id) {
        return this.goalAttemptRepository.findDefenderPerformanceIdByGoalAttemptId(id);
    }

    @Override
    public Optional<Long> findAttackerPerformanceIdByGoalAttemptId(long id) {
        return this.goalAttemptRepository.findAttackerPerformanceIdByGoalAttemptId(id);
    }

    @Override
    public List<GoalAttempt> findByDefenderPerformanceId(long id) {
        return this.goalAttemptRepository.findByDefenderPerformanceId(id);
    }

    @Override
    public List<GoalAttempt> findByAttackerPerformanceId(long id) {
        return this.goalAttemptRepository.findByAttackerPerformanceId(id);
    }

    @Override
    public List<GoalAttempt> findByRelatedPerformanceId(long id) {
        return this.goalAttemptRepository.findByRelatedPerformanceId(id);
    }

    @Override
    public List<GoalAttempt> findAll() {
        return this.goalAttemptRepository.findAll();
    }
}
