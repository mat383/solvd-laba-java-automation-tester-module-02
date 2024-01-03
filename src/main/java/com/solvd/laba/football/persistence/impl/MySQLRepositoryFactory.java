package com.solvd.laba.football.persistence.impl;

import com.solvd.laba.football.persistence.PersonRepository;
import com.solvd.laba.football.persistence.PlayerRepository;
import com.solvd.laba.football.persistence.RepositoryProvider;
import com.solvd.laba.football.persistence.TeamRepository;

public class MySQLRepositoryProvider implements RepositoryProvider {
    private MySQLPersonRepositoryImpl personRepository;
    private MySQLPlayerRepositoryImpl playerRepository;
    private MySQLTeamRepositoryImpl teamRepository;

    @Override
    public PersonRepository getPersonRepository() {
        if (this.personRepository == null) {
            this.personRepository = new MySQLPersonRepositoryImpl();
        }
        return this.personRepository;
    }

    @Override
    public PlayerRepository getPlayerRepository() {
        if (this.playerRepository == null) {
            this.playerRepository = new MySQLPlayerRepositoryImpl();
        }
        return this.playerRepository;
    }

    @Override
    public TeamRepository getTeamRepository() {
        if (this.teamRepository == null) {
            this.teamRepository = new MySQLTeamRepositoryImpl();
        }
        return this.teamRepository;
    }
}
