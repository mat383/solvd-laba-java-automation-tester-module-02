package com.solvd.laba.football.persistence.impl;

import com.solvd.laba.football.persistence.*;

public class MySQLRepositoryFactory implements RepositoryFactory {

    @Override
    public PersonRepository createPersonRepository() {
        return new MySQLPersonRepositoryImpl();
    }

    @Override
    public PlayerRepository createPlayerRepository() {
        return new MySQLPlayerRepositoryImpl();
    }

    @Override
    public TeamRepository createTeamRepository() {
        return new MySQLTeamRepositoryImpl();
    }

    @Override
    public PositionRepository createPositionRepository() {
        return new MySQLPositionRepositoryImpl();
    }

    @Override
    public ShootOutcomeRepository createShootOutcomeRepository() {
        return new MySQLShootOutcomeRepositoryImpl();
    }
}
