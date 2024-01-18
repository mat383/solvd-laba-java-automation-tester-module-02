package com.solvd.laba.football.service;

import com.solvd.laba.football.domain.PenaltyShot;

import java.util.List;
import java.util.Optional;

public interface PenaltyShootService {
    void create(PenaltyShot penaltyShot, long goalkeeperPerformanceId, long shooterPerformanceId);

    void update(PenaltyShot penaltyShot, long goalkeeperPerformanceId, long shooterPerformanceId);

    void delete(PenaltyShot penaltyShot);

    /**
     * finds PenaltyShot with specified id,
     * this will return PenaltyShot, but PlayerPerformances
     * that are inside will not be fully filled, and will be
     * containing only id
     *
     * @param id
     * @return
     */
    Optional<PenaltyShot> findById(long id);

    /**
     * finds PenaltyShot with specified goalkeeper performance id,
     * this will return list of PenaltyShots, but PlayerPerformances
     * that are inside will not be fully filled, and will be
     * containing only id
     *
     * @param id
     * @return
     */
    List<PenaltyShot> findByGoalkeeperPerformanceId(long id);

    /**
     * finds PenaltyShot with specified shooter performance id,
     * this will return list of PenaltyShots, but PlayerPerformances
     * that are inside will not be fully filled, and will be
     * containing only id
     *
     * @param id
     * @return
     */
    List<PenaltyShot> findByShooterPerformanceId(long id);

    /**
     * find PenaltyShot where either shooter id or goalkeeper id
     * equals to id
     * this will return list of PenaltyShots, but PlayerPerformances
     * that are inside will not be fully filled, and will be
     * containing only id
     *
     * @param id
     * @return
     */
    List<PenaltyShot> findByRelatedPerformanceId(long id);

    List<PenaltyShot> findAll();
}

