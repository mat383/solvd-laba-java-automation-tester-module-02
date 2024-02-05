package com.solvd.laba.football.persistence.impl.mybatis.util;

import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @param <T> class of objects in repository
 * @param <R> class of repository itself
 */
public class MyBatisQueryHelper<T, R> {
    private final Class<R> repositoryClass;

    public MyBatisQueryHelper(Class<R> repositoryClass) {
        this.repositoryClass = repositoryClass;
    }

    public void executeUpdate(Consumer<R> operationExecutor) {
        try (SqlSession sqlSession = MyBatisConfig.getSqlSessionFactory().openSession(true)) {
            R repository = sqlSession.getMapper(this.repositoryClass);
            operationExecutor.accept(repository);
        }
    }

    /**
     * executes query that returns single row
     */
    public Optional<T> executeSingleRowQuery(Function<R, Optional<T>> operationExecutor) {
        try (SqlSession sqlSession = MyBatisConfig.getSqlSessionFactory().openSession(true)) {
            R repository = sqlSession.getMapper(this.repositoryClass);
            return operationExecutor.apply(repository);
        }
    }

    /**
     * same as executeSingleRowQuery, but return type uses generic
     * so can be different from <T></T>
     */
    public <S> Optional<S> executeSingleRowQueryGenericReturn(Function<R, Optional<S>> operationExecutor) {
        try (SqlSession sqlSession = MyBatisConfig.getSqlSessionFactory().openSession(true)) {
            R repository = sqlSession.getMapper(this.repositoryClass);
            return operationExecutor.apply(repository);
        }
    }

    /**
     * executes query that returns multiple rows
     */
    public List<T> executeMultiRowQuery(Function<R, List<T>> operationExecutor) {
        try (SqlSession sqlSession = MyBatisConfig.getSqlSessionFactory().openSession(true)) {
            R repository = sqlSession.getMapper(this.repositoryClass);
            return operationExecutor.apply(repository);
        }
    }
}
