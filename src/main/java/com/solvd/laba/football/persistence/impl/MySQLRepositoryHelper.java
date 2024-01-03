package com.solvd.laba.football.persistence.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
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
     * @param listToPutKeys     list that this method will fill with generated keys
     * @return
     * @throws SQLException
     */
    public static int executeUpdateGetKeys(String query,
                                           StatementPreparer statementPreparer,
                                           MySQLConnectionPool connectionPool,
                                           List<Long> listToPutKeys) throws SQLException {
        Connection connection = null;
        int affectedRows = 0;
        try {
            connection = connectionPool.getConnection();

            try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statementPreparer.prepare(preparedStatement);
                affectedRows = preparedStatement.executeUpdate();
                ResultSet keys = preparedStatement.getGeneratedKeys();
                while (keys.next()) {
                    listToPutKeys.add(keys.getLong(1));
                }
            }
        } finally {
            connectionPool.releaseConnection(connection);
        }
        return affectedRows;
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


    /**
     * create sql query for inserting object into database
     * this query is meant to be used with prepared statement,
     * so it doesn't contain any values
     *
     * @param tableName
     * @param fieldNames fields to be updated
     * @return insert query for PreparedStatement
     */
    public static String buildInsertQuery(String tableName, List<String> fieldNames) {
        String createQuery = "INSERT_INTO `%s`".formatted(tableName) + "("
                + String.join(", ", fieldNames)
                + ") VALUES ("
                + String.join(", ", Collections.nCopies(fieldNames.size(), "?"))
                + ");";

        LOGGER.info("Generated create query: \"" + createQuery + "\"");

        return createQuery;
    }


    /**
     * create sql query for updating object in database
     * this query is meant to be used with prepared statement,
     * so it doesn't contain any values
     *
     * @param tableName        name of table to be altered
     * @param fieldsToUpdate   fields to update (ones in SET part)
     * @param primaryKeyFields fields to locate record to be updated (ones in WHERE part)
     * @return insert query for PreparedStatement
     */
    public static String buildUpdateQuery(String tableName, List<String> fieldsToUpdate, List<String> primaryKeyFields) {
        String updateQuery = "UPDATE `%s` ".formatted(tableName)
                + "SET "
                + String.join("=?, ", fieldsToUpdate) + "=? "
                + "WHERE "
                + String.join("=? AND", primaryKeyFields) + "=? "
                + ";";

        LOGGER.info("Generated update query: \"" + updateQuery + "\"");

        return updateQuery;
    }

    /**
     * create sql query for deleting object from database
     * this query is meant to be used with prepared statement,
     * so it doesn't contain any values
     *
     * @param tableName        name of table to be altered
     * @param primaryKeyFields fields to locate record to be updated (ones in WHERE part)
     * @return insert query for PreparedStatement
     */
    public static String buildDeleteQuery(String tableName, List<String> primaryKeyFields) {
        String deleteQuery = "DELETE FROM `%s` ".formatted(tableName)
                + "WHERE "
                + String.join("=? AND", primaryKeyFields) + "=? "
                + ";";

        LOGGER.info("Generated delete query: \"" + deleteQuery + "\"");

        return deleteQuery;
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
}
