package com.solvd.laba.football.persistence.impl;

import com.solvd.laba.football.persistence.PersonRepository;
import com.solvd.laba.football.persistence.PlayerRepository;
import com.solvd.laba.football.persistence.RepositoryFactory;
import com.solvd.laba.football.persistence.TeamRepository;

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
}
