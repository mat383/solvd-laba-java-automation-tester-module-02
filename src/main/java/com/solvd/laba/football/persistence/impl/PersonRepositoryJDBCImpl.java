package com.solvd.laba.football.persistence.impl;

import com.solvd.laba.football.domain.Person;
import com.solvd.laba.football.persistence.PersonRepository;
import org.apache.commons.lang3.NotImplementedException;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class PersonRepositoryJDBCImpl implements PersonRepository {
    private static final ConnectionPoolJDBC CONNECTION_POOL = ConnectionPoolJDBC.getInstance();


    /**
     * inserts person into database
     *
     * @param person
     */
    @Override
    public void create(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("person cannot be null");
        }

        // get connection
        Connection connection = null;
        try {
            connection = CONNECTION_POOL.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to get connection to database ", e);
        }

        // set connection settings
        try {
            connection.setAutoCommit(true);
            connection.setReadOnly(false);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to set connection settings ", e);
        }

        // insert into db & restore defaults
        final String INSERT_STATEMENT = "INSERT INTO people(id, first_name, last_name, birth_date) VALUES (?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_STATEMENT)) {
            preparedStatement.setLong(1, person.getId());
            preparedStatement.setString(2, person.getFirstName());
            preparedStatement.setString(3, person.getLastName());
            preparedStatement.setDate(4, Date.valueOf(person.getBirthDate()));
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            try {
                connection.setAutoCommit(true);
                connection.setReadOnly(false);
            } catch (SQLException f) {
                f.addSuppressed(e);
                throw new RuntimeException("unable to create person", f);
            }
            throw new RuntimeException("unable to create person", e);
        }

        // restore defaults
        try {
            connection.setAutoCommit(true);
            connection.setReadOnly(false);
        } catch (SQLException e) {
            throw new RuntimeException("unable to restore connection settings", e);
        }
    }

    @Override
    public void update(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("person cannot be null");
        }

        // get connection
        Connection connection = null;
        try {
            connection = CONNECTION_POOL.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to get connection to database ", e);
        }

        // set connection settings
        try {
            connection.setAutoCommit(true);
            connection.setReadOnly(false);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to set connection settings ", e);
        }

        // update & restore defaults
        final String UPDATE_STATEMENT = "UPDATE people SET first_name=?, last_name=?, birth_date=? WHERE id=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_STATEMENT)) {
            preparedStatement.setString(1, person.getFirstName());
            preparedStatement.setString(2, person.getLastName());
            preparedStatement.setDate(3, Date.valueOf(person.getBirthDate()));
            preparedStatement.setLong(4, person.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            try {
                connection.setAutoCommit(true);
                connection.setReadOnly(false);
            } catch (SQLException f) {
                f.addSuppressed(e);
                throw new RuntimeException("unable to create person", f);
            }
            throw new RuntimeException("unable to create person", e);
        }

        // restore defaults
        try {
            connection.setAutoCommit(true);
            connection.setReadOnly(false);
        } catch (SQLException e) {
            throw new RuntimeException("unable to restore connection settings", e);
        }
    }

    // TODO: return bool to indicate if deletion occured?
    @Override
    public void delete(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("person cannot be null");
        }

        // get connection
        Connection connection = null;
        try {
            connection = CONNECTION_POOL.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to get connection to database ", e);
        }

        // set connection settings
        try {
            connection.setAutoCommit(true);
            connection.setReadOnly(false);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to set connection settings ", e);
        }

        // delete from db & restore defaults
        final String DELETE_STATEMENT = "DELETE FROM people WHERE id=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_STATEMENT)) {
            preparedStatement.setLong(1, person.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            try {
                connection.setAutoCommit(true);
                connection.setReadOnly(false);
            } catch (SQLException f) {
                f.addSuppressed(e);
                throw new RuntimeException("unable to create person", f);
            }
            throw new RuntimeException("unable to create person", e);
        }

        // restore defaults
        try {
            connection.setAutoCommit(true);
            connection.setReadOnly(false);
        } catch (SQLException e) {
            throw new RuntimeException("unable to restore connection settings", e);
        }
    }

    @Override
    public Optional<Person> findById(long id) {
        // get connection
        Connection connection = null;
        try {
            connection = CONNECTION_POOL.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to get connection to database ", e);
        }

        // set connection settings
        try {
            connection.setAutoCommit(true);
            connection.setReadOnly(true);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to set connection settings ", e);
        }

        // get from db & restore defaults
        ResultSet results = null;
        final String GET_STATEMENT = "SELECT id, first_name, last_name, birth_date FROM people WHERE id=?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_STATEMENT)) {
            preparedStatement.setLong(1, id);
            results = preparedStatement.executeQuery();

        } catch (SQLException e) {
            try {
                connection.setAutoCommit(true);
                connection.setReadOnly(false);
            } catch (SQLException f) {
                f.addSuppressed(e);
                throw new RuntimeException("unable to create person", f);
            }
            throw new RuntimeException("unable to create person", e);
        }

        // restore defaults
        try {
            connection.setAutoCommit(true);
            connection.setReadOnly(false);
        } catch (SQLException e) {
            throw new RuntimeException("unable to restore connection settings", e);
        }

        // convert results to person
        /*if (results.next()) {

        } else {
            return Optional.empty();
        }*/
        return Optional.empty();
    }

    @Override
    public List<Person> findAll() {
        return null;
    }

    /**
     * generates person from resultSet uses current position
     * doesn't change cursor position
     *
     * @param resultSet
     * @return
     */
    private Person createPersonFromResultSet(ResultSet resultSet) {
        throw new NotImplementedException();
    }
}
