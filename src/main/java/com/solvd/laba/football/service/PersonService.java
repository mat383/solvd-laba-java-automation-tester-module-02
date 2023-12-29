package com.solvd.laba.football.service;

import com.solvd.laba.football.domain.Person;

import java.util.List;

public interface PersonService {
    void create(Person person);

    void update(Person person);

    void delete(Person person);

    Person findByID(long id);

    List<Person> findAll();
}
