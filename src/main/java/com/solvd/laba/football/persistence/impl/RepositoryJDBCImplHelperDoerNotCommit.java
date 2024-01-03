package com.solvd.laba.football.persistence.impl;

import com.solvd.laba.football.persistence.IRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

// TODO: rename this class to be more descriptive
public class RepositoryJDBCImplHelper<T> implements IRepository<T> {
    private static final Logger LOGGER = LogManager.getLogger(RepositoryJDBCImplHelper.class.getName());
    private static final ConnectionPoolJDBC CONNECTION_POOL = ConnectionPoolJDBC.getInstance();
    private final String tableName;
    private final List<Field<T>> fields;

    private final String createQuery;
    private final String updateQuery;
    private final String deleteQuery;
    //private final String findByIdQuery;
    //private final String findAllQuery;

    public RepositoryJDBCImplHelper(String tableName, List<Field<T>> fields) {
        if (fields.isEmpty()) {
            throw new IllegalArgumentException("fields list cannot be empty");
        }
        this.tableName = tableName;
        this.fields = List.copyOf(fields);
        this.createQuery = buildCreateQuery();
        this.updateQuery = buildUpdateQuery();
        this.deleteQuery = buildDeleteQuery();
    }

    @Override
    public void create(T object) {

    }

    @Override
    public void update(T object) {

    }

    @Override
    public void delete(T object) {

    }

    @Override
    public Optional<T> findById(long id) {
        return Optional.empty();
    }

    @Override
    public List<T> findAll() {
        return null;
    }


    /**
     * create sql query for inserting object into database
     * this query is meant to be used with prepared statement,
     * so it doesn't contain any values
     *
     * @return insert query for PreparedStatement
     */
    private final String buildCreateQuery() {
        List<String> fieldNames = getFieldNames();

        String createQuery = "INSERT_INTO `%s`".formatted(this.tableName) + "("
                + String.join(", ", fieldNames)
                + ") VALUES ("
                + String.join(", ", Collections.nCopies(fields.size(), "?"))
                + ");";

        LOGGER.info("Generated create query: \"" + createQuery + "\"");

        return createQuery;
    }

    /**
     * create sql query for inserting object into database
     * this query is meant to be used with prepared statement,
     * so it doesn't contain any values
     *
     * @return insert query for PreparedStatement
     */
    public static String buildCreateQueryForFields(String tableName, List<String> fieldNames) {
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
     * @return insert query for PreparedStatement
     */
    private final String buildUpdateQuery() {
        String updateQuery = "UPDATE `%s` ".formatted(this.tableName)
                + "SET "
                + String.join("=?, ", getNonPrimaryKeyFieldNames()) + "=? "
                + "WHERE "
                + String.join("=? AND", getPrimaryKeyFieldNames()) + "=? "
                + ";";

        LOGGER.info("Generated update query: \"" + updateQuery + "\"");

        return updateQuery;
    }

    /**
     * create sql query for deleting object from database
     * this query is meant to be used with prepared statement,
     * so it doesn't contain any values
     *
     * @return insert query for PreparedStatement
     */
    private final String buildDeleteQuery() {
        String deleteQuery = "DELETE FROM `%s` ".formatted(this.tableName)
                + "WHERE "
                + String.join("=? AND", getPrimaryKeyFieldNames()) + "=? "
                + ";";

        LOGGER.info("Generated delete query: \"" + deleteQuery + "\"");

        return deleteQuery;
    }

    /**
     * @return list of field names, with order preserved
     */
    private final List<String> getFieldNames() {
        return this.fields.stream()
                .map(Field::fieldName)
                .toList();
    }

    /**
     * @return list of primary keys names, with order preserved
     */
    private final List<String> getPrimaryKeyFieldNames() {
        return this.fields.stream()
                .filter(Field::isPrimaryKey)
                .map(Field::fieldName)
                .toList();
    }

    /**
     * @return list of names of fields that aren't primary keys,
     * with order preserved
     */
    private final List<String> getNonPrimaryKeyFieldNames() {
        return this.fields.stream()
                .filter(field -> !field.isPrimaryKey())
                .map(Field::fieldName)
                .toList();
    }

    public static record Field<U>(
            String fieldName,
            boolean isPrimaryKey,
            BiConsumer<PreparedStatement, U> statementSetter) {
    }
}
