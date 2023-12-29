package com.solvd.laba.football;


import com.solvd.laba.football.domain.Person;
import com.solvd.laba.football.persistence.PersonRepository;
import com.solvd.laba.football.persistence.impl.PersonRepositoryJDBCImpl;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        PersonRepository personRepository = new PersonRepositoryJDBCImpl();
        Person person = new Person(9, "Person9", "Last9", LocalDate.of(1998, 9, 23));
        personRepository.create(person);

        System.out.println("Hello world.");
    }
}