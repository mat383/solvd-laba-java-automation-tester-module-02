package com.solvd.laba.football.persistence.impl.mybatis;

import com.solvd.laba.football.domain.ShootOutcome;
import com.solvd.laba.football.persistence.IRepository;
import com.solvd.laba.football.persistence.ShootOutcomeRepository;
import com.solvd.laba.football.persistence.impl.mybatis.util.MyBatisQueryHelper;

import java.util.List;
import java.util.Optional;

public class ShootOutcomeRepositoryMyBatis implements ShootOutcomeRepository {
    private final MyBatisQueryHelper<ShootOutcome, ShootOutcomeRepository> queryHelper =
            new MyBatisQueryHelper<>(ShootOutcomeRepository.class);

    @Override
    public void create(ShootOutcome object) {
        this.queryHelper.executeUpdate(shootOutcomeRepository -> shootOutcomeRepository.create(object));
    }

    @Override
    public void update(ShootOutcome object) {
        this.queryHelper.executeUpdate(shootOutcomeRepository -> shootOutcomeRepository.update(object));
    }

    @Override
    public void delete(ShootOutcome object) {
        this.queryHelper.executeUpdate(shootOutcomeRepository -> shootOutcomeRepository.delete(object));
    }

    @Override
    public Optional<ShootOutcome> findById(long id) {
        return this.queryHelper.executeSingleRowQuery(shootOutcomeRepository -> shootOutcomeRepository.findById(id));
    }

    @Override
    public List<ShootOutcome> findAll() {
        return this.queryHelper.executeMultiRowQuery(IRepository::findAll);
    }
}
