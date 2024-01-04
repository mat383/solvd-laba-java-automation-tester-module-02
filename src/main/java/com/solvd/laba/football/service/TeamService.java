package com.solvd.laba.football.service;

import com.solvd.laba.football.domain.Team;

import java.util.List;
import java.util.Optional;

public interface TeamService {
    void create(Team team, long clubId, Long leagueId, Integer leaguePosition);

    void update(Team team, long clubId, Long leagueId, Integer leaguePosition);

    void delete(Team team);

    Optional<Team> findById(long id);

    List<Team> findAll();

    List<Team> findByClubId(long id);
}
