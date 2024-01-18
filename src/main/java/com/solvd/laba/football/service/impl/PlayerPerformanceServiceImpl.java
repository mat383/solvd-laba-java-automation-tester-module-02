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
    public void create(PlayerPerformance playerPerformance) {
        // create player performance
        this.playerPerformanceRepository.create(playerPerformance);

        // create position if not exists, otherwise update it
        createOrUpdatePosition(playerPerformance.getPosition());

        // create / update penalty shots
        createOrUpdatePenaltyShots(playerPerformance);

        // create / update goal attempts
        createOrUpdateGoalAttempt(playerPerformance);
    }

    @Override
    public void update(PlayerPerformance playerPerformance) {
        // create player performance
        this.playerPerformanceRepository.update(playerPerformance);

        // create position if not exists, otherwise update it
        createOrUpdatePosition(playerPerformance.getPosition());

        // create / update penalty shots
        createOrUpdatePenaltyShots(playerPerformance);

        // create / update goal attempts
        createOrUpdateGoalAttempt(playerPerformance);
    }

    @Override
    public void delete(PlayerPerformance playerPerformance) {
        // delete penalty shots
        deleteRelatedPenaltyShots(playerPerformance);

        // delete goal attempts
        deleteRelatedGoalAttempts(playerPerformance);

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
     * loads & fills player performance with:
     * goal attempts
     * penalty shots
     * position
     *
     * @param playerPerformance
     */
    private void loadDetails(PlayerPerformance playerPerformance) {

        long playerPerformanceId = playerPerformance.getId();

        // add goal attempts
        this.goalAttemptService
                .findByDefenderPerformanceId(playerPerformanceId)
                .forEach(playerPerformance::addGoalAttemptAsDefender);
        this.goalAttemptService
                .findByAttackerPerformanceId(playerPerformanceId)
                .forEach(playerPerformance::addGoalAttemptAsAttacker);

        // add penalty shots
        this.penaltyShootService
                .findByGoalkeeperPerformanceId(playerPerformanceId)
                .forEach(playerPerformance::addPenaltyShotAsGoalkeeper);
        this.penaltyShootService
                .findByShooterPerformanceId(playerPerformanceId)
                .forEach(playerPerformance::addPenaltyShotAsShooter);

        // load position
        Position fullyLoadedPosition = this.positionService
                .findById(playerPerformance.getPosition().getId()).get();
        playerPerformance.setPosition(fullyLoadedPosition);
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
     * creates penalty shots if they don't exist
     * otherwise update it
     *
     * @param playerPerformance
     */
    private void createOrUpdatePenaltyShots(PlayerPerformance playerPerformance) {
        // TODO improve this so that it updates only if there is a need
        // handle shoots as goalkeeper
        for (PenaltyShot penaltyShot : playerPerformance.getPenaltyShotsAsGoalkeeper()) {
            // find corresponding shooter performance
            PlayerPerformance shooterPerformance = getShooterPerformance(playerPerformance, penaltyShot);

            if (penaltyShotInRepository(penaltyShot)) {
                this.penaltyShootService.update(penaltyShot, playerPerformance.getId(), shooterPerformance.getId());
            } else {
                this.penaltyShootService.create(penaltyShot, playerPerformance.getId(), shooterPerformance.getId());
            }
        }
        // handle shoots as shooter
        for (PenaltyShot penaltyShot : playerPerformance.getPenaltyShotsAsShooter()) {
            // find corresponding goalkeeper performance
            PlayerPerformance goalkeeperPerformance = getGoalkeeperPerformance(playerPerformance, penaltyShot);

            if (penaltyShotInRepository(penaltyShot)) {
                this.penaltyShootService.update(penaltyShot, goalkeeperPerformance.getId(), playerPerformance.getId());
            } else {
                this.penaltyShootService.create(penaltyShot, goalkeeperPerformance.getId(), playerPerformance.getId());
            }
        }
    }

    /**
     * creates goal attempts if they don't exist
     * otherwise update it
     *
     * @param playerPerformance
     */
    private void createOrUpdateGoalAttempt(PlayerPerformance playerPerformance) {
        // TODO improve this so that it updates only if there is a need
        // handle goal attempts as defender
        for (GoalAttempt goalAttempt : playerPerformance.getGoalAttemptsAsDefender()) {
            // find corresponding attacker performance
            PlayerPerformance attackerPerformance = getAttackerPerformance(playerPerformance, goalAttempt);

            if (goalAttemptInRepository(goalAttempt)) {
                this.goalAttemptService.update(goalAttempt, playerPerformance.getId(), attackerPerformance.getId());
            } else {
                this.goalAttemptService.create(goalAttempt, playerPerformance.getId(), attackerPerformance.getId());
            }
        }
        // handle goal attempts as attacker
        for (GoalAttempt goalAttempt : playerPerformance.getGoalAttemptsAsAttacker()) {
            // find corresponding defender performance
            PlayerPerformance defenderPerformance = getDefenderPerformance(playerPerformance, goalAttempt);

            if (goalAttemptInRepository(goalAttempt)) {
                this.goalAttemptService.update(goalAttempt, defenderPerformance.getId(), playerPerformance.getId());
            } else {
                this.goalAttemptService.create(goalAttempt, defenderPerformance.getId(), playerPerformance.getId());
            }
        }
    }


    private void deleteRelatedPenaltyShots(PlayerPerformance playerPerformance) {
        // TODO figure out how to avoid double deleting penalty shots when deleting set of related player performances
        for (PenaltyShot penaltyShot : playerPerformance.getAllPenaltyShots()) {
            this.penaltyShootService.delete(penaltyShot);
        }
    }


    private void deleteRelatedGoalAttempts(PlayerPerformance playerPerformance) {
        // TODO figure out how to avoid double deleting goal attempts when deleting set of related player performances
        for (GoalAttempt goalAttempt : playerPerformance.getAllGoalAttempts()) {
            this.goalAttemptService.delete(goalAttempt);
        }
    }


    /**
     * get shooter performance for penalty shot, throws Runtime Exception on failure
     *
     * @param playerPerformance
     * @param penaltyShot
     * @return
     */
    private static PlayerPerformance getShooterPerformance(PlayerPerformance playerPerformance, PenaltyShot penaltyShot) {
        final String exceptionMessage =
                "Cannot find shooter for penalty shot with id: " + penaltyShot.getId();
        return playerPerformance.getGame().findShooterPerformance(penaltyShot)
                .orElseThrow(() -> new RuntimeException(exceptionMessage));
    }

    /**
     * get goalkeeper performance for penalty shot, throws Runtime Exception on failure
     *
     * @param playerPerformance
     * @param penaltyShot
     * @return
     */
    private static PlayerPerformance getGoalkeeperPerformance(PlayerPerformance playerPerformance, PenaltyShot penaltyShot) {
        final String exceptionMessage =
                "Cannot find goalkeeper for penalty shot with id: " + penaltyShot.getId();
        return playerPerformance.getGame().findGoalkeeperPerformance(penaltyShot)
                .orElseThrow(() -> new RuntimeException(exceptionMessage));
    }

    /**
     * get attacker performance for goal attempt, throws Runtime Exception on failure
     *
     * @param playerPerformance
     * @param goalAttempt
     * @return
     */
    private static PlayerPerformance getAttackerPerformance(PlayerPerformance playerPerformance, GoalAttempt goalAttempt) {
        final String exceptionMessage =
                "Cannot find attacker for goal attempt with id: " + goalAttempt.getId();
        return playerPerformance.getGame().findAttackerPerformance(goalAttempt)
                .orElseThrow(() -> new RuntimeException(exceptionMessage));
    }

    /**
     * get defender performance for goal attempt, throws Runtime Exception on failure
     *
     * @param playerPerformance
     * @param goalAttempt
     * @return
     */
    private static PlayerPerformance getDefenderPerformance(PlayerPerformance playerPerformance, GoalAttempt goalAttempt) {
        final String exceptionMessage =
                "cannot find defender for goal attempt with id: " + goalAttempt.getId();
        return playerPerformance.getGame().findDefenderPerformance(goalAttempt)
                .orElseThrow(() -> new RuntimeException(exceptionMessage));
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
