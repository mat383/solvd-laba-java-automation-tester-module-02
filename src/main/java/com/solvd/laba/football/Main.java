package com.solvd.laba.football;


import com.solvd.laba.football.domain.Team;
import com.solvd.laba.football.persistence.RepositoryFactory;
import com.solvd.laba.football.persistence.impl.jdbc.RepositoryFactoryJDBC;
import com.solvd.laba.football.service.*;
import com.solvd.laba.football.service.impl.jdbc.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        RepositoryFactory repositoryFactory = new RepositoryFactoryJDBC();

        PersonService personService = new PersonServiceJdbc(
                repositoryFactory.createPersonRepository());

        PositionService positionService = new PositionServiceJdbc(
                repositoryFactory.createPositionRepository());

        ShootOutcomeService shootOutcomeService = new ShootOutcomeServiceJdbc(
                repositoryFactory.createShootOutcomeRepository());

        GoalAttemptService goalAttemptService = new GoalAttemptServiceJdbc(
                repositoryFactory.createGoalAttemptRepository());

        PenaltyShootService penaltyShootService = new PenaltyShotServiceJdbc(
                repositoryFactory.createPenaltyShotRepository());

        PlayerPerformanceServiceJdbc playerPerformanceService = new PlayerPerformanceServiceJdbc(
                repositoryFactory.createPlayerPerformanceRepository(),
                goalAttemptService,
                penaltyShootService,
                positionService);

        PlayerService playerService = new PlayerServiceJdbc(
                repositoryFactory.createPlayerRepository(),
                personService, playerPerformanceService, positionService);

        TeamService teamService = new TeamServiceJdbc(
                repositoryFactory.createTeamRepository(), playerService);

        GameOutcomePredictorService gameOutcomePredictorService = new GameOutcomePredictorServiceJdbc();

        List<Team> teams = teamService.findAll();

        // predict outcome
        Team winner = gameOutcomePredictorService.predictGameWinner(teams.get(3), teams.get(1));
        System.out.println("winner is: " + winner.getName());
    }
}