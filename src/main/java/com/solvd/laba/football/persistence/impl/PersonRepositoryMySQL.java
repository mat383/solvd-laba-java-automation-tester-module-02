package com.solvd.laba.football.persistence.impl;

import com.solvd.laba.football.domain.Person;
import com.solvd.laba.football.persistence.PersonRepository;
import com.solvd.laba.football.persistence.impl.util.MySQLConnectionPool;
import com.solvd.laba.football.persistence.impl.util.MySQLRepositoryHelper;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PersonRepositoryMySQL implements PersonRepository {
    private static final Logger LOGGER = LogManager.getLogger(PersonRepositoryMySQL.class.getName());
    private static final MySQLConnectionPool CONNECTION_POOL = MySQLConnectionPool.getInstance();


    /**
     * inserts person into database
     *
     * @param person
     */
    @Override
    public void create(@NonNull Person person) {
        if (person.hasSetId()) {
            throw new IllegalArgumentException("Person cannot have id set");
        }
        try {
            MySQLRepositoryHelper.UpdateResult result = MySQLRepositoryHelper.executeUpdateGetKeys(
                    "INSERT INTO people(first_name, last_name, birth_date) VALUES (?, ?, ?);",
                    preparedStatement -> {
                        preparedStatement.setString(1, person.getFirstName());
                        preparedStatement.setString(2, person.getLastName());
                        preparedStatement.setDate(3, Date.valueOf(person.getBirthDate()));
                    },
                    CONNECTION_POOL);
            assert result.affectedRows() == 1;
            assert result.generatedKeys().size() == 1;
            person.setId(result.generatedKeys().get(0));
        } catch (SQLException e) {
            throw new RuntimeException("Unable create person", e);
        }
    }

    @Override
    public void update(@NonNull Person person) {
        if (!person.hasSetId()) {
            throw new IllegalArgumentException("Unable to update person that doesn't have id.");
        }
        try {
            int affectedRows = MySQLRepositoryHelper.executeUpdate(
                    "UPDATE people SET first_name=?, last_name=?, birth_date=? WHERE id=?;",
                    preparedStatement -> {
                        preparedStatement.setString(1, person.getFirstName());
                        preparedStatement.setString(2, person.getLastName());
                        preparedStatement.setDate(3, Date.valueOf(person.getBirthDate()));
                        preparedStatement.setLong(4, person.getId());
                    },
                    CONNECTION_POOL);
            assert affectedRows == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Unable to update person", e);
        }
    }

    // TODO: return bool to indicate if deletion occured?
    @Override
    public void delete(@NonNull Person person) {
        if (!person.hasSetId()) {
            throw new IllegalArgumentException("Unable to delete person that doesn't have id.");
        }
        try {
            int affectedRows = MySQLRepositoryHelper.executeUpdate(
                    "DELETE FROM people WHERE id=?;",
                    preparedStatement -> {
                        preparedStatement.setLong(1, person.getId());
                    },
                    CONNECTION_POOL);
            assert affectedRows < 2;
        } catch (SQLException e) {
            throw new RuntimeException("Unable to delete person", e);
        }
    }

    @Override
    public Optional<Person> findById(long id) {
        Optional<Person> person = Optional.empty();
        try {
            List<Person> results = MySQLRepositoryHelper.executeQuery(
                    "SELECT id, first_name, last_name, birth_date FROM people WHERE id=?;",
                    preparedStatement -> preparedStatement.setLong(1, id),
                    PersonRepositoryMySQL::createPersonFromResultSet,
                    CONNECTION_POOL);
            assert results.size() < 2;

            if (!results.isEmpty()) {
                person = Optional.of(results.get(0));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find person", e);
        }
        return person;
    }

    @Override
    public List<Person> findAll() {
        try {
            return MySQLRepositoryHelper.executeQuery(
                    "SELECT id, first_name, last_name, birth_date FROM people;",
                    preparedStatement -> {},
                    PersonRepositoryMySQL::createPersonFromResultSet,
                    CONNECTION_POOL);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to find person", e);
        }
    }


    /**
     * generates person from resultSet uses current position
     * doesn't change cursor position
     *
     * @param resultSet
     * @return
     */
    static private Person createPersonFromResultSet(ResultSet resultSet) throws SQLException {
        return new Person(resultSet.getLong("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getDate("birth_date").toLocalDate());
    }
}
