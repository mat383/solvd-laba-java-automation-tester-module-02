package com.solvd.laba.football.persistence.impl;

import com.solvd.laba.football.domain.GoalAttempt;
import com.solvd.laba.football.domain.ShootOutcome;
import com.solvd.laba.football.domain.interfaces.Identifiable;
import com.solvd.laba.football.persistence.GoalAttemptRepository;
import com.solvd.laba.football.persistence.impl.util.MySQLConnectionPool;
import com.solvd.laba.football.persistence.impl.util.MySQLRepositoryHelper;
import com.solvd.laba.football.persistence.impl.util.MySQLTable;
import lombok.*;

import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

public class GoalAttemptRepositoryMySQL implements GoalAttemptRepository {
    private static final MySQLConnectionPool CONNECTION_POOL = MySQLConnectionPool.getInstance();

    private final MySQLTable<GoalAttemptTransport> goalAttemptsTable = createGoalAttemptsTableDescription();


    @Override
    public void create(GoalAttempt goalAttempt, long defenderPerformanceId, long attackerPerformanceId) {
        GoalAttemptTransport goalAttemptTransport =
                GoalAttemptTransport.of(goalAttempt, defenderPerformanceId, attackerPerformanceId);

        this.goalAttemptsTable.insertRow(goalAttemptTransport);
        goalAttempt.setId(goalAttemptTransport.getId());
    }

    @Override
    public void update(GoalAttempt goalAttempt, long defenderPerformanceId, long attackerPerformanceId) {
        this.goalAttemptsTable.updateRow(
                GoalAttemptTransport.of(goalAttempt, defenderPerformanceId, attackerPerformanceId));
    }

    @Override
    public void delete(GoalAttempt goalAttempt) {
        this.goalAttemptsTable.deleteRow(
                GoalAttemptTransport.of(goalAttempt, null, null));
    }

    @Override
    public Optional<GoalAttempt> findById(long id) {
        return this.goalAttemptsTable.findRowById(id)
                .map(GoalAttemptTransport::toGoalAttempt);
    }

    @Override
    public Optional<Long> findDefenderPerformanceIdByGoalAttemptId(long id) {
        return this.goalAttemptsTable.findRowById(id)
                .map(GoalAttemptTransport::getDefenderPerformanceId);
    }

    @Override
    public Optional<Long> findAttackerPerformanceIdByGoalAttemptId(long id) {
        return this.goalAttemptsTable.findRowById(id)
                .map(GoalAttemptTransport::getAttackerPerformanceId);
    }

    @Override
    public List<GoalAttempt> findAll() {
        return this.goalAttemptsTable.findAllRows().stream()
                .map(GoalAttemptTransport::toGoalAttempt)
                .toList();
    }

    @Override
    public List<GoalAttempt> findByDefenderPerformanceId(long id) {
        return this.goalAttemptsTable.findRowsByLongColumn("defender_performance_id", id)
                .stream()
                .map(GoalAttemptTransport::toGoalAttempt)
                .toList();

    }

    @Override
    public List<GoalAttempt> findByAttackerPerformanceId(long id) {
        return this.goalAttemptsTable.findRowsByLongColumn("attacker_performance_id", id)
                .stream()
                .map(GoalAttemptTransport::toGoalAttempt)
                .toList();
    }

    @Override
    public List<GoalAttempt> findByRelatedPerformanceId(long id) {
        try {
            return MySQLRepositoryHelper.executeQuery(
                    "SELECT id, game_time, outcome_id " +
                            "FROM goal_attempts WHERE defender_performance_id = ? OR attacker_performance_id = ?;",
                    preparedStatement -> {
                        preparedStatement.setLong(1, id);
                        preparedStatement.setLong(2, id);
                    },
                    resultSet -> {
                        // build object from result set
                        return new GoalAttempt(
                                resultSet.getLong("id"),
                                new ShootOutcome(resultSet.getLong("outcome_id"), null),
                                resultSet.getTime("game_time").toLocalTime());
                    },
                    CONNECTION_POOL);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find goal attempts", e);
        }
    }


    private static MySQLTable<GoalAttemptTransport> createGoalAttemptsTableDescription() {
        return new MySQLTable<>(
                "goal_attempts",
                "id",
                createGoalAttemptColumnsDescription(),
                GoalAttemptTransport::new
        );
    }

    private static List<MySQLTable.Column<GoalAttemptTransport>> createGoalAttemptColumnsDescription() {
        return List.of(
                new MySQLTable.Column<>(
                        "game_time",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, goalAttemptTransport) -> {
                            preparedStatement.setTime(parameterIndex, goalAttemptTransport.getGameTime());
                        },
                        // saving results
                        (resultSet, columnLabel, goalAttemptTransport) -> {
                            goalAttemptTransport.setGameTime(resultSet.getTime(columnLabel));
                        }
                ),
                new MySQLTable.Column<>(
                        "defender_performance_id",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, goalAttemptTransport) -> {
                            preparedStatement.setLong(parameterIndex, goalAttemptTransport.getDefenderPerformanceId());
                        },
                        // saving results
                        (resultSet, columnLabel, goalAttemptTransport) -> {
                            goalAttemptTransport.setDefenderPerformanceId(resultSet.getLong(columnLabel));
                        }
                ),
                new MySQLTable.Column<>(
                        "attacker_performance_id",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, goalAttemptTransport) -> {
                            preparedStatement.setLong(parameterIndex, goalAttemptTransport.getAttackerPerformanceId());
                        },
                        // saving results
                        (resultSet, columnLabel, goalAttemptTransport) -> {
                            goalAttemptTransport.setAttackerPerformanceId(resultSet.getLong(columnLabel));
                        }
                ),
                new MySQLTable.Column<>(
                        "outcome_id",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, goalAttemptTransport) -> {
                            preparedStatement.setLong(parameterIndex, goalAttemptTransport.getOutcomeId());
                        },
                        // saving results
                        (resultSet, columnLabel, goalAttemptTransport) -> {
                            goalAttemptTransport.setOutcomeId(resultSet.getLong(columnLabel));
                        }
                )
        );
    }


    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class GoalAttemptTransport implements Identifiable {
        private Long id;
        private Long outcomeId;
        private Time gameTime;
        private Long defenderPerformanceId;
        private Long attackerPerformanceId;

        @Override
        public void setId(long id) {
            this.id = id;
        }

        public static GoalAttemptTransport of(GoalAttempt goalAttempt, Long defenderPerformanceId, Long attackerPerformanceId) {
            Time sqlTime = Time.valueOf(goalAttempt.getGameTime());
            return GoalAttemptTransport.builder()
                    .id(goalAttempt.getId())
                    .outcomeId(goalAttempt.getOutcome().getId())
                    .gameTime(sqlTime)
                    .defenderPerformanceId(defenderPerformanceId)
                    .attackerPerformanceId(attackerPerformanceId)
                    .build();
        }

        public GoalAttempt toGoalAttempt() {
            return new GoalAttempt(
                    this.id,
                    new ShootOutcome(this.outcomeId, null),
                    this.gameTime.toLocalTime()
            );
        }
    }
}
