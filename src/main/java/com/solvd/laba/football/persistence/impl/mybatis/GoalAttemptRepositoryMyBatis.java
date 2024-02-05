package com.solvd.laba.football.persistence.impl.mybatis;

import com.solvd.laba.football.domain.GoalAttempt;
import com.solvd.laba.football.persistence.GoalAttemptRepository;
import com.solvd.laba.football.persistence.impl.mybatis.util.MyBatisQueryHelper;

import java.util.List;
import java.util.Optional;

public class GoalAttemptRepositoryMyBatis implements GoalAttemptRepository {
    private final MyBatisQueryHelper<GoalAttempt, GoalAttemptRepository> queryHelper =
            new MyBatisQueryHelper<>(GoalAttemptRepository.class);

    @Override
    public void create(GoalAttempt goalAttempt, long defenderPerformanceId, long attackerPerformanceId) {
        this.queryHelper.executeUpdate(repository -> {
            repository.create(goalAttempt, defenderPerformanceId, attackerPerformanceId);
        });
    }

    @Override
    public void update(GoalAttempt goalAttempt, long defenderPerformanceId, long attackerPerformanceId) {
        this.queryHelper.executeUpdate(repository -> {
            repository.update(goalAttempt, defenderPerformanceId, attackerPerformanceId);
        });
    }

    @Override
    public void delete(GoalAttempt goalAttempt) {
        this.queryHelper.executeUpdate(repository -> {
            repository.delete(goalAttempt);
        });
    }

    @Override
    public Optional<GoalAttempt> findById(long id) {
        return this.queryHelper.executeSingleRowQuery(repository -> {
            return repository.findById(id);
        });
    }

    @Override
    public Optional<Long> findDefenderPerformanceIdByGoalAttemptId(long id) {
        return this.queryHelper.executeSingleRowQueryGenericReturn(repository -> {
            return repository.findDefenderPerformanceIdByGoalAttemptId(id);
        });
    }

    @Override
    public Optional<Long> findAttackerPerformanceIdByGoalAttemptId(long id) {
        return this.queryHelper.executeSingleRowQueryGenericReturn(repository -> {
            return repository.findAttackerPerformanceIdByGoalAttemptId(id);
        });
    }

    @Override
    public List<GoalAttempt> findAll() {
        return this.queryHelper.executeMultiRowQuery(GoalAttemptRepository::findAll);
    }

    @Override
    public List<GoalAttempt> findByDefenderPerformanceId(long id) {
        return this.queryHelper.executeMultiRowQuery(repository -> {
            return repository.findByDefenderPerformanceId(id);
        });
    }

    @Override
    public List<GoalAttempt> findByAttackerPerformanceId(long id) {
        return this.queryHelper.executeMultiRowQuery(repository -> {
            return repository.findByAttackerPerformanceId(id);
        });
    }

    @Override
    public List<GoalAttempt> findByRelatedPerformanceId(long id) {
        return this.queryHelper.executeMultiRowQuery(repository -> {
            return repository.findByRelatedPerformanceId(id);
        });
    }
}
