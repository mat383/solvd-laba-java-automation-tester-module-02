package com.solvd.laba.football.persistence.impl;

import com.solvd.laba.football.domain.ShootOutcome;
import com.solvd.laba.football.persistence.ShootOutcomeRepository;
import com.solvd.laba.football.persistence.impl.util.MySQLTable;

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
        this.shootOutcomesTable.insertRow(shootOutcome);
    }

    @Override
    public void update(ShootOutcome shootOutcome) {
        this.shootOutcomesTable.updateRow(shootOutcome);
    }

    @Override
    public void delete(ShootOutcome shootOutcome) {
        this.shootOutcomesTable.deleteRow(shootOutcome);
    }

    @Override
    public Optional<ShootOutcome> findById(long id) {
        return this.shootOutcomesTable.findRowById(id);
    }

    @Override
    public List<ShootOutcome> findAll() {
        return this.shootOutcomesTable.findAllRows();
    }


    private static void nameResultSetter(PreparedStatement preparedStatement, int parameterIndex, ShootOutcome shootOutcome) throws SQLException {
        preparedStatement.setString(parameterIndex, shootOutcome.getName());
    }

    private static void nameResultSaver(ResultSet resultSet, String columnLabel, ShootOutcome shootOutcome) throws SQLException {
        shootOutcome.setName(resultSet.getString(columnLabel));
    }
}
