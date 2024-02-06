package com.solvd.laba.football.service.impl.jdbc;

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

public class PlayerPerformanceServiceJdbc implements PlayerPerformanceService {
    private static final Logger LOGGER = LogManager.getLogger(PlayerPerformanceServiceJdbc.class.getName());

    private final PlayerPerformanceRepository playerPerformanceRepository;
    private final GoalAttemptService goalAttemptService;
    private final PenaltyShootService penaltyShootService;
    private final PositionService positionService;

    public PlayerPerformanceServiceJdbc(PlayerPerformanceRepository playerPerformanceRepository,
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
            long shooterPerformanceId = getShooterPerformanceId(playerPerformance, penaltyShot);

            if (penaltyShotInRepository(penaltyShot)) {
                this.penaltyShootService.update(penaltyShot, playerPerformance.getId(), shooterPerformanceId);
            } else {
                this.penaltyShootService.create(penaltyShot, playerPerformance.getId(), shooterPerformanceId);
            }
        }
        // handle shoots as shooter
        for (PenaltyShot penaltyShot : playerPerformance.getPenaltyShotsAsShooter()) {
            // find corresponding goalkeeper performance
            long goalkeeperPerformanceId = getGoalkeeperPerformanceId(playerPerformance, penaltyShot);

            if (penaltyShotInRepository(penaltyShot)) {
                this.penaltyShootService.update(penaltyShot, goalkeeperPerformanceId, playerPerformance.getId());
            } else {
                this.penaltyShootService.create(penaltyShot, goalkeeperPerformanceId, playerPerformance.getId());
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
            long attackerPerformanceId = getAttackerPerformanceId(playerPerformance, goalAttempt);

            if (goalAttemptInRepository(goalAttempt)) {
                this.goalAttemptService.update(goalAttempt, playerPerformance.getId(), attackerPerformanceId);
            } else {
                this.goalAttemptService.create(goalAttempt, playerPerformance.getId(), attackerPerformanceId);
            }
        }
        // handle goal attempts as attacker
        for (GoalAttempt goalAttempt : playerPerformance.getGoalAttemptsAsAttacker()) {
            // find corresponding defender performance
            long defenderPerformanceId = getDefenderPerformanceId(playerPerformance, goalAttempt);

            if (goalAttemptInRepository(goalAttempt)) {
                this.goalAttemptService.update(goalAttempt, defenderPerformanceId, playerPerformance.getId());
            } else {
                this.goalAttemptService.create(goalAttempt, defenderPerformanceId, playerPerformance.getId());
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
     * get goalkeeper performance id for penalty shot, throws Runtime Exception on failure
     * tries to get it from game from penalty shot service, but if it's null, then it uses
     * player performance repository
     */
    private long getGoalkeeperPerformanceId(PlayerPerformance playerPerformance, PenaltyShot penaltyShot) {
        final String exceptionMessage =
                "Cannot find goalkeeper for penalty shot with id: " + penaltyShot.getId();

        // try to get performance id through game
        Optional<PlayerPerformance> goalkeeperPerformance =
                playerPerformance.getGame().findGoalkeeperPerformance(penaltyShot);

        if (goalkeeperPerformance.isPresent()) {
            return goalkeeperPerformance.get().getId();
        } else {
            // get performance id from player repository
            return this.penaltyShootService
                    .findGoalkeeperPerformanceIdByPenaltyShotId(penaltyShot.getId())
                    .orElseThrow(() -> new RuntimeException(exceptionMessage));
        }
    }

    /**
     * get shooter performance id for penalty shot, throws Runtime Exception on failure
     * tries to get it from game from penalty shot service, but if it's null, then it uses
     * player performance repository
     */
    private long getShooterPerformanceId(PlayerPerformance playerPerformance, PenaltyShot penaltyShot) {
        final String exceptionMessage =
                "Cannot find shooter for penalty shot with id: " + penaltyShot.getId();

        // try to get performance id through game
        Optional<PlayerPerformance> shooterPerformance =
                playerPerformance.getGame().findShooterPerformance(penaltyShot);

        if (shooterPerformance.isPresent()) {
            return shooterPerformance.get().getId();
        } else {
            // get performance id from player repository
            return this.penaltyShootService
                    .findShooterPerformanceIdByPenaltyShotId(penaltyShot.getId())
                    .orElseThrow(() -> new RuntimeException(exceptionMessage));
        }
    }

    /**
     * get defender performance id for goal attempt, throws Runtime Exception on failure
     * tries to get it from game from goal attempt service, but if it's null, then it uses
     * player performance repository
     */
    private long getDefenderPerformanceId(PlayerPerformance playerPerformance, GoalAttempt goalAttempt) {
        final String exceptionMessage =
                "cannot find defender for goal attempt with id: " + goalAttempt.getId();

        // try to get performance id through game
        Optional<PlayerPerformance> defenderPerformance =
                playerPerformance.getGame().findDefenderPerformance(goalAttempt);

        if (defenderPerformance.isPresent()) {
            return defenderPerformance.get().getId();
        } else {
            // get performance id from player repository
            return this.goalAttemptService
                    .findDefenderPerformanceIdByGoalAttemptId(goalAttempt.getId())
                    .orElseThrow(() -> new RuntimeException(exceptionMessage));
        }
    }

    /**
     * get attacker performance id for goal attempt, throws Runtime Exception on failure
     * tries to get it from game from goal attempt service, but if it's null, then it uses
     * player performance repository
     */
    private long getAttackerPerformanceId(PlayerPerformance playerPerformance, GoalAttempt goalAttempt) {
        final String exceptionMessage =
                "Cannot find attacker for goal attempt with id: " + goalAttempt.getId();

        // try to get performance id through game
        Optional<PlayerPerformance> attackerPerformance =
                playerPerformance.getGame().findAttackerPerformance(goalAttempt);

        if (attackerPerformance.isPresent()) {
            return attackerPerformance.get().getId();
        } else {
            // get performance id from player repository
            return this.goalAttemptService
                    .findAttackerPerformanceIdByGoalAttemptId(goalAttempt.getId())
                    .orElseThrow(() -> new RuntimeException(exceptionMessage));
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
