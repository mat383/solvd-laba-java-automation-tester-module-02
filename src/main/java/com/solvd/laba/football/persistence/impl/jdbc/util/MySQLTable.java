package com.solvd.laba.football.persistence.impl.jdbc.util;

import com.solvd.laba.football.domain.interfaces.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Class for doing simple operations on table from MySQL database
 * it assumes that this table have one primary key of type BIGINT
 *
 * @param <T>
 */
@AllArgsConstructor
public class MySQLTable<T extends Identifiable> {
    private static final Logger LOGGER = LogManager.getLogger(MySQLTable.class.getName());
    private static final MySQLConnectionPool CONNECTION_POOL = MySQLConnectionPool.getInstance();
    @Getter
    private final String name;
    /**
     * name of primary key column
     */
    private final String idColumnName;
    private final List<Column<T>> nonIdColumns;
    /**
     * constructs empty object that is filled with results using ResultSavers
     * used in find queries
     */
    private final Supplier<T> rowDataCreator;

    // TODO write constructor that will create immutable collections (not views) for columns
    // TODO constructor should also validate that columns are unique

    public void insertRow(T rowData) {
        if (rowData.hasSetId()) {
            throw new IllegalArgumentException(this.name + " row data cannot have id set");
        }
        try {
            MySQLRepositoryHelper.UpdateResult result = MySQLRepositoryHelper.executeUpdateGetKeys(
                    // create INSERT query
                    MySQLQueryBuilder.buildInsertQuery(this.name, getNonIdColumnNames()),
                    // fill in PreparedStatement
                    preparedStatement -> {
                        for (int i = 0; i < this.nonIdColumns.size(); i++) {
                            this.nonIdColumns.get(i).statementSetter().set(preparedStatement, i + 1, rowData);
                            LOGGER.info("Building query: inserting to column '" + this.nonIdColumns.get(i).name() + "' at position " + (i + 1));
                        }
                    },
                    CONNECTION_POOL);
            assert result.affectedRows() == 1;
            assert result.generatedKeys().size() == 1;
            // update rowData with auto-generated id
            rowData.setId(result.generatedKeys().get(0));
        } catch (SQLException e) {
            throw new RuntimeException("Unable to create row in " + this.name, e);
        }
    }

    public void updateRow(T rowData) {
        if (!rowData.hasSetId()) {
            throw new IllegalArgumentException(this.name + " row data must have id set");
        }
        try {
            int affectedRows = MySQLRepositoryHelper.executeUpdate(
                    // create UPDATE query
                    MySQLQueryBuilder.buildUpdateQuery(this.name, getNonIdColumnNames(), List.of(this.idColumnName)),
                    // fill in PreparedStatement
                    preparedStatement -> {
                        for (int i = 0; i < this.nonIdColumns.size(); i++) {
                            this.nonIdColumns.get(i).statementSetter().set(preparedStatement, i + 1, rowData);
                            LOGGER.info("Inserting to column '" + this.nonIdColumns.get(i).name() + "' at position " + (i + 1));
                        }
                        preparedStatement.setLong(this.nonIdColumns.size() + 1, rowData.getId());
                        LOGGER.info("Building query: inserting to column '" + this.idColumnName + "' at position " + (this.nonIdColumns.size() + 1));
                    },
                    CONNECTION_POOL);
            assert affectedRows == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Unable to update row in " + this.name, e);
        }
    }

    public void deleteRow(T rowData) {
        if (!rowData.hasSetId()) {
            throw new IllegalArgumentException(this.name + " row data must have id set");
        }
        try {
            int affectedRows = MySQLRepositoryHelper.executeUpdate(
                    // create DELETE query
                    MySQLQueryBuilder.buildDeleteQuery(this.name, List.of(this.idColumnName)),
                    // fill in PreparedStatement
                    preparedStatement -> {
                        preparedStatement.setLong(1, rowData.getId());
                        LOGGER.info("Building query: inserting to column '" + this.idColumnName + "' at position " + 1);
                    },
                    CONNECTION_POOL);
            assert affectedRows < 2;
        } catch (SQLException e) {
            throw new RuntimeException("Unable to delete row in " + this.name, e);
        }
    }

