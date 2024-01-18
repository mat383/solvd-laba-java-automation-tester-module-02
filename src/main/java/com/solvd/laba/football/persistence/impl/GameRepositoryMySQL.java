package com.solvd.laba.football.persistence.impl;

import com.solvd.laba.football.domain.Game;
import com.solvd.laba.football.persistence.GameRepository;
import com.solvd.laba.football.persistence.impl.util.MySQLTable;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class GameRepositoryMySQL implements GameRepository {

    // TODO when stadiums are implemented add them here
    private final MySQLTable<Game> gamesTable = createGamesTableDescription();


    @Override
    public void create(Game game) {
        this.gamesTable.insertRow(game);
    }

    @Override
    public void update(Game game) {
        this.gamesTable.updateRow(game);
    }

    @Override
    public void delete(Game game) {
        this.gamesTable.deleteRow(game);
    }

    @Override
    public Optional<Game> findById(long id) {
        return this.gamesTable.findRowById(id);
    }

    @Override
    public List<Game> findAll() {
        return this.gamesTable.findAllRows();
    }


    private static MySQLTable<Game> createGamesTableDescription() {
        return new MySQLTable<>(
                "games",
                "id",
                createGamesColumnsDescription(),
                Game::new
        );
    }

    private static List<MySQLTable.Column<Game>> createGamesColumnsDescription() {
        return List.of(
                new MySQLTable.Column<>("name",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, game) -> {
                            preparedStatement.setString(parameterIndex, game.getName());
                        },
                        // saving results
                        (resultSet, columnLabel, game) -> {
                            game.setName(resultSet.getString(columnLabel));
                        }
                ),
                new MySQLTable.Column<>("time",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, game) -> {
                            preparedStatement.setObject(parameterIndex, game.getTime());
                        },
                        // saving results
                        (resultSet, columnLabel, game) -> {
                            game.setTime(resultSet.getObject(columnLabel, LocalDateTime.class));
                        }
                ),
                new MySQLTable.Column<>("duration",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, game) -> {
                            preparedStatement.setTime(parameterIndex, new Time(game.getDuration().toMillis()));
                        },
                        // saving results
                        (resultSet, columnLabel, game) -> {
                            // TODO make it use the same unit as result setter
                            game.setDuration(Duration.ofNanos(
                                    resultSet.getTime(columnLabel).toLocalTime().toNanoOfDay()
                            ));
                        }
                ),
                new MySQLTable.Column<>("first_half_duration",
                        // setting prepared statement
                        (preparedStatement, parameterIndex, game) -> {
                            preparedStatement.setTime(parameterIndex, new Time(game.getFirstHalfDuration().toMillis()));
                        },
                        // saving results
                        (resultSet, columnLabel, game) -> {
                            // TODO make it use the same unit as result setter
                            game.setFirstHalfDuration(Duration.ofNanos(
                                    resultSet.getTime(columnLabel).toLocalTime().toNanoOfDay()
                            ));
                        }
                )
        );
    }
}
