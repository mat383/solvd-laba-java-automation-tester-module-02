package com.solvd.laba.football.service.impl;

import com.solvd.laba.football.domain.GoalAttempt;
import com.solvd.laba.football.persistence.GoalAttemptRepository;
import com.solvd.laba.football.service.GoalAttemptService;

import java.util.List;
import java.util.Optional;

public class GoalAttemptServiceImpl implements GoalAttemptService {
    private final GoalAttemptRepository goalAttemptRepository;

    public GoalAttemptServiceImpl(GoalAttemptRepository goalAttemptRepository) {
        this.goalAttemptRepository = goalAttemptRepository;
    }

    @Override
    public void create(GoalAttempt goalAttempt) {
        this.goalAttemptRepository.create(goalAttempt);
    }

    @Override
    public void update(GoalAttempt goalAttempt) {
        this.goalAttemptRepository.update(goalAttempt);
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