    /**
     * returns row with idColumnName == id, if exists
     *
     * @param id
     * @return
     */
    public Optional<T> findRowById(long id) {
        List<T> results = findRowsByLongColumn(this.idColumnName, id);

        assert results.size() < 2;

        if (!results.isEmpty()) {
            return Optional.of(results.get(0));
        } else {
            return Optional.empty();
        }
    }

    public List<T> findAllRows() {
        try {
            return MySQLRepositoryHelper.executeQuery(
                    // create SELECT query
                    MySQLQueryBuilder.buildSelectAllQuery(this.name, this.getAllColumnNames()),
                    // fill in PreparedStatement
                    preparedStatement -> {
                    },
                    this::createObjectFromResultSet,
                    CONNECTION_POOL);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find row in " + this.name, e);
        }
    }

    public List<T> findRowsByLongColumn(String longColumnName, long columnValue) {
        if (!hasColumn(longColumnName)) {
            throw new NoSuchElementException("No such column found '" + longColumnName + "'");
        }
        try {
            return MySQLRepositoryHelper.executeQuery(
                    // create SELECT query
                    MySQLQueryBuilder.buildSelectByIdQuery(this.name, this.getAllColumnNames(), longColumnName),
                    // fill in PreparedStatement
                    preparedStatement -> {
                        preparedStatement.setLong(1, columnValue);
                        LOGGER.info("Building query: inserting to column '" + longColumnName + "' at position " + 1);
                    },
                    this::createObjectFromResultSet,
                    CONNECTION_POOL);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find row in " + this.name, e);
        }
    }


    /**
     * return list of all column names, with the same order as this.nonIdColumns
     * but with id column at first position
     *
     * @return
     */
    private List<String> getAllColumnNames() {
        return Stream.concat(Stream.of(this.idColumnName),
                        this.nonIdColumns.stream().map(Column::name))
                .toList();
    }

    /**
     * return list of column names (without id column), with the same order as this.nonIdColumns
     *
     * @return
     */
    private List<String> getNonIdColumnNames() {
        return this.nonIdColumns.stream()
                .map(Column::name)
                .toList();
    }

    private Column<T> getNonIdColumnByName(String name) {
        return this.nonIdColumns.stream()
                .filter(column -> column.name().equals(name))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("No such column found '" + name + "'"));
    }

    /**
     * checks whether this table has column with given name
     * this checks also idColumnName, so true result cannot guarantee
     * that getNonIdColumnByName won't throw exception
     *
     * @param name name of the column to check
     * @return whether this table has specified column
     */
    private boolean hasColumn(String name) {
        return Stream.concat(Stream.of(this.idColumnName),
                        this.nonIdColumns.stream().map(Column::name))
                .anyMatch(columnName -> columnName.equals(name));
    }


    private T createObjectFromResultSet(ResultSet resultSet) throws SQLException {
        T rowData = this.rowDataCreator.get();

        rowData.setId(resultSet.getLong(this.idColumnName));

        for (Column<T> column : this.nonIdColumns) {
            column.resultSaver().save(resultSet, column.name(), rowData);
        }

        return rowData;
    }


    /**
     * @param name
     * @param statementSetter used to fill in prepared statement in insert and
     *                        update operations (and only in those)
     * @param resultSaver     saves element from ResultSet to U (rowData) type
     * @param <U>             type of object storing row data
     */
    public record Column<U>(
            String name,
            StatementSetter<U> statementSetter,
            ResultSaver<U> resultSaver
    ) {
    }

    /**
     * Fills on field of prepared statement from object of type U
     *
     * @param <U>
     */
    @FunctionalInterface
    public interface StatementSetter<U> {
        void set(PreparedStatement preparedStatement, int parameterIndex, U source) throws SQLException;
    }

    /**
     * Saves data from one field of  ResultSet to object
     *
     * @param <U>
     */
    @FunctionalInterface
    public interface ResultSaver<U> {
        void save(ResultSet resultSet, String columnLabel, U source) throws SQLException;
    }
}
