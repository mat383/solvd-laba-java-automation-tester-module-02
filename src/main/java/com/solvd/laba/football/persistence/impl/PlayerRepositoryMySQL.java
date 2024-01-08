package com.solvd.laba.football.persistence.impl;

import com.solvd.laba.football.domain.Person;
import com.solvd.laba.football.domain.Player;
import com.solvd.laba.football.persistence.PlayerRepository;
import com.solvd.laba.football.persistence.impl.util.MySQLConnectionPool;
import com.solvd.laba.football.persistence.impl.util.MySQLRepositoryHelper;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PlayerRepositoryMySQL implements PlayerRepository {
    private static final Logger LOGGER = LogManager.getLogger(PlayerRepositoryMySQL.class.getName());
    private static final MySQLConnectionPool CONNECTION_POOL = MySQLConnectionPool.getInstance();


    @Override
    public void create(@NonNull Player player, long teamId) {
        if (player.hasSetId()) {
            throw new IllegalArgumentException("Person cannot have id set");
        }
        try {
            MySQLRepositoryHelper.UpdateResult result = MySQLRepositoryHelper.executeUpdateGetKeys(
                    "INSERT INTO players(person_id, preffered_position_id, team_id) VALUES (?, ?, ?);",
                    preparedStatement -> {
                        preparedStatement.setLong(1, player.getPerson().getId());
                        // FIXME FIX THIS -------- TEMPORARY CODE, NEEDS TO BE REPLACED WITH ACTUAL POSITION
                        preparedStatement.setLong(2, 1);
                        preparedStatement.setLong(3, teamId);
                    },
                    CONNECTION_POOL);
            assert result.affectedRows() == 1;
            assert result.generatedKeys().size() == 1;
            player.setId(result.generatedKeys().get(0));
        } catch (SQLException e) {
            throw new RuntimeException("Unable create player", e);
        }
    }

    @Override
    public void update(@NonNull Player player, long teamId) {
        if (!player.hasSetId()) {
            throw new IllegalArgumentException("Unable to update player that doesn't have id.");
        }
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
        if (!player.hasSetId()) {
            throw new IllegalArgumentException("Unable to delete player that doesn't have id.");
        }
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
                    PlayerRepositoryMySQL::createPlayerFromResultSet,
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
                    PlayerRepositoryMySQL::createPlayerFromResultSet,
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
                    PlayerRepositoryMySQL::createPlayerFromResultSet,
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
