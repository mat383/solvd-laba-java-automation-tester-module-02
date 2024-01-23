package com.solvd.laba.football.persistence.impl.jdbc;

import com.solvd.laba.football.persistence.*;

public class RepositoryFactoryJDBC implements RepositoryFactory {

    @Override
    public GameRepository createGameRepository() {
        return new GameRepositoryJDBC();
    }

    @Override
    public GoalAttemptRepository createGoalAttemptRepository() {
        return new GoalAttemptRepositoryJDBC();
    }

    @Override
    public PenaltyShotRepository createPenaltyShotRepository() {
        return new PenaltyShotRepositoryJDBC();
    }

    @Override
    public PersonRepository createPersonRepository() {
        return new PersonRepositoryJDBC();
    }

    @Override
    public PlayerPerformanceRepository createPlayerPerformanceRepository() {
        return new PlayerPerformanceRepositoryJDBC();
    }

    @Override
    public PlayerRepository createPlayerRepository() {
        return new PlayerRepositoryJDBC();
    }

    @Override
    public TeamRepository createTeamRepository() {
        return new TeamRepositoryJDBC();
    }

    @Override
    public PositionRepository createPositionRepository() {
        return new PositionRepositoryJDBC();
    }

    @Override
    public ShootOutcomeRepository createShootOutcomeRepository() {
        return new ShootOutcomeRepositoryJDBC();
    }
}
