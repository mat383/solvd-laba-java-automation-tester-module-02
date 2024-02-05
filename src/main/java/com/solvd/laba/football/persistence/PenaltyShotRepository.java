package com.solvd.laba.football.persistence;

import com.solvd.laba.football.domain.PenaltyShot;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface PenaltyShotRepository {

    void create(@Param("penaltyShot") PenaltyShot penaltyShot,
                @Param("goalkeeperPerformanceId") long goalkeeperPerformanceId,
                @Param("shooterPerformanceId") long shooterPerformanceId);

    void update(@Param("penaltyShot") PenaltyShot penaltyShot,
                @Param("goalkeeperPerformanceId") long goalkeeperPerformanceId,
                @Param("shooterPerformanceId") long shooterPerformanceId);


    void delete(PenaltyShot penaltyShot);

    Optional<PenaltyShot> findById(long id);

    Optional<Long> findGoalkeeperPerformanceIdByPenaltyShotId(long id);

    Optional<Long> findShooterPerformanceIdByPenaltyShotId(long id);

    List<PenaltyShot> findAll();

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
