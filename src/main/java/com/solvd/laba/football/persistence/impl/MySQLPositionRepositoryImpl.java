package com.solvd.laba.football.persistence.impl;

import com.solvd.laba.football.domain.Position;
import com.solvd.laba.football.persistence.PositionRepository;
import com.solvd.laba.football.persistence.impl.util.MySQLConnectionPool;
import com.solvd.laba.football.persistence.impl.util.MySQLRepositoryHelper;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MySQLPositionRepositoryImpl implements PositionRepository {
    private static final Logger LOGGER = LogManager.getLogger(MySQLPositionRepositoryImpl.class.getName());
    private static final MySQLConnectionPool CONNECTION_POOL = MySQLConnectionPool.getInstance();


    /**
     * inserts position into database
     *
     * @param position
     */
    @Override
    public void create(@NonNull Position position) {
        if (position.hasSetId()) {
            throw new IllegalArgumentException("Position cannot have id set");
        }
        try {
            MySQLRepositoryHelper.UpdateResult result = MySQLRepositoryHelper.executeUpdateGetKeys(
                    "INSERT INTO positions(name) VALUES (?);",
                    preparedStatement -> preparedStatement.setString(1, position.getName()),
                    CONNECTION_POOL);
            assert result.affectedRows() == 1;
            assert result.generatedKeys().size() == 1;
            position.setId(result.generatedKeys().get(0));
        } catch (SQLException e) {
            throw new RuntimeException("Unable create position", e);
        }
    }

    @Override
    public void update(@NonNull Position position) {
        if (!position.hasSetId()) {
            throw new IllegalArgumentException("Unable to update position that doesn't have id.");
        }
        try {
            int affectedRows = MySQLRepositoryHelper.executeUpdate(
                    "UPDATE positions SET name=? WHERE id=?;",
                    preparedStatement -> preparedStatement.setString(1, position.getName()),
                    CONNECTION_POOL);
            assert affectedRows == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Unable to update position", e);
        }
    }

    // TODO: return bool to indicate if deletion occured?
    @Override
    public void delete(@NonNull Position position) {
        if (!position.hasSetId()) {
            throw new IllegalArgumentException("Unable to delete position that doesn't have id.");
        }
        try {
            int affectedRows = MySQLRepositoryHelper.executeUpdate(
                    "DELETE FROM positions WHERE id=?;",
                    preparedStatement -> {
                        preparedStatement.setLong(1, position.getId());
                    },
                    CONNECTION_POOL);
            assert affectedRows < 2;
        } catch (SQLException e) {
            throw new RuntimeException("Unable to delete position", e);
        }
    }

    @Override
    public Optional<Position> findById(long id) {
        Optional<Position> position = Optional.empty();
        try {
            List<Position> results = MySQLRepositoryHelper.executeQuery(
                    "SELECT id, name FROM positions WHERE id=?;",
                    preparedStatement -> preparedStatement.setLong(1, id),
                    MySQLPositionRepositoryImpl::createPositionFromResultSet,
                    CONNECTION_POOL);
            assert results.size() < 2;

            if (!results.isEmpty()) {
                position = Optional.of(results.get(0));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find position", e);
        }
        return position;
    }

    @Override
    public List<Position> findAll() {
        try {
            return MySQLRepositoryHelper.executeQuery(
                    "SELECT id, name FROM positions;",
                    preparedStatement -> {},
                    MySQLPositionRepositoryImpl::createPositionFromResultSet,
                    CONNECTION_POOL);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find position", e);
        }
    }


    /**
     * generates positions from resultSet uses current position
     * doesn't change cursor position
     *
     * @param resultSet
     * @return
     */
    static private Position createPositionFromResultSet(ResultSet resultSet) throws SQLException {
        return new Position(resultSet.getLong("id"),
                resultSet.getString("name"));
    }
}
