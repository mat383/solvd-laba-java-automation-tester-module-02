package com.solvd.laba.football.persistence.impl.jdbc;

import com.solvd.laba.football.domain.Team;
import com.solvd.laba.football.persistence.TeamRepository;
import com.solvd.laba.football.persistence.impl.jdbc.util.MySQLConnectionPool;
import com.solvd.laba.football.persistence.impl.jdbc.util.MySQLRepositoryHelper;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TeamRepositoryJDBC implements TeamRepository {
    private static final Logger LOGGER = LogManager.getLogger(TeamRepositoryJDBC.class.getName());
    private static final MySQLConnectionPool CONNECTION_POOL = MySQLConnectionPool.getInstance();

    @Override
    public void create(@NonNull Team team, long clubId, Long leagueId, Integer leaguePosition) {
        if (team.hasSetId()) {
            throw new IllegalArgumentException("Team cannot have id set");
        }
        try {
            MySQLRepositoryHelper.UpdateResult result = MySQLRepositoryHelper.executeUpdateGetKeys(
                    "INSERT INTO teams(name, club_id, creation_date, closure_date, league_id, league_position) VALUES (?, ?, ?, ?, ?, ?);",
                    preparedStatement -> {
                        preparedStatement.setString(1, team.getName());
                        preparedStatement.setLong(2, clubId);
                        preparedStatement.setDate(3, Date.valueOf(team.getCreationDate()));
                        preparedStatement.setDate(4, Date.valueOf(team.getClosureDate()));
                        preparedStatement.setLong(5, leagueId);
                        preparedStatement.setInt(6, leaguePosition);
                    },
                    CONNECTION_POOL);
            assert result.affectedRows() == 1;
            assert result.generatedKeys().size() == 1;
            team.setId(result.generatedKeys().get(0));
        } catch (SQLException e) {
            throw new RuntimeException("Unable to create team", e);
        }
    }

    @Override
    public void update(@NonNull Team team, long clubId, Long leagueId, Integer leaguePosition) {
        if (!team.hasSetId()) {
            throw new IllegalArgumentException("Unable to update team that doesn't have id.");
        }
        try {
            int affectedRows = MySQLRepositoryHelper.executeUpdate(
                    "UPDATE teams SET name=?, club_id=?, creation_date=?, closure_date=?, league_id=?, league_position=? WHERE id=?;",
                    preparedStatement -> {
                        preparedStatement.setString(1, team.getName());
                        preparedStatement.setLong(2, clubId);
                        preparedStatement.setDate(3, Date.valueOf(team.getCreationDate()));
                        preparedStatement.setDate(4, team.getClosureDate() == null ? null : Date.valueOf(team.getClosureDate()));
                        preparedStatement.setLong(5, leagueId);
                        preparedStatement.setInt(6, leaguePosition);
                        preparedStatement.setLong(7, team.getId());
                    },
                    CONNECTION_POOL);
            assert affectedRows == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Unable to create team", e);
        }
    }

    @Override
    public void delete(@NonNull Team team) {
        if (!team.hasSetId()) {
            throw new IllegalArgumentException("Unable to delete team that doesn't have id.");
        }
        try {
            int affectedRows = MySQLRepositoryHelper.executeUpdate(
                    "DELETE FROM teams WHERE id=?;",
                    preparedStatement -> preparedStatement.setLong(1, team.getId()),
                    CONNECTION_POOL);
            assert affectedRows == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Unable to create team", e);
        }
    }

    @Override
    public Optional<Team> findById(long id) {
        Optional<Team> team = Optional.empty();
        try {
            List<Team> results = MySQLRepositoryHelper.executeQuery(
                    "SELECT id, name, club_id, creation_date, closure_date, league_id, league_position FROM teams WHERE id=?;",
                    preparedStatement -> preparedStatement.setLong(1, id),
                    TeamRepositoryJDBC::createTeamFromResultSet,
                    CONNECTION_POOL);
            if (!results.isEmpty()) {
                team = Optional.of(results.get(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to create team", e);
        }
        return team;
    }

    @Override
    public List<Team> findAll() {
        try {
            return MySQLRepositoryHelper.executeQuery(
                    "SELECT id, name, club_id, creation_date, closure_date, league_id, league_position FROM teams;",
                    preparedStatement -> {
                    },
                    TeamRepositoryJDBC::createTeamFromResultSet,
                    CONNECTION_POOL);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to create team", e);
        }
    }

    @Override
    public List<Team> findByClubId(long id) {
        try {
            return MySQLRepositoryHelper.executeQuery(
                    "SELECT id, name, club_id, creation_date, closure_date, league_id, league_position FROM teams WHERE club_id=?;",
                    preparedStatement -> preparedStatement.setLong(1, id),
                    TeamRepositoryJDBC::createTeamFromResultSet,
                    CONNECTION_POOL);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to create team", e);
        }
    }

    static private Team createTeamFromResultSet(ResultSet resultSet) throws SQLException {
        Date closureSqlDate = resultSet.getDate("closure_date");
        LocalDate closureDate = null;
        if (closureSqlDate != null) {
            closureDate = closureSqlDate.toLocalDate();
        }
        return new Team(resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getDate("creation_date").toLocalDate(),
                closureDate);
    }
}
