package com.solvd.laba.football.persistence;

/**
 * provides different repositories,
 * works like abstract factory, but caches once created objects
 */
public interface RepositoryProvider {
    PersonRepository getPersonRepository();

    PlayerRepository getPlayerRepository();

    TeamRepository getTeamRepository();
}
