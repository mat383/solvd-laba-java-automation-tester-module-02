package com.solvd.laba.football;


import com.solvd.laba.football.domain.Person;
import com.solvd.laba.football.domain.Position;
import com.solvd.laba.football.domain.ShootOutcome;
import com.solvd.laba.football.persistence.RepositoryFactory;
import com.solvd.laba.football.persistence.impl.RepositoryFactoryMySQL;
import com.solvd.laba.football.service.PersonService;
import com.solvd.laba.football.service.PositionService;
import com.solvd.laba.football.service.ShootOutcomeService;
import com.solvd.laba.football.service.impl.PersonServiceImpl;
import com.solvd.laba.football.service.impl.PositionServiceImpl;
import com.solvd.laba.football.service.impl.ShootOutcomeServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        RepositoryFactory repositoryFactory = new RepositoryFactoryMySQL();

        PersonService personService = new PersonServiceImpl(
                repositoryFactory.createPersonRepository());
        PositionService positionService = new PositionServiceImpl(
                repositoryFactory.createPositionRepository());
        ShootOutcomeService shootOutcomeService = new ShootOutcomeServiceImpl(
                repositoryFactory.createShootOutcomeRepository());

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
    }
}