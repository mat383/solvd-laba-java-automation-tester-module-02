package com.solvd.laba.football.persistence.impl.mybatis.util;

import com.solvd.laba.football.persistence.PositionRepository;
import lombok.Getter;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MyBatisConfig {
    @Getter
    private static final SqlSessionFactory sqlSessionFactory;

    static {
        try (InputStream configStream = Resources.getResourceAsStream("mybatis-config.xml")) {
            sqlSessionFactory = new SqlSessionFactoryBuilder()
                    .build(configStream);
            SqlSession sqlSession = sqlSessionFactory.openSession(true);
            PositionRepository positionRepository = sqlSession.getMapper(PositionRepository.class);

        } catch (IOException e) {
            throw new RuntimeException("Unable to initialize MyBatis", e);
        }
    }
}
