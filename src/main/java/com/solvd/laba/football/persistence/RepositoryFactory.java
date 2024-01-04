package com.solvd.laba.football.persistence;

public interface RepositoryFactory {
    PersonRepository createPersonRepository();

    PlayerRepository createPlayerRepository();

    TeamRepository createTeamRepository();

    PositionRepository createPositionRepository();

    ShootOutcomeRepository createShootOutcomeRepository();
}
