package com.solvd.laba.football.persistence.impl.mybatis;

import com.solvd.laba.football.domain.Person;
import com.solvd.laba.football.persistence.PersonRepository;
import com.solvd.laba.football.persistence.impl.mybatis.util.MyBatisConfig;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Optional;

public class PersonRepositoryMyBatis implements PersonRepository {
    @Override
    public void create(Person object) {
        try (SqlSession sqlSession = MyBatisConfig.getSqlSessionFactory().openSession(true)) {
            PersonRepository personRepository = sqlSession.getMapper(PersonRepository.class);
            personRepository.create(object);
        }
    }

    @Override
    public void update(Person object) {
        try (SqlSession sqlSession = MyBatisConfig.getSqlSessionFactory().openSession(true)) {
            PersonRepository personRepository = sqlSession.getMapper(PersonRepository.class);
            personRepository.update(object);
        }
    }

    @Override
    public void delete(Person object) {
        try (SqlSession sqlSession = MyBatisConfig.getSqlSessionFactory().openSession(true)) {
            PersonRepository personRepository = sqlSession.getMapper(PersonRepository.class);
            personRepository.delete(object);
        }

    }

    @Override
    public Optional<Person> findById(long id) {
        try (SqlSession sqlSession = MyBatisConfig.getSqlSessionFactory().openSession(true)) {
            PersonRepository personRepository = sqlSession.getMapper(PersonRepository.class);
            return personRepository.findById(id);
        }
    }

    @Override
    public List<Person> findAll() {
        try (SqlSession sqlSession = MyBatisConfig.getSqlSessionFactory().openSession(true)) {
            PersonRepository personRepository = sqlSession.getMapper(PersonRepository.class);
            return personRepository.findAll();
        }
    }
}
