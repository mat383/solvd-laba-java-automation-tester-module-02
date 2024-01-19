package com.solvd.laba.football.service.impl;

import com.solvd.laba.football.domain.PenaltyShot;
import com.solvd.laba.football.persistence.PenaltyShotRepository;
import com.solvd.laba.football.service.PenaltyShootService;

import java.util.List;
import java.util.Optional;

public class PenaltyShotServiceImpl implements PenaltyShootService {
    private final PenaltyShotRepository penaltyShotRepository;

    public PenaltyShotServiceImpl(PenaltyShotRepository penaltyShotRepository) {
        this.penaltyShotRepository = penaltyShotRepository;
    }


    @Override
    public void create(PenaltyShot penaltyShot, long goalkeeperPerformanceId, long shooterPerformanceId) {
        this.penaltyShotRepository.create(penaltyShot, goalkeeperPerformanceId, shooterPerformanceId);
    }

    @Override
    public void update(PenaltyShot penaltyShot, long goalkeeperPerformanceId, long shooterPerformanceId) {
        this.penaltyShotRepository.update(penaltyShot, goalkeeperPerformanceId, shooterPerformanceId);
    }

    @Override
    public void delete(PenaltyShot penaltyShot) {
        this.penaltyShotRepository.delete(penaltyShot);
    }

    @Override
    public Optional<PenaltyShot> findById(long id) {
        return this.penaltyShotRepository.findById(id);
    }

    @Override
    public Optional<Long> findGoalkeeperPerformanceIdByPenaltyShotId(long id) {
        return this.penaltyShotRepository.findGoalkeeperPerformanceIdByPenaltyShotId(id);
    }

    @Override
    public Optional<Long> findShooterPerformanceIdByPenaltyShotId(long id) {
        return this.penaltyShotRepository.findShooterPerformanceIdByPenaltyShotId(id);
    }

    @Override
    public List<PenaltyShot> findByGoalkeeperPerformanceId(long id) {
        return this.penaltyShotRepository.findByGoalkeeperPerformanceId(id);
    }

    @Override
    public List<PenaltyShot> findByShooterPerformanceId(long id) {
        return this.penaltyShotRepository.findByShooterPerformanceId(id);
    }

    @Override
    public List<PenaltyShot> findByRelatedPerformanceId(long id) {
        return this.penaltyShotRepository.findByRelatedPerformanceId(id);
    }

    @Override
    public List<PenaltyShot> findAll() {
        return this.penaltyShotRepository.findAll();
    }
}
