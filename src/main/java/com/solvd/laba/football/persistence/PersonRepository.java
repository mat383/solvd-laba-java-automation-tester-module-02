package com.solvd.laba.football.persistence;

import com.solvd.laba.football.domain.Person;

import java.util.List;
import java.util.Optional;

public interface PersonRepository {
    void create(Person person);

    void update(Person person);

    void delete(Person person);

    Optional<Person> findById(long id);

    List<Person> findAll();
}
