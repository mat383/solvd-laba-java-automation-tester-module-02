package com.solvd.laba.football.persistence;

public interface RepositoryFactory {
    GameRepository createGameRepository();

    GoalAttemptRepository createGoalAttemptRepository();

    PenaltyShotRepository createPenaltyShotRepository();

    PersonRepository createPersonRepository();

    PlayerPerformanceRepository createPlayerPerformanceRepository();

    PlayerRepository createPlayerRepository();

    PositionRepository createPositionRepository();

    ShootOutcomeRepository createShootOutcomeRepository();

    TeamRepository createTeamRepository();
}
