package com.solvd.laba.football.persistence.impl.mybatis;

import com.solvd.laba.football.domain.Position;
import com.solvd.laba.football.persistence.PositionRepository;
import com.solvd.laba.football.persistence.impl.mybatis.util.MyBatisConfig;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Optional;

public class PositionRepositoryMyBatis implements PositionRepository {

    @Override
    public void create(Position object) {
        try (SqlSession sqlSession = MyBatisConfig.getSqlSessionFactory().openSession(true)) {
            PositionRepository positionRepository = sqlSession.getMapper(PositionRepository.class);
            positionRepository.create(object);
        }
    }

    @Override
    public void update(Position object) {
        try (SqlSession sqlSession = MyBatisConfig.getSqlSessionFactory().openSession(true)) {
            PositionRepository positionRepository = sqlSession.getMapper(PositionRepository.class);
            positionRepository.update(object);
        }
    }

    @Override
    public void delete(Position object) {
        try (SqlSession sqlSession = MyBatisConfig.getSqlSessionFactory().openSession(true)) {
            PositionRepository positionRepository = sqlSession.getMapper(PositionRepository.class);
            positionRepository.delete(object);
        }
    }

    @Override
    public Optional<Position> findById(long id) {
        try (SqlSession sqlSession = MyBatisConfig.getSqlSessionFactory().openSession(true)) {
            PositionRepository positionRepository = sqlSession.getMapper(PositionRepository.class);
            return positionRepository.findById(id);
        }
    }

    @Override
    public List<Position> findAll() {
        try (SqlSession sqlSession = MyBatisConfig.getSqlSessionFactory().openSession(true)) {
            PositionRepository positionRepository = sqlSession.getMapper(PositionRepository.class);
            return positionRepository.findAll();
        }
    }
}
