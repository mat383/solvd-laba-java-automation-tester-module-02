package com.solvd.laba.football.persistence.impl;

import com.solvd.laba.football.domain.PenaltyShot;
import com.solvd.laba.football.domain.ShootOutcome;
import com.solvd.laba.football.domain.interfaces.Identifiable;
import com.solvd.laba.football.persistence.PenaltyShotRepository;
import com.solvd.laba.football.persistence.impl.util.MySQLConnectionPool;
import com.solvd.laba.football.persistence.impl.util.MySQLRepositoryHelper;
import com.solvd.laba.football.persistence.impl.util.MySQLTable;
import lombok.*;

import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

public class PenaltyShotRepositoryMySQL implements PenaltyShotRepository {
    private static final MySQLConnectionPool CONNECTION_POOL = MySQLConnectionPool.getInstance();

    private final MySQLTable<PenaltyShotTransport> penaltyShotsTable = createPenaltyShotsTableDescription();


    @Override
    public void create(PenaltyShot penaltyShot, long goalkeeperPerformanceId, long shooterPerformanceId) {
        PenaltyShotTransport penaltyShotTransport =
                PenaltyShotTransport.of(penaltyShot, goalkeeperPerformanceId, shooterPerformanceId);

        this.penaltyShotsTable.insertRow(penaltyShotTransport);
        penaltyShot.setId(penaltyShotTransport.getId());
    }

    @Override
    public void update(PenaltyShot penaltyShot, long goalkeeperPerformanceId, long shooterPerformanceId) {
        this.penaltyShotsTable.updateRow(
                PenaltyShotTransport.of(penaltyShot, goalkeeperPerformanceId, shooterPerformanceId));
    }

    @Override
    public void delete(PenaltyShot penaltyShot) {
        this.penaltyShotsTable.deleteRow(
                PenaltyShotTransport.of(penaltyShot, null, null));
    }

    @Override
    public Optional<PenaltyShot> findById(long id) {
        return this.penaltyShotsTable.findRowById(id)
                .map(PenaltyShotTransport::toGoalAttempt);
    }

    @Override
    public Optional<Long> findGoalkeeperPerformanceIdByPenaltyShotId(long id) {
        return this.penaltyShotsTable.findRowById(id)
                .map(PenaltyShotTransport::getGoalkeeperPerformanceId);
    }

    @Override
    public Optional<Long> findShooterPerformanceIdByPenaltyShotId(long id) {
        return this.penaltyShotsTable.findRowById(id)
                .map(PenaltyShotTransport::getShooterPerformanceId);
    }

    @Override
    public List<PenaltyShot> findAll() {
        return this.penaltyShotsTable.findAllRows().stream()
                .map(PenaltyShotTransport::toGoalAttempt)
                .toList();
    }

    @Override
    public List<PenaltyShot> findByGoalkeeperPerformanceId(long id) {
        return this.penaltyShotsTable.findRowsByLongColumn("goalkeeper_performance_id", id)
                .stream()
                .map(PenaltyShotTransport::toGoalAttempt)
                .toList();
    }

    @Override
    public List<PenaltyShot> findByShooterPerformanceId(long id) {
        return this.penaltyShotsTable.findRowsByLongColumn("shooter_performance_id", id)
                .stream()
                .map(PenaltyShotTransport::toGoalAttempt)
                .toList();
    }

    @Override
    public List<PenaltyShot> findByRelatedPerformanceId(long id) {
        try {
            return MySQLRepositoryHelper.executeQuery(
                    "SELECT id, outcome_id, game_time " +
                            "FROM penalty_shots WHERE goalkeeper_performance_id = ? OR shooter_performance_id = ?;",
                    preparedStatement -> {
                        preparedStatement.setLong(1, id);
                        preparedStatement.setLong(2, id);
                    },
                    resultSet -> {
                        // build object from result set
                        return new PenaltyShot(
                                resultSet.getLong("id"),
                                new ShootOutcome(resultSet.getLong("outcome_id"), null),
                                resultSet.getTime("game_time").toLocalTime());
                    },
                    CONNECTION_POOL);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find penalty shots", e);
        }
    }


    private static MySQLTable<PenaltyShotTransport> createPenaltyShotsTableDescription() {
        return new MySQLTable<>(
                "penalty_shots",
                "id",
                createGoalAttemptColumnsDescription(),
                PenaltyShotTransport::new
        );
    }

    private static List<MySQLTable.Column<PenaltyShotTransport>> createGoalAttemptColumnsDescription() {
        return List.of(
                new MySQLTable.Column<>(
                        "goalkeeper_performance_id",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, penaltyShot) -> {
                            preparedStatement.setLong(parameterIndex, penaltyShot.getGoalkeeperPerformanceId());
                        },
                        // saving results
                        (resultSet, columnLabel, penaltyShot) -> {
                            penaltyShot.setGoalkeeperPerformanceId(resultSet.getLong(columnLabel));
                        }
                ),
                new MySQLTable.Column<>(
                        "shooter_performance_id",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, penaltyShot) -> {
                            preparedStatement.setLong(parameterIndex, penaltyShot.getShooterPerformanceId());
                        },
                        // saving results
                        (resultSet, columnLabel, penaltyShot) -> {
                            penaltyShot.setShooterPerformanceId(resultSet.getLong(columnLabel));
                        }
                ),
                new MySQLTable.Column<>(
                        "outcome_id",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, penaltyShot) -> {
                            preparedStatement.setLong(parameterIndex, penaltyShot.getOutcomeId());
                        },
                        // saving results
                        (resultSet, columnLabel, penaltyShot) -> {
                            penaltyShot.setOutcomeId(resultSet.getLong(columnLabel));
                        }
                ),
                new MySQLTable.Column<>(
                        "game_time",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, penaltyShot) -> {
                            preparedStatement.setTime(parameterIndex, penaltyShot.getGameTime());
                        },
                        // saving results
                        (resultSet, columnLabel, penaltyShot) -> {
                            penaltyShot.setGameTime(resultSet.getTime(columnLabel));
                        }
                )
        );
    }


    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class PenaltyShotTransport implements Identifiable {
        private Long id;
        private Long outcomeId;
        private Time gameTime;
        private Long goalkeeperPerformanceId;
        private Long shooterPerformanceId;

        @Override
        public void setId(long id) {
            this.id = id;
        }

        public static PenaltyShotTransport of(PenaltyShot penaltyShot, Long goalkeeperPerformanceId, Long shooterPerformanceId) {
            Time sqlTime = Time.valueOf(penaltyShot.getGameTime());
            return PenaltyShotTransport.builder()
                    .id(penaltyShot.getId())
                    .outcomeId(penaltyShot.getOutcome().getId())
                    .gameTime(sqlTime)
                    .goalkeeperPerformanceId(goalkeeperPerformanceId)
                    .shooterPerformanceId(shooterPerformanceId)
                    .build();
        }

        public PenaltyShot toGoalAttempt() {
            return new PenaltyShot(
                    this.id,
                    new ShootOutcome(this.outcomeId, null),
                    this.gameTime.toLocalTime()
            );
        }
    }
}
