package com.solvd.laba.football.persistence.impl.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;

public class MySQLQueryBuilder {
    private static final Logger LOGGER = LogManager.getLogger(MySQLQueryBuilder.class.getName());

    /**
     * create sql query for inserting object into table
     * this query is meant to be used with prepared statement,
     * so it doesn't contain any values
     *
     * @param tableName
     * @param fieldNames fields to be updated
     * @return insert query for PreparedStatement
     */
    public static String buildInsertQuery(String tableName, List<String> fieldNames) {
        String createQuery = "INSERT INTO `%s` ".formatted(tableName)
                + "(`" + String.join("`, `", fieldNames) + "`) "
                + "VALUES "
                + "("
                + String.join(", ", Collections.nCopies(fieldNames.size(), "?"))
                + ");";

        LOGGER.info("Generated create query: \"" + createQuery + "\"");

        return createQuery;
    }

    /**
     * create sql query for updating object in table
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
                + "`" + String.join("` = ?, `", fieldsToUpdate) + "` = ? "
                + "WHERE "
                + "`" + String.join("` = ? AND `", primaryKeyFields) + "` = ? "
                + ";";

        LOGGER.info("Generated update query: \"" + updateQuery + "\"");

        return updateQuery;
    }

    /**
     * create sql query for deleting object from table
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
                + "`" + String.join("` = ? AND `", primaryKeyFields) + "` = ? "
                + ";";

        LOGGER.info("Generated delete query: \"" + deleteQuery + "\"");

        return deleteQuery;
    }


    /**
     * create sql query for selecting object from table by some field
     * this query is meant to be used with prepared statement,
     * so it doesn't contain any values
     *
     * @param tableName   name of table to get from
     * @param fieldsToGet fields to get
     * @param idField     field by which to select row(s)
     * @return insert query for PreparedStatement
     */
    public static String buildSelectByIdQuery(String tableName, List<String> fieldsToGet, String idField) {
        String selectByIdQuery = "SELECT "
                + "`" + String.join("`, `", fieldsToGet) + "` "
                + "FROM `%s` ".formatted(tableName)
                + "WHERE `%s` = ?".formatted(idField)
                + ";";

        LOGGER.info("Generated select by id query: \"" + selectByIdQuery + "\"");

        return selectByIdQuery;
    }


    /**
     * create sql query for selecting all object from table
     * this query is meant to be used with prepared statement,
     * so it doesn't contain any values
     *
     * @param tableName   name of table to get from
     * @param fieldsToGet fields to get
     * @return insert query for PreparedStatement
     */
    public static String buildSelectAllQuery(String tableName, List<String> fieldsToGet) {
        String selectAllQuery = "SELECT "
                + "`" + String.join("`, `", fieldsToGet) + "` "
                + "FROM `%s` ".formatted(tableName)
                + ";";

        LOGGER.info("Generated select all query: \"" + selectAllQuery + "\"");

        return selectAllQuery;
    }
}
