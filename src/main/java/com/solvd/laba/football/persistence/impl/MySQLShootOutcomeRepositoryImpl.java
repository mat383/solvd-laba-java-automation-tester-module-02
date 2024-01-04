package com.solvd.laba.football.persistence.impl;

import com.solvd.laba.football.domain.ShootOutcome;
import com.solvd.laba.football.persistence.ShootOutcomeRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MySQLShootOutcomeRepositoryImpl implements ShootOutcomeRepository {
    private final MySQLTable<ShootOutcome> shootOutcomesTable = new MySQLTable<>(
            "shoot_outcomes",
            "id",
            List.of(
                    new MySQLTable.Column<>("name",
                            MySQLShootOutcomeRepositoryImpl::nameResultSetter,
                            MySQLShootOutcomeRepositoryImpl::nameResultSaver)
            ),
            () -> new ShootOutcome(null, null)
    );

    @Override
    public void create(ShootOutcome shootOutcome) {
        shootOutcomesTable.insertRow(shootOutcome);
    }

    @Override
    public void update(ShootOutcome shootOutcome) {
        shootOutcomesTable.updateRow(shootOutcome);
    }

    @Override
    public void delete(ShootOutcome shootOutcome) {
        shootOutcomesTable.deleteRow(shootOutcome);
    }

    @Override
    public Optional<ShootOutcome> findById(long id) {
        return Optional.empty();
    }

    @Override
    public List<ShootOutcome> findAll() {
        return null;
    }


    private static void nameResultSetter(PreparedStatement preparedStatement, int parameterIndex, ShootOutcome shootOutcome) throws SQLException {
        preparedStatement.setString(parameterIndex, shootOutcome.getName());
    }

    private static void nameResultSaver(ResultSet resultSet, String columnLabel, ShootOutcome shootOutcome) throws SQLException {
        shootOutcome.setName(resultSet.getString(columnLabel));
    }
}
