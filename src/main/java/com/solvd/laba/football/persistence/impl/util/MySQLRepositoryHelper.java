package com.solvd.laba.football.persistence.impl.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLRepositoryHelper {
    private static final Logger LOGGER = LogManager.getLogger(MySQLRepositoryHelper.class.getName());

    /**
     * execute given sql query and return number of rows affected
     *
     * @param query             SQL query that is passes to PreparedStatement
     * @param statementPreparer lambda that feeds data to PreparedStatement
     * @param connectionPool
     * @return
     * @throws SQLException
     */
    public static int executeUpdate(String query,
                                    StatementPreparer statementPreparer,
                                    MySQLConnectionPool connectionPool) throws SQLException {
        Connection connection = null;
        int affectedRows = 0;
        try {
            connection = connectionPool.getConnection();

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                statementPreparer.prepare(preparedStatement);
                affectedRows = preparedStatement.executeUpdate();
            }
        } finally {
            connectionPool.releaseConnection(connection);
        }
        return affectedRows;
    }


    /**
     * execute given sql query and return number of rows affected
     * also get generated Keys
     * this method assumes that ResultSet with generated keys
     * will have one field of type long
     *
     * @param query             SQL query that is passes to PreparedStatement
     * @param statementPreparer lambda that feeds data to PreparedStatement
     * @param connectionPool
     * @return
     * @throws SQLException
     */
    public static UpdateResult executeUpdateGetKeys(String query,
                                                    StatementPreparer statementPreparer,
                                                    MySQLConnectionPool connectionPool) throws SQLException {
        Connection connection = null;
        int affectedRows = 0;
        List<Long> generatedKeys = new ArrayList<>(1);
        try {
            connection = connectionPool.getConnection();

            try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statementPreparer.prepare(preparedStatement);
                affectedRows = preparedStatement.executeUpdate();
                ResultSet keys = preparedStatement.getGeneratedKeys();
                while (keys.next()) {
                    generatedKeys.add(keys.getLong(1));
                }
            }
        } finally {
            connectionPool.releaseConnection(connection);
        }
        return new UpdateResult(affectedRows, generatedKeys);
    }

    public static <T> List<T> executeQuery(String query,
                                           StatementPreparer statementPreparer,
                                           ResultSetMapper<T> resultSetMapper,
                                           MySQLConnectionPool connectionPool) throws SQLException {
        Connection connection = null;
        List<T> results = new ArrayList<>();
        try {
            connection = connectionPool.getConnection();
            connection.setReadOnly(true);

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                statementPreparer.prepare(preparedStatement);
                ResultSet resultSet = preparedStatement.executeQuery();

                // create objects from results
                while (resultSet.next()) {
                    results.add(resultSetMapper.map(resultSet));
                }
            }
        } finally {
            connectionPool.releaseConnection(connection);
        }
        return results;
    }


    @FunctionalInterface
    public interface StatementPreparer {
        void prepare(PreparedStatement preparedStatement) throws SQLException;
    }

    @FunctionalInterface
    public interface ResultSetMapper<T> {
        /**
         * converts single ResultSet row to object of type T
         * shouldn't call next on ResultSet
         *
         * @param resultSet
         * @return
         * @throws SQLException
         */
        T map(ResultSet resultSet) throws SQLException;
    }

    public record UpdateResult(
            int affectedRows,
            List<Long> generatedKeys
    ) {
    }
}
