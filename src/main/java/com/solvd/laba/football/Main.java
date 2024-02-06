package com.solvd.laba.football;


import com.solvd.laba.football.domain.*;
import com.solvd.laba.football.persistence.*;
import com.solvd.laba.football.persistence.impl.jdbc.RepositoryFactoryJDBC;
import com.solvd.laba.football.persistence.impl.mybatis.*;
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


        //mybatis test
        PositionRepository myBatisPositionRepository = new PositionRepositoryMyBatis();
        PersonRepository myBatisPersonRepository = new PersonRepositoryMyBatis();
        ShootOutcomeRepository myBatisShootOutcomeRepository = new ShootOutcomeRepositoryMyBatis();
        PenaltyShotRepository myBatisPenaltyShootRepository = new PenaltyShotRepositoryMyBatis();
        GoalAttemptRepository myBatisGoalAttemptRepository = new GoalAttemptRepositoryMyBatis();
        PlayerPerformanceRepository myBatisPlayerPerformanceRepository = new PlayerPerformanceRepositoryMyBatis();

        System.out.println("-- positions");
        for (Position position : myBatisPositionRepository.findAll()) {
            System.out.printf("- (%d) %s\n", position.getId(), position.getName());
        }
        System.out.println("-- people");
        for (Person person : myBatisPersonRepository.findAll()) {
            System.out.printf("- (%d) %s %s (%s)\n", person.getId(), person.getFirstName(), person.getLastName(), person.getBirthDate().toString());
        }
        System.out.println("-- shoot outcome");
        for (ShootOutcome shootOutcome : myBatisShootOutcomeRepository.findAll()) {
            System.out.printf("- (%d) %s\n", shootOutcome.getId(), shootOutcome.getName());
        }

        System.out.println("-- penalty shoot");
        for (PenaltyShot penaltyShot : myBatisPenaltyShootRepository.findAll()) {
            System.out.printf("- (%d) %s - (%s) %s\n",
                    penaltyShot.getId(),
                    penaltyShot.getGameTime(),
                    penaltyShot.getOutcome().getId(),
                    penaltyShot.getOutcome().getName());
        }

        System.out.println("-- goal attempt");
        for (GoalAttempt goalAttempt : myBatisGoalAttemptRepository.findAll()) {
            System.out.printf("- (%d) %s - (%s) %s [%s]\n",
                    goalAttempt.getId(),
                    goalAttempt.getGameTime(),
                    goalAttempt.getOutcome().getId(),
                    goalAttempt.getOutcome().getName(),
                    goalAttempt.getOutcome().toString());
        }

        // TODO check if positions are cached and point to the same object

        System.out.println("-- player performance");
        for (PlayerPerformance playerPerformance : myBatisPlayerPerformanceRepository.findAll()) {
            System.out.printf("- (%d) %.2f %.2f %.2f\n",
                    playerPerformance.getId(),
                    playerPerformance.getDefensivePerformance(),
                    playerPerformance.getOffensivePerformance(),
                    playerPerformance.getCooperativePerformance());
        }
    }
}