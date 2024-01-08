package com.solvd.laba.football.persistence.impl;

import com.solvd.laba.football.persistence.*;

public class RepositoryFactoryMySQL implements RepositoryFactory {

    @Override
    public PersonRepository createPersonRepository() {
        return new PersonRepositoryMySQL();
    }

    @Override
    public PlayerRepository createPlayerRepository() {
        return new PlayerRepositoryMySQL();
    }

    @Override
    public TeamRepository createTeamRepository() {
        return new TeamRepositoryMySQL();
    }

    @Override
    public PositionRepository createPositionRepository() {
        return new PositionRepositoryMySQL();
    }

    @Override
    public ShootOutcomeRepository createShootOutcomeRepository() {
        return new ShootOutcomeRepositoryMySQL();
    }
}
