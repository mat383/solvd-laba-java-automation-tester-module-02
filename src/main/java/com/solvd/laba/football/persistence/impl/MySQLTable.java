package com.solvd.laba.football.persistence.impl;

import com.solvd.laba.football.domain.interfaces.Identifiable;
import com.solvd.laba.football.persistence.impl.util.MySQLConnectionPool;
import com.solvd.laba.football.persistence.impl.util.MySQLRepositoryHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

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
                    MySQLRepositoryHelper.buildInsertQuery(this.name, getNonIdColumnNames()),
                    // fill in PreparedStatement
                    preparedStatement -> {
                        for (int i = 0; i < this.nonIdColumns.size(); i++) {
                            this.nonIdColumns.get(i).statementSetter().set(preparedStatement, i + 1, rowData);
                            LOGGER.info("Inserting to column '" + this.nonIdColumns.get(i).name() + "' at position " + (i + 1));
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
                    MySQLRepositoryHelper.buildUpdateQuery(this.name, getNonIdColumnNames(), List.of(this.idColumnName)),
                    // fill in PreparedStatement
                    preparedStatement -> {
                        for (int i = 0; i < this.nonIdColumns.size(); i++) {
                            this.nonIdColumns.get(i).statementSetter().set(preparedStatement, i + 1, rowData);
                            LOGGER.info("Inserting to column '" + this.nonIdColumns.get(i).name() + "' at position " + (i + 1));
                        }
                        preparedStatement.setLong(this.nonIdColumns.size() + 1, rowData.getId());
                        LOGGER.info("Inserting to column '" + this.idColumnName + "' at position " + (this.nonIdColumns.size() + 1));
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
                    MySQLRepositoryHelper.buildDeleteQuery(this.name, List.of(this.idColumnName)),
                    // fill in PreparedStatement
                    preparedStatement -> {
                        preparedStatement.setLong(1, rowData.getId());
                        LOGGER.info("Inserting to column '" + this.idColumnName + "' at position " + 1);
                    },
                    CONNECTION_POOL);
            assert affectedRows < 2;
        } catch (SQLException e) {
            throw new RuntimeException("Unable to delete row in " + this.name, e);
        }
    }

    public Optional<T> findRowById(long id) {
        Optional<T> player = Optional.empty();
        try {
            List<T> results = MySQLRepositoryHelper.executeQuery(
                    // create SELECT query
                    // TODO
                    "SELECT id, person_id FROM players WHERE id=?;",
                    // fill in PreparedStatement
                    preparedStatement -> preparedStatement.setLong(1, id),
                    this::createObjectFromResultSet,
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

    //public List<T> findAllRows(long id)
    //public List<T> findRowsByLongColumn(String longColumnName)


    /**
     * return list of column names, with the same order as this.nonIdColumns
     *
     * @return
     */
    private List<String> getNonIdColumnNames() {
        return this.nonIdColumns.stream()
                .map(Column::name)
                .toList();
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
     * @param statementSetter
     * @param resultSaver     saves element from ResultSet to U (rowData) type
     * @param <U>             type of object storing row data
     */
    protected record Column<U>(
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
    protected interface StatementSetter<U> {
        void set(PreparedStatement preparedStatement, int parameterIndex, U source) throws SQLException;
    }

    /**
     * Saves data from one field of  ResultSet to object
     *
     * @param <U>
     */
    @FunctionalInterface
    protected interface ResultSaver<U> {
        void save(ResultSet resultSet, String columnLabel, U source) throws SQLException;
    }
}
