package com.solvd.laba.football.persistence.impl;

import com.solvd.laba.football.domain.Game;
import com.solvd.laba.football.domain.PlayerPerformance;
import com.solvd.laba.football.domain.Position;
import com.solvd.laba.football.domain.Team;
import com.solvd.laba.football.domain.interfaces.Identifiable;
import com.solvd.laba.football.persistence.PlayerPerformanceRepository;
import com.solvd.laba.football.persistence.impl.util.MySQLTable;
import lombok.*;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlayerPerformanceRepositoryMySQL implements PlayerPerformanceRepository {
    private final MySQLTable<PlayerPerformanceTransfer> playerPerformanceTable = createPlayerPerformanceTableDescription();

    @Override
    public void create(PlayerPerformance playerPerformance, long playerId) {
        PlayerPerformanceTransfer playerPerformanceTransfer = PlayerPerformanceTransfer.of(playerPerformance, playerId);
        this.playerPerformanceTable.insertRow(playerPerformanceTransfer);
        playerPerformance.setId(playerPerformanceTransfer.getId());
    }

    @Override
    public void update(PlayerPerformance playerPerformance, long playerId) {
        this.playerPerformanceTable.updateRow(PlayerPerformanceTransfer.of(playerPerformance, playerId));
    }

    @Override
    public void delete(PlayerPerformance playerPerformance) {
        this.playerPerformanceTable.deleteRow(PlayerPerformanceTransfer.of(playerPerformance, null));
    }

    @Override
    public Optional<PlayerPerformance> findById(long id) {
        return this.playerPerformanceTable.findRowById(id);
    }

    @Override
    public List<PlayerPerformance> findByPlayerId(long id) {
        List<PlayerPerformanceTransfer> results = this.playerPerformanceTable.findRowsByLongColumn("player_id", id);
        return results.stream()
                .map(PlayerPerformanceTransfer::toPlayerPerformance)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<PlayerPerformance> findByGameId(long id) {
        List<PlayerPerformanceTransfer> results = this.playerPerformanceTable.findRowsByLongColumn("game_id", id);
        return results.stream()
                .map(PlayerPerformanceTransfer::toPlayerPerformance)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<PlayerPerformance> findAll() {
        List<PlayerPerformanceTransfer> results = this.playerPerformanceTable.findAllRows();
        return results.stream()
                .map(PlayerPerformanceTransfer::toPlayerPerformance)
                .collect(Collectors.toCollection(ArrayList::new));
    }


    private static MySQLTable<PlayerPerformanceTransfer> createPlayerPerformanceTableDescription() {
        return new MySQLTable<>(
                "player_performances",
                "id",
                createPlayerPerformanceColumnsDescription(),
                PlayerPerformanceTransfer::new
        );
    }

    private static List<MySQLTable.Column<PlayerPerformanceTransfer>> createPlayerPerformanceColumnsDescription() {
        return List.of(
                new MySQLTable.Column<>(
                        "player_id",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, playerPerformanceTransfer) -> {
                            preparedStatement.setLong(parameterIndex, playerPerformanceTransfer.getPlayerId());
                        },
                        // saving results
                        (resultSet, columnLabel, playerPerformanceTransfer) -> {
                            playerPerformanceTransfer.setPlayerId(resultSet.getLong(columnLabel));
                        }
                ),
                new MySQLTable.Column<>(
                        "position_id",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, playerPerformanceTransfer) -> {
                            preparedStatement.setLong(parameterIndex, playerPerformanceTransfer.getPositionId());
                        },
                        // saving results
                        (resultSet, columnLabel, playerPerformanceTransfer) -> {
                            playerPerformanceTransfer.setPositionId(resultSet.getLong(columnLabel));
                        }
                ),
                new MySQLTable.Column<>(
                        "game_id",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, playerPerformanceTransfer) -> {
                            preparedStatement.setLong(parameterIndex, playerPerformanceTransfer.getGameId());
                        },
                        // saving results
                        (resultSet, columnLabel, playerPerformanceTransfer) -> {
                            playerPerformanceTransfer.setGameId(resultSet.getLong(columnLabel));
                        }
                ),
                new MySQLTable.Column<>(
                        "team_id",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, playerPerformanceTransfer) -> {
                            preparedStatement.setLong(parameterIndex, playerPerformanceTransfer.getTeamId());
                        },
                        // saving results
                        (resultSet, columnLabel, playerPerformanceTransfer) -> {
                            playerPerformanceTransfer.setTeamId(resultSet.getLong(columnLabel));
                        }
                ),
                new MySQLTable.Column<>(
                        "defensive_performance",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, playerPerformanceTransfer) -> {
                            preparedStatement.setDouble(parameterIndex, playerPerformanceTransfer.getDefensivePerformance());
                        },
                        // saving results
                        (resultSet, columnLabel, playerPerformanceTransfer) -> {
                            playerPerformanceTransfer.setDefensivePerformance(resultSet.getDouble(columnLabel));
                        }
                ),
                new MySQLTable.Column<>(
                        "offensive_performance",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, playerPerformanceTransfer) -> {
                            preparedStatement.setDouble(parameterIndex, playerPerformanceTransfer.getOffensivePerformance());
                        },
                        // saving results
                        (resultSet, columnLabel, playerPerformanceTransfer) -> {
                            playerPerformanceTransfer.setOffensivePerformance(resultSet.getDouble(columnLabel));
                        }
                ),
                new MySQLTable.Column<>(
                        "cooperative_performance",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, playerPerformanceTransfer) -> {
                            preparedStatement.setDouble(parameterIndex, playerPerformanceTransfer.getCooperativePerformance());
                        },
                        // saving results
                        (resultSet, columnLabel, playerPerformanceTransfer) -> {
                            playerPerformanceTransfer.setCooperativePerformance(resultSet.getDouble(columnLabel));
                        }
                ),
                new MySQLTable.Column<>(
                        "performance_start",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, playerPerformanceTransfer) -> {
                            preparedStatement.setTime(parameterIndex, Time.valueOf(playerPerformanceTransfer.getStart()));
                        },
                        // saving results
                        (resultSet, columnLabel, playerPerformanceTransfer) -> {
                            playerPerformanceTransfer.setStart(resultSet.getTime(columnLabel).toLocalTime());
                        }
                ),
                new MySQLTable.Column<>(
                        "performance_end",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, playerPerformanceTransfer) -> {
                            preparedStatement.setTime(parameterIndex, Time.valueOf(playerPerformanceTransfer.getEnd()));
                        },
                        // saving results
                        (resultSet, columnLabel, playerPerformanceTransfer) -> {
                            playerPerformanceTransfer.setEnd(resultSet.getTime(columnLabel).toLocalTime());
                        }
                )
        );
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class PlayerPerformanceTransfer implements Identifiable {
        @Setter(AccessLevel.NONE)
        private Long id;
        private Long playerId;
        private Long gameId;
        private Double defensivePerformance;
        private Double offensivePerformance;
        private Double cooperativePerformance;
        private LocalTime start;
        private LocalTime end;
        private Long positionId;
        private Long teamId;

        @Override
        public void setId(long id) {
            this.id = id;
        }

        public static PlayerPerformanceTransfer of(PlayerPerformance playerPerformance, Long playerId) {
            return new PlayerPerformanceTransfer(
                    playerPerformance.getId(),
                    playerId,
                    playerPerformance.getGame().getId(),
                    playerPerformance.getDefensivePerformance(),
                    playerPerformance.getOffensivePerformance(),
                    playerPerformance.getCooperativePerformance(),
                    playerPerformance.getStart(),
                    playerPerformance.getEnd(),
                    playerPerformance.getPosition().getId(),
                    playerPerformance.getTeam().getId()
            );
        }

        public PlayerPerformance toPlayerPerformance() {
            Game game = new Game();
            game.setId(this.gameId);
            Position position = new Position();
            position.setId(this.positionId);
            Team team = new Team();
            team.setId(this.teamId);

            return new PlayerPerformance(
                    this.id,
                    game,
                    this.defensivePerformance,
                    this.offensivePerformance,
                    this.cooperativePerformance,
                    this.start,
                    this.end,
                    position,
                    team
            );
        }
    }
}
