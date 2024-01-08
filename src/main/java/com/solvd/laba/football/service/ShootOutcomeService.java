package com.solvd.laba.football.service;

import com.solvd.laba.football.domain.ShootOutcome;

import java.util.List;
import java.util.Optional;

public interface ShootOutcomeService {
    void create(ShootOutcome shootOutcome);

    void update(ShootOutcome shootOutcome);

    void delete(ShootOutcome shootOutcome);

    Optional<ShootOutcome> findById(long id);

    List<ShootOutcome> findAll();
}
