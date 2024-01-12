package com.solvd.laba.football.persistence.impl;

import com.solvd.laba.football.domain.GoalAttempt;
import com.solvd.laba.football.domain.PlayerPerformance;
import com.solvd.laba.football.domain.ShootOutcome;
import com.solvd.laba.football.persistence.GoalAttemptRepository;
import com.solvd.laba.football.persistence.impl.util.MySQLConnectionPool;
import com.solvd.laba.football.persistence.impl.util.MySQLRepositoryHelper;
import com.solvd.laba.football.persistence.impl.util.MySQLTable;

import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

public class GoalAttemptRepositoryMySQL implements GoalAttemptRepository {
    private static final MySQLConnectionPool CONNECTION_POOL = MySQLConnectionPool.getInstance();

    private final MySQLTable<GoalAttempt> goalAttemptsTable = createGoalAttemptsTableDescription();


    @Override
    public void create(GoalAttempt goalAttempt) {
        this.goalAttemptsTable.insertRow(goalAttempt);
    }

    @Override
    public void update(GoalAttempt goalAttempt) {
        this.goalAttemptsTable.updateRow(goalAttempt);
    }

    @Override
    public void delete(GoalAttempt goalAttempt) {
        this.goalAttemptsTable.deleteRow(goalAttempt);
    }

    @Override
    public Optional<GoalAttempt> findById(long id) {
        return this.goalAttemptsTable.findRowById(id);
    }

    @Override
    public List<GoalAttempt> findAll() {
        return this.goalAttemptsTable.findAllRows();
    }

    @Override
    public List<GoalAttempt> findByDefenderPerformanceId(long id) {
        return this.goalAttemptsTable.findRowsByLongColumn("defender_performance_id", id);
    }

    @Override
    public List<GoalAttempt> findByAttackerPerformanceId(long id) {
        return this.goalAttemptsTable.findRowsByLongColumn("attacker_performance_id", id);
    }

    @Override
    public List<GoalAttempt> findByRelatedPerformanceId(long id) {
        try {
            return MySQLRepositoryHelper.executeQuery(
                    "SELECT id, game_time, defender_performance_id, attacker_performance_id, outcome_id " +
                            "FROM goal_attempts WHERE defender_performance_id = ? OR attacker_performance_id = ?;",
                    preparedStatement -> {
                        preparedStatement.setLong(1, id);
                        preparedStatement.setLong(2, id);
                    },
                    resultSet -> {
                        // build object from result set
                        PlayerPerformance defenderPerformance = new PlayerPerformance();
                        PlayerPerformance attackerPerformance = new PlayerPerformance();
                        defenderPerformance.setId(resultSet.getLong("defender_performance_id"));
                        attackerPerformance.setId(resultSet.getLong("attacker_performance_id"));

                        return new GoalAttempt(resultSet.getLong("id"),
                                new ShootOutcome(resultSet.getLong("outcome_id"), null),
                                defenderPerformance, attackerPerformance,
                                resultSet.getTime("game_time").toLocalTime());
                    },
                    CONNECTION_POOL);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find goal attempts", e);
        }
    }


    private static MySQLTable<GoalAttempt> createGoalAttemptsTableDescription() {
        return new MySQLTable<>(
                "goal_attempts",
                "id",
                createGoalAttemptColumnsDescription(),
                () -> {
                    return new GoalAttempt(null, new ShootOutcome(), new PlayerPerformance(), new PlayerPerformance(), null);
                }
        );
    }

    private static List<MySQLTable.Column<GoalAttempt>> createGoalAttemptColumnsDescription() {
        return List.of(
                new MySQLTable.Column<>(
                        "game_time",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, goalAttempt) -> {
                            preparedStatement.setTime(parameterIndex, Time.valueOf(goalAttempt.getGameTime()));
                        },
                        // saving results
                        (resultSet, columnLabel, goalAttempt) -> {
                            goalAttempt.setGameTime(resultSet.getTime(columnLabel).toLocalTime());
                        }
                ),
                new MySQLTable.Column<>(
                        "defender_performance_id",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, goalAttempt) -> {
                            preparedStatement.setLong(parameterIndex, goalAttempt.getDefenderPerformance().getId());
                        },
                        // saving results
                        (resultSet, columnLabel, goalAttempt) -> {
                            goalAttempt.getDefenderPerformance().setId(resultSet.getLong(columnLabel));
                        }
                ),
                new MySQLTable.Column<>(
                        "attacker_performance_id",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, goalAttempt) -> {
                            preparedStatement.setLong(parameterIndex, goalAttempt.getAttackerPerformance().getId());
                        },
                        // saving results
                        (resultSet, columnLabel, goalAttempt) -> {
                            goalAttempt.getAttackerPerformance().setId(resultSet.getLong(columnLabel));
                        }
                ),
                new MySQLTable.Column<>(
                        "outcome_id",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, goalAttempt) -> {
                            preparedStatement.setLong(parameterIndex, goalAttempt.getOutcome().getId());
                        },
                        // saving results
                        (resultSet, columnLabel, goalAttempt) -> {
                            goalAttempt.getOutcome().setId(resultSet.getLong(columnLabel));
                        }
                )
        );
    }
}
