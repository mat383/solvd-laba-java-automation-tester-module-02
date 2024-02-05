package com.solvd.laba.football.persistence.impl.mybatis;

import com.solvd.laba.football.domain.PenaltyShot;
import com.solvd.laba.football.persistence.PenaltyShotRepository;
import com.solvd.laba.football.persistence.impl.mybatis.util.MyBatisQueryHelper;

import java.util.List;
import java.util.Optional;

public class PenaltyShotRepositoryMyBatis implements PenaltyShotRepository {
    private final MyBatisQueryHelper<PenaltyShot, PenaltyShotRepository> queryHelper =
            new MyBatisQueryHelper<>(PenaltyShotRepository.class);

    @Override
    public void create(PenaltyShot penaltyShot, long goalkeeperPerformanceId, long shooterPerformanceId) {
        this.queryHelper.executeUpdate(repository -> {
            repository.create(penaltyShot, goalkeeperPerformanceId, shooterPerformanceId);
        });
    }

    @Override
    public void update(PenaltyShot penaltyShot, long goalkeeperPerformanceId, long shooterPerformanceId) {
        this.queryHelper.executeUpdate(repository -> {
            repository.update(penaltyShot, goalkeeperPerformanceId, shooterPerformanceId);
        });
    }

    @Override
    public void delete(PenaltyShot penaltyShot) {
        this.queryHelper.executeUpdate(repository -> repository.delete(penaltyShot));
    }

    @Override
    public Optional<PenaltyShot> findById(long id) {
        return this.queryHelper.executeSingleRowQuery(repository -> repository.findById(id));
    }

    @Override
    public Optional<Long> findGoalkeeperPerformanceIdByPenaltyShotId(long id) {
        return this.queryHelper.<Long>executeSingleRowQueryGenericReturn(
                repository -> repository.findGoalkeeperPerformanceIdByPenaltyShotId(id));
    }

    @Override
    public Optional<Long> findShooterPerformanceIdByPenaltyShotId(long id) {
        return this.queryHelper.<Long>executeSingleRowQueryGenericReturn(
                repository -> repository.findShooterPerformanceIdByPenaltyShotId(id));
    }

    @Override
    public List<PenaltyShot> findAll() {
        return this.queryHelper.executeMultiRowQuery(PenaltyShotRepository::findAll);
    }

    @Override
    public List<PenaltyShot> findByGoalkeeperPerformanceId(long id) {
        return this.queryHelper.executeMultiRowQuery(
                repository -> repository.findByGoalkeeperPerformanceId(id));
    }

    @Override
    public List<PenaltyShot> findByShooterPerformanceId(long id) {
        return this.queryHelper.executeMultiRowQuery(
                repository -> repository.findByShooterPerformanceId(id));
    }

    @Override
    public List<PenaltyShot> findByRelatedPerformanceId(long id) {
        return this.queryHelper.executeMultiRowQuery(
                repository -> repository.findByRelatedPerformanceId(id));
    }
}
