package com.solvd.laba.football;


import com.solvd.laba.football.domain.Team;
import com.solvd.laba.football.persistence.RepositoryFactory;
import com.solvd.laba.football.persistence.impl.jdbc.RepositoryFactoryJDBC;
import com.solvd.laba.football.service.*;
import com.solvd.laba.football.service.impl.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        RepositoryFactory repositoryFactory = new RepositoryFactoryJDBC();

        PersonService personService = new PersonServiceImpl(
                repositoryFactory.createPersonRepository());

        PositionService positionService = new PositionServiceImpl(
                repositoryFactory.createPositionRepository());

        ShootOutcomeService shootOutcomeService = new ShootOutcomeServiceImpl(
                repositoryFactory.createShootOutcomeRepository());

        GoalAttemptService goalAttemptService = new GoalAttemptServiceImpl(
                repositoryFactory.createGoalAttemptRepository());

        PenaltyShootService penaltyShootService = new PenaltyShotServiceImpl(
                repositoryFactory.createPenaltyShotRepository());

        PlayerPerformanceServiceImpl playerPerformanceService = new PlayerPerformanceServiceImpl(
                repositoryFactory.createPlayerPerformanceRepository(),
                goalAttemptService,
                penaltyShootService,
                positionService);

        PlayerService playerService = new PlayerServiceImpl(
                repositoryFactory.createPlayerRepository(),
                personService, playerPerformanceService, positionService);

        TeamService teamService = new TeamServiceImpl(
                repositoryFactory.createTeamRepository(), playerService);

        GameOutcomePredictorService gameOutcomePredictorService = new GameOutcomePredictorServiceImpl();

        List<Team> teams = teamService.findAll();

        // predict outcome
        Team winner = gameOutcomePredictorService.predictGameWinner(teams.get(3), teams.get(1));
        System.out.println("winner is: " + winner.getName());
    }
}