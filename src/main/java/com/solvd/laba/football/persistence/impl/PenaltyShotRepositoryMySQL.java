package com.solvd.laba.football.persistence.impl;

import com.solvd.laba.football.domain.PenaltyShot;
import com.solvd.laba.football.domain.PlayerPerformance;
import com.solvd.laba.football.domain.ShootOutcome;
import com.solvd.laba.football.persistence.PenaltyShotRepository;
import com.solvd.laba.football.persistence.impl.util.MySQLConnectionPool;
import com.solvd.laba.football.persistence.impl.util.MySQLRepositoryHelper;
import com.solvd.laba.football.persistence.impl.util.MySQLTable;

import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

public class PenaltyShotRepositoryMySQL implements PenaltyShotRepository {
    private static final MySQLConnectionPool CONNECTION_POOL = MySQLConnectionPool.getInstance();

    private final MySQLTable<PenaltyShot> penaltyShotsTable = createPenaltyShotsTableDescription();


    @Override
    public void create(PenaltyShot penaltyShot) {
        this.penaltyShotsTable.insertRow(penaltyShot);
    }

    @Override
    public void update(PenaltyShot penaltyShot) {
        this.penaltyShotsTable.updateRow(penaltyShot);
    }

    @Override
    public void delete(PenaltyShot penaltyShot) {
        this.penaltyShotsTable.deleteRow(penaltyShot);
    }

    @Override
    public Optional<PenaltyShot> findById(long id) {
        return this.penaltyShotsTable.findRowById(id);
    }

    @Override
    public List<PenaltyShot> findAll() {
        return this.penaltyShotsTable.findAllRows();
    }

    @Override
    public List<PenaltyShot> findByGoalkeeperPerformanceId(long id) {
        return this.penaltyShotsTable.findRowsByLongColumn("defender_performance_id", id);
    }

    @Override
    public List<PenaltyShot> findByShooterPerformanceId(long id) {
        return this.penaltyShotsTable.findRowsByLongColumn("attacker_performance_id", id);
    }

    @Override
    public List<PenaltyShot> findByRelatedPerformanceId(long id) {
        try {
            return MySQLRepositoryHelper.executeQuery(
                    "SELECT id, goalkeeper_performance_id, shooter_performance_id, outcome_id, game_time " +
                            "FROM penalty_shots WHERE goalkeeper_performance_id = ? OR shooter_performance_id = ?;",
                    preparedStatement -> {
                        preparedStatement.setLong(1, id);
                        preparedStatement.setLong(2, id);
                    },
                    resultSet -> {
                        // build object from result set
                        PlayerPerformance goalkeeperPerformance = new PlayerPerformance();
                        PlayerPerformance shooterPerformance = new PlayerPerformance();
                        goalkeeperPerformance.setId(resultSet.getLong("goalkeeper_performance_id"));
                        shooterPerformance.setId(resultSet.getLong("shooter_performance_id"));

                        return new PenaltyShot(resultSet.getLong("id"),
                                new ShootOutcome(resultSet.getLong("outcome_id"), null),
                                goalkeeperPerformance, shooterPerformance,
                                resultSet.getTime("game_time").toLocalTime());
                    },
                    CONNECTION_POOL);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find penalty shots", e);
        }
    }


    private static MySQLTable<PenaltyShot> createPenaltyShotsTableDescription() {
        return new MySQLTable<>(
                "penalty_shots",
                "id",
                createGoalAttemptColumnsDescription(),
                () -> {
                    return new PenaltyShot(null, new ShootOutcome(), new PlayerPerformance(), new PlayerPerformance(), null);
                }
        );
    }

    private static List<MySQLTable.Column<PenaltyShot>> createGoalAttemptColumnsDescription() {
        return List.of(
                new MySQLTable.Column<>(
                        "goalkeeper_performance_id",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, penaltyShot) -> {
                            preparedStatement.setLong(parameterIndex, penaltyShot.getGoalkeeperPerformance().getId());
                        },
                        // saving results
                        (resultSet, columnLabel, penaltyShot) -> {
                            penaltyShot.getGoalkeeperPerformance().setId(resultSet.getLong(columnLabel));
                        }
                ),
                new MySQLTable.Column<>(
                        "attacker_performance_id",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, penaltyShot) -> {
                            preparedStatement.setLong(parameterIndex, penaltyShot.getShooterPerformance().getId());
                        },
                        // saving results
                        (resultSet, columnLabel, penaltyShot) -> {
                            penaltyShot.getShooterPerformance().setId(resultSet.getLong(columnLabel));
                        }
                ),
                new MySQLTable.Column<>(
                        "outcome_id",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, penaltyShot) -> {
                            preparedStatement.setLong(parameterIndex, penaltyShot.getOutcome().getId());
                        },
                        // saving results
                        (resultSet, columnLabel, penaltyShot) -> {
                            penaltyShot.getOutcome().setId(resultSet.getLong(columnLabel));
                        }
                ),
                new MySQLTable.Column<>(
                        "game_time",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, penaltyShot) -> {
                            preparedStatement.setTime(parameterIndex, Time.valueOf(penaltyShot.getGameTime()));
                        },
                        // saving results
                        (resultSet, columnLabel, penaltyShot) -> {
                            penaltyShot.setGameTime(resultSet.getTime(columnLabel).toLocalTime());
                        }
                )
        );
    }
}
