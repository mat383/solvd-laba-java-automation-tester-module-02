package com.solvd.laba.football.service;

import com.solvd.laba.football.domain.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    void create(Person person);

    void update(Person person);

    void delete(Person person);

    Optional<Person> findById(long id);

    List<Person> findAll();
}
