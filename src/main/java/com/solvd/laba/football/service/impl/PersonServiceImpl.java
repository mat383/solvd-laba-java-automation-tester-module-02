package com.solvd.laba.football.service.impl;

import com.solvd.laba.football.domain.Person;
import com.solvd.laba.football.persistence.PersonRepository;
import com.solvd.laba.football.service.PersonService;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class PersonServiceImpl implements PersonService {
    @NonNull
    private final PersonRepository personRepository;

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
