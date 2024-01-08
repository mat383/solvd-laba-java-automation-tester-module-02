package com.solvd.laba.football.persistence.impl;

import com.solvd.laba.football.domain.Game;
import com.solvd.laba.football.persistence.GameRepository;
import com.solvd.laba.football.persistence.impl.util.MySQLTable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class GameRepositoryMySQL implements GameRepository {

    // TODO when stadiums are implemented add them here
    private MySQLTable<Game> gamesTable = new MySQLTable<>(
            "games",
            "id",
            List.of(
                    new MySQLTable.Column<>("name",
                            GameRepositoryMySQL::nameResultSetter,
                            GameRepositoryMySQL::nameResultSaver),
                    new MySQLTable.Column<>("time",
                            GameRepositoryMySQL::timeResultSetter,
                            GameRepositoryMySQL::timeResultSaver),
                    new MySQLTable.Column<>("duration",
                            GameRepositoryMySQL::durationResultSetter,
                            GameRepositoryMySQL::durationResultSaver),
                    new MySQLTable.Column<>("first_half_duration",
                            GameRepositoryMySQL::firstHalfDurationResultSetter,
                            GameRepositoryMySQL::firstHalfDurationResultSaver)
            ),
            () -> new Game(null, null, null, null, null, null, null)
    );

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


    private static void nameResultSetter(PreparedStatement preparedStatement, int parameterIndex,
                                         Game game) throws SQLException {
        preparedStatement.setString(parameterIndex, game.getName());
    }

    private static void nameResultSaver(ResultSet resultSet, String columnLabel,
                                        Game game) throws SQLException {
        game.setName(resultSet.getString(columnLabel));
    }

    private static void timeResultSetter(PreparedStatement preparedStatement, int parameterIndex,
                                         Game game) throws SQLException {
        preparedStatement.setObject(parameterIndex, game.getTime());
    }

    private static void timeResultSaver(ResultSet resultSet, String columnLabel,
                                        Game game) throws SQLException {
        game.setTime(resultSet.getObject(columnLabel, LocalDateTime.class));
    }

    private static void durationResultSetter(PreparedStatement preparedStatement, int parameterIndex,
                                             Game game) throws SQLException {
        preparedStatement.setTime(parameterIndex, new Time(game.getDuration().toMillis()));
    }

    private static void durationResultSaver(ResultSet resultSet, String columnLabel,
                                            Game game) throws SQLException {
        // TODO make it use the same unit as result setter
        game.setDuration(Duration.ofNanos(
                resultSet.getTime(columnLabel).toLocalTime().toNanoOfDay()
        ));
    }

    private static void firstHalfDurationResultSetter(PreparedStatement preparedStatement, int parameterIndex,
                                                      Game game) throws SQLException {
        preparedStatement.setTime(parameterIndex, new Time(game.getFirstHalfDuration().toMillis()));
    }

    private static void firstHalfDurationResultSaver(ResultSet resultSet, String columnLabel,
                                                     Game game) throws SQLException {
        // TODO make it use the same unit as result setter
        game.setFirstHalfDuration(Duration.ofNanos(
                resultSet.getTime(columnLabel).toLocalTime().toNanoOfDay()
        ));
    }
}
