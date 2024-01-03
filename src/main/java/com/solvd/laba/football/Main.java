package com.solvd.laba.football;


import com.solvd.laba.football.domain.Person;
import com.solvd.laba.football.persistence.RepositoryFactory;
import com.solvd.laba.football.persistence.impl.MySQLRepositoryFactory;
import com.solvd.laba.football.service.PersonService;
import com.solvd.laba.football.service.impl.PersonServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        RepositoryFactory repositoryFactory = new MySQLRepositoryFactory();

        PersonService personService = new PersonServiceImpl(repositoryFactory.createPersonRepository());

        List<Person> people = personService.findAll();
        for (Person personTest : people) {
            System.out.println(personTest.getFirstName() + " " + personTest.getLastName());
        }

        System.out.println("Hello world.");
    }
}