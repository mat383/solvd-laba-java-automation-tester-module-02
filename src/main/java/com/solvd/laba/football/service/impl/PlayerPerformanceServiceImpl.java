package com.solvd.laba.football.service.impl;

import com.solvd.laba.football.domain.GoalAttempt;
import com.solvd.laba.football.domain.PenaltyShot;
import com.solvd.laba.football.domain.PlayerPerformance;
import com.solvd.laba.football.domain.Position;
import com.solvd.laba.football.persistence.PlayerPerformanceRepository;
import com.solvd.laba.football.service.GoalAttemptService;
import com.solvd.laba.football.service.PenaltyShootService;
import com.solvd.laba.football.service.PlayerPerformanceService;
import com.solvd.laba.football.service.PositionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class PlayerPerformanceServiceImpl implements PlayerPerformanceService {
    private static final Logger LOGGER = LogManager.getLogger(PlayerPerformanceServiceImpl.class.getName());

    private final PlayerPerformanceRepository playerPerformanceRepository;
    private final GoalAttemptService goalAttemptService;
    private final PenaltyShootService penaltyShootService;
    private final PositionService positionService;

    public PlayerPerformanceServiceImpl(PlayerPerformanceRepository playerPerformanceRepository,
                                        GoalAttemptService goalAttemptService,
                                        PenaltyShootService penaltyShootService,
                                        PositionService positionService) {
        this.playerPerformanceRepository = playerPerformanceRepository;
        this.goalAttemptService = goalAttemptService;
        this.penaltyShootService = penaltyShootService;
        this.positionService = positionService;
    }

    @Override
    public void create(PlayerPerformance playerPerformance, long playerId) {
        // create player performance
        this.playerPerformanceRepository.create(playerPerformance, playerId);

        // create position if not exists, otherwise update it
        createOrUpdatePosition(playerPerformance.getPosition());

        // create / update penalty shots
        createOrUpdatePenaltyShots(playerPerformance.getPenaltyShots());

        // create / update goal attempts
        createOrUpdateGoalAttempt(playerPerformance.getGoalAttempts());
    }

    @Override
    public void update(PlayerPerformance playerPerformance, long playerId) {
        // create player performance
        this.playerPerformanceRepository.update(playerPerformance, playerId);

        // create position if not exists, otherwise update it
        createOrUpdatePosition(playerPerformance.getPosition());

        // create / update penalty shots
        createOrUpdatePenaltyShots(playerPerformance.getPenaltyShots());

        // create / update goal attempts
        createOrUpdateGoalAttempt(playerPerformance.getGoalAttempts());

    }

    @Override
    public void delete(PlayerPerformance playerPerformance) {
        // delete penalty shots
        while (!playerPerformance.getPenaltyShots().isEmpty()) {
            PenaltyShot penaltyShot = playerPerformance.getPenaltyShots().remove(
                    playerPerformance.getPenaltyShots().size() - 1
            );
            // delete probably will try to remove it from playerPerformance
            this.penaltyShootService.delete(penaltyShot);
        }

        // delete goal attempts
        while (!playerPerformance.getGoalAttempts().isEmpty()) {
            GoalAttempt goalAttempt = playerPerformance.getGoalAttempts().remove(
                    playerPerformance.getGoalAttempts().size() - 1
            );
            // delete probably will try to remove it from playerPerformance
            this.goalAttemptService.delete(goalAttempt);
        }

        // delete player performance
        playerPerformanceRepository.delete(playerPerformance);
    }

    @Override
    public Optional<PlayerPerformance> findById(long id) {
        Optional<PlayerPerformance> playerPerformance = this.playerPerformanceRepository.findById(id);
        playerPerformance.ifPresent(this::loadDetails);

        if (playerPerformance.isEmpty()) {
            LOGGER.info("Loading PlayerPerformance with id '" + id + "' failed. Not found in repository.");
        } else {
            LOGGER.info("Loading PlayerPerformance with id '" + id + "' successful.");
        }

        return playerPerformance;
    }

    @Override
    public List<PlayerPerformance> findByPlayerId(long id) {
        List<PlayerPerformance> playerPerformanceList = this.playerPerformanceRepository.findByPlayerId(id);
        LOGGER.info("Loading PlayerPerformances related to player with id=" + id);

        for (PlayerPerformance playerPerformance : playerPerformanceList) {
            loadDetails(playerPerformance);
            LOGGER.info("Loading PlayerPerformance with id '" + playerPerformance.getId() + "' successful.");
        }

        return playerPerformanceList;
    }

    @Override
    public List<PlayerPerformance> findByGameId(long id) {
        List<PlayerPerformance> playerPerformanceList = this.playerPerformanceRepository.findByGameId(id);
        LOGGER.info("Loading PlayerPerformances related to game with id=" + id);

        for (PlayerPerformance playerPerformance : playerPerformanceList) {
            loadDetails(playerPerformance);
            LOGGER.info("Loading PlayerPerformance with id '" + playerPerformance.getId() + "' successful.");
        }

        return playerPerformanceList;
    }

    @Override
    public List<PlayerPerformance> findAll() {
        List<PlayerPerformance> playerPerformanceList = this.playerPerformanceRepository.findAll();
        LOGGER.info("Loading all PlayerPerformances");

        for (PlayerPerformance playerPerformance : playerPerformanceList) {
            loadDetails(playerPerformance);
            LOGGER.info("Loading PlayerPerformance with id '" + playerPerformance.getId() + "' successful.");
        }

        return playerPerformanceList;
    }

    /**
     * fills player performance with goal attempts
     * and penalty shots
     *
     * @param playerPerformance
     */
    private void loadDetails(PlayerPerformance playerPerformance) {

        long playerPerformanceId = playerPerformance.getId();

        // add goal attempts
        List<GoalAttempt> goalAttemptList = this.goalAttemptService
                .findByRelatedPerformanceId(playerPerformanceId);

        for (GoalAttempt goalAttempt : goalAttemptList) {
            // make reference in goal attempt point to correct object
            if (goalAttempt.getDefenderPerformance().getId()
                    .equals(playerPerformanceId)) {
                goalAttempt.setDefenderPerformance(playerPerformance);
            } else if (goalAttempt.getAttackerPerformance().getId()
                    .equals(playerPerformanceId)) {
                goalAttempt.setAttackerPerformance(playerPerformance);
            }

            // add goal attempt
            playerPerformance.addGoalAttempt(goalAttempt);
        }

        // add penalty shots
        List<PenaltyShot> penaltyShotList = this.penaltyShootService
                .findByRelatedPerformanceId(playerPerformanceId);

        for (PenaltyShot penaltyShot : penaltyShotList) {
            // make reference in penalty shot point to correct object
            if (penaltyShot.getGoalkeeperPerformance().getId()
                    .equals(playerPerformanceId)) {
                penaltyShot.setGoalkeeperPerformance(playerPerformance);
            } else if (penaltyShot.getShooterPerformance().getId()
                    .equals(playerPerformanceId)) {
                penaltyShot.setShooterPerformance(playerPerformance);
            }
            // add penalty shot
            playerPerformance.addPenaltyShot(penaltyShot);
        }

    }

    /**
     * creates position if it doesn't exist
     * otherwise update it
     *
     * @param position
     */
    private void createOrUpdatePosition(Position position) {
        // TODO improve this so that it updates only if there is a need
        if (positionInRepository(position)) {
            this.positionService.update(position);
        } else {
            this.positionService.create(position);
        }
    }

    /**
     * creates penalty shot if it doesn't exist
     * otherwise update it
     *
     * @param penaltyShotsList
     */
    private void createOrUpdatePenaltyShots(List<PenaltyShot> penaltyShotsList) {
        // TODO improve this so that it updates only if there is a need
        for (PenaltyShot penaltyShot : penaltyShotsList) {
            if (penaltyShotInRepository(penaltyShot)) {
                this.penaltyShootService.update(penaltyShot);
            } else {
                this.penaltyShootService.create(penaltyShot);
            }
        }
    }

    /**
     * creates goal attempt if it doesn't exist
     * otherwise update it
     *
     * @param goalAttemptsList
     */
    private void createOrUpdateGoalAttempt(List<GoalAttempt> goalAttemptsList) {
        // TODO improve this so that it updates only if there is a need
        for (GoalAttempt goalAttempt : goalAttemptsList) {
            if (goalAttemptInRepository(goalAttempt)) {
                this.goalAttemptService.update(goalAttempt);
            } else {
                this.goalAttemptService.create(goalAttempt);
            }
        }
    }

    private boolean positionInRepository(Position position) {
        return this.positionService.findById(position.getId())
                .isPresent();
    }

    private boolean penaltyShotInRepository(PenaltyShot penaltyShot) {
        return this.penaltyShootService.findById(penaltyShot.getId())
                .isPresent();
    }

    private boolean goalAttemptInRepository(GoalAttempt goalAttempt) {
        return this.goalAttemptService.findById(goalAttempt.getId())
                .isPresent();
    }
}
