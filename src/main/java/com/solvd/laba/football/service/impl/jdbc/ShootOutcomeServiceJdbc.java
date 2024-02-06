package com.solvd.laba.football.service.impl.jdbc;

import com.solvd.laba.football.domain.ShootOutcome;
import com.solvd.laba.football.persistence.ShootOutcomeRepository;
import com.solvd.laba.football.service.ShootOutcomeService;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public class ShootOutcomeServiceJdbc implements ShootOutcomeService {
    @NonNull
    private final ShootOutcomeRepository shootOutcomeRepository;

    public ShootOutcomeServiceJdbc(@NonNull ShootOutcomeRepository shootOutcomeRepository) {
        this.shootOutcomeRepository = shootOutcomeRepository;
    }


    @Override
    public void create(ShootOutcome shootOutcome) {
        this.shootOutcomeRepository.create(shootOutcome);
    }

    @Override
    public void update(ShootOutcome shootOutcome) {
        this.shootOutcomeRepository.update(shootOutcome);
    }

    @Override
    public void delete(ShootOutcome shootOutcome) {
        this.shootOutcomeRepository.delete(shootOutcome);
    }

    @Override
    public Optional<ShootOutcome> findById(long id) {
        return this.shootOutcomeRepository.findById(id);
    }

    @Override
    public List<ShootOutcome> findAll() {
        return this.shootOutcomeRepository.findAll();
    }
}
