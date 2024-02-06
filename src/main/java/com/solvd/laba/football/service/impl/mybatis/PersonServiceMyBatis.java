package com.solvd.laba.football.service.impl.mybatis;

import com.solvd.laba.football.domain.Person;
import com.solvd.laba.football.persistence.impl.mybatis.PersonRepositoryMyBatis;
import com.solvd.laba.football.service.PersonService;

import java.util.List;
import java.util.Optional;

public class PersonServiceMyBatis implements PersonService {
    private final PersonRepositoryMyBatis personRepository = new PersonRepositoryMyBatis();

    @Override
    public void create(Person person) {
        this.personRepository.create(person);
    }

    @Override
    public void update(Person person) {
        this.personRepository.update(person);
    }

    @Override
    public void delete(Person person) {
        this.personRepository.delete(person);
    }

    @Override
    public Optional<Person> findById(long id) {
        return this.personRepository.findById(id);
    }

    @Override
    public List<Person> findAll() {
        return this.personRepository.findAll();
    }
}
