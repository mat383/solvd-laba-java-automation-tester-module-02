package com.solvd.laba.football.service.impl;

import com.solvd.laba.football.domain.Position;
import com.solvd.laba.football.persistence.PositionRepository;
import com.solvd.laba.football.service.PositionService;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public class PositionServiceImpl implements PositionService {
    @NonNull
    private final PositionRepository positionRepository;

    public PositionServiceImpl(@NonNull PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }


    @Override
    public void create(Position position) {
        this.positionRepository.create(position);
    }

    @Override
    public void update(Position position) {
        this.positionRepository.update(position);
    }

    @Override
    public void delete(Position position) {
        this.positionRepository.delete(position);
    }

    @Override
    public Optional<Position> findById(long id) {
        return this.positionRepository.findById(id);
    }

    @Override
    public List<Position> findAll() {
        return this.positionRepository.findAll();
    }
}
