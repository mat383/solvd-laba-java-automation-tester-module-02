package com.solvd.laba.football.persistence.impl.mybatis;

import com.solvd.laba.football.domain.PlayerPerformance;
import com.solvd.laba.football.persistence.PlayerPerformanceRepository;
import com.solvd.laba.football.persistence.impl.mybatis.util.MyBatisQueryHelper;

import java.util.List;
import java.util.Optional;

public class PlayerPerformanceRepositoryMyBatis implements PlayerPerformanceRepository {
    private final MyBatisQueryHelper<PlayerPerformance, PlayerPerformanceRepository> queryHelper =
            new MyBatisQueryHelper<>(PlayerPerformanceRepository.class);

    @Override
    public void create(PlayerPerformance object) {
        this.queryHelper.executeUpdate(repository -> repository.create(object));
    }

    @Override
    public void update(PlayerPerformance object) {
        this.queryHelper.executeUpdate(repository -> repository.update(object));
    }

    @Override
    public void delete(PlayerPerformance object) {
        this.queryHelper.executeUpdate(repository -> repository.delete(object));
    }

    @Override
    public Optional<PlayerPerformance> findById(long id) {
        return this.queryHelper.executeSingleRowQuery(repository -> repository.findById(id));
    }

    @Override
    public List<PlayerPerformance> findAll() {
        return this.queryHelper.executeMultiRowQuery(repository -> repository.findAll());
    }

    @Override
    public List<PlayerPerformance> findByPlayerId(long id) {
        return this.queryHelper.executeMultiRowQuery(repository -> repository.findByPlayerId(id));
    }

    @Override
    public List<PlayerPerformance> findByGameId(long id) {
        return this.queryHelper.executeMultiRowQuery(repository -> repository.findByGameId(id));
    }
}
