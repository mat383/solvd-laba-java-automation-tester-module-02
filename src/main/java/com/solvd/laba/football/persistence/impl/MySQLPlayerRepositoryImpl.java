package com.solvd.laba.football.persistence.impl;

import com.solvd.laba.football.domain.Person;
import com.solvd.laba.football.domain.Player;
import com.solvd.laba.football.persistence.PlayerRepository;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MySQLPlayerRepositoryImpl implements PlayerRepository {
    private static final Logger LOGGER = LogManager.getLogger(MySQLPlayerRepositoryImpl.class.getName());
    private static final MySQLConnectionPool CONNECTION_POOL = MySQLConnectionPool.getInstance();


    @Override
    public void create(@NonNull Player player, long teamId) {
        try {
            int affectedRows = MySQLRepositoryHelper.executeUpdate(
                    "INSERT INTO players(id, person_id, preffered_position_id, team_id) VALUES (?, ?, ?, ?);",
                    preparedStatement -> {
                        preparedStatement.setLong(1, player.getId());
                        preparedStatement.setLong(2, player.getPerson().getId());
                        // FIXME FIX THIS -------- TEMPORARY CODE, NEEDS TO BE REPLACED WITH ACTUAL POSITION
                        preparedStatement.setLong(3, 1);
                        preparedStatement.setLong(4, teamId);
                    },
                    CONNECTION_POOL);
            assert affectedRows == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Unable create player", e);
        }
    }

    @Override
    public void update(@NonNull Player player, long teamId) {
        try {
            int affectedRows = MySQLRepositoryHelper.executeUpdate(
                    "UPDATE players SET person_id=?, preffered_position_id=?, team_id=? WHERE id=?;",
                    preparedStatement -> {
                        preparedStatement.setLong(1, player.getPerson().getId());
                        // FIXME FIX THIS -------- TEMPORARY CODE, NEEDS TO BE REPLACED WITH ACTUAL POSITION
                        preparedStatement.setLong(2, 1);
                        preparedStatement.setLong(3, teamId);
                        preparedStatement.setLong(4, player.getId());
                    },
                    CONNECTION_POOL);
            assert affectedRows == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Unable to update player", e);
        }
    }

    @Override
    public void delete(@NonNull Player player) {
        try {
            int affectedRows = MySQLRepositoryHelper.executeUpdate(
                    "DELETE FROM players WHERE id=?",
                    preparedStatement -> preparedStatement.setLong(1, player.getId()),
                    CONNECTION_POOL);
            assert affectedRows < 2;
        } catch (SQLException e) {
            throw new RuntimeException("Unable to delete player", e);
        }
    }

    @Override
    public Optional<Player> findById(long id) {
        Optional<Player> player = Optional.empty();
        try {
            List<Player> results = MySQLRepositoryHelper.executeQuery(
                    "SELECT id, person_id FROM players WHERE id=?;",
                    preparedStatement -> preparedStatement.setLong(1, id),
                    MySQLPlayerRepositoryImpl::createPlayerFromResultSet,
                    CONNECTION_POOL);
            assert results.size() < 2;

            if (!results.isEmpty()) {
                player = Optional.of(results.get(0));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find player", e);
        }
        return player;
    }

    @Override
    public List<Player> findAll() {
        try {
            return MySQLRepositoryHelper.executeQuery(
                    "SELECT id, person_id FROM players;",
                    preparedStatement -> {},
                    MySQLPlayerRepositoryImpl::createPlayerFromResultSet,
                    CONNECTION_POOL);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find player", e);
        }
    }


    @Override
    public List<Player> findByTeamId(long teamId) {
        try {
            return MySQLRepositoryHelper.executeQuery(
                    "SELECT id, person_id FROM players WHERE team_id=?;",
                    preparedStatement -> preparedStatement.setLong(1, teamId),
                    MySQLPlayerRepositoryImpl::createPlayerFromResultSet,
                    CONNECTION_POOL);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find player", e);
        }
    }

    static private Player createPlayerFromResultSet(ResultSet resultSet) throws SQLException {
        Person playerPerson = new Person(resultSet.getLong("person_id"),
                null, null, null);
        return new Player(resultSet.getLong("id"), playerPerson);
    }
}
