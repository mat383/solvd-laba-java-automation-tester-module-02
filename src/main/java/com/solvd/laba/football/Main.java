package com.solvd.laba.football;


import com.solvd.laba.football.domain.*;
import com.solvd.laba.football.persistence.RepositoryFactory;
import com.solvd.laba.football.persistence.impl.jdbc.*;
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
                new GoalAttemptRepositoryJDBC());
        PenaltyShootService penaltyShootService = new PenaltyShotServiceImpl(
                new PenaltyShotRepositoryJDBC());
        PlayerPerformanceServiceImpl playerPerformanceService = new PlayerPerformanceServiceImpl(
                new PlayerPerformanceRepositoryJDBC(),
                goalAttemptService,
                penaltyShootService,
                positionService);
        PlayerService playerService = new PlayerServiceImpl(new PlayerRepositoryJDBC(),
                personService, playerPerformanceService, positionService);

        List<Person> people = personService.findAll();
        for (Person personTest : people) {
            System.out.println(personTest.getFirstName() + " " + personTest.getLastName());
        }

        List<Position> positions = positionService.findAll();
        for (Position position : positions) {
            System.out.println(position.getId() + " " + position.getName());
        }

        ShootOutcome shootOutcomeTest = new ShootOutcome(8L, null);
        List<ShootOutcome> shootOutcomes = shootOutcomeService.findAll();
        for (ShootOutcome so : shootOutcomes) {
            System.out.println(so.getId() + " - " + so.getName());
        }

        for (PlayerPerformance playerPerformance : playerPerformanceService.findAll()) {
            System.out.println("playerPerformanceId: " + playerPerformance.getId());
        }

        TeamService teamService = new TeamServiceImpl(new TeamRepositoryJDBC(), playerService);
        List<Team> teams = teamService.findAll();
        for (Team team : teams) {
            System.out.println(team.getName());
            team.getPlayers().forEach(player -> System.out.println(player.getPerson().getFirstName()));
            team.getPlayers().forEach(player -> System.out.println(player.getId()));
        }

        GameOutcomePredictorService gameOutcomePredictorService = new GameOutcomePredictorServiceImpl();
        Team winner = gameOutcomePredictorService.predictGameWinner(teams.get(3), teams.get(1));
        System.out.println("winner is: " + winner.getName());
    }
}