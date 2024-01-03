package com.solvd.laba.football.persistence;

import com.solvd.laba.football.domain.Person;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends IRepository<Person> {
    @Override
    void create(Person person);

    @Override
    void update(Person person);

    @Override
    void delete(Person person);

    @Override
    Optional<Person> findById(long id);

    @Override
    List<Person> findAll();
}
