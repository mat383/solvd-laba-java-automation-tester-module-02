package com.solvd.laba.football.service.impl;

import com.solvd.laba.football.domain.Player;
import com.solvd.laba.football.domain.Team;
import com.solvd.laba.football.persistence.TeamRepository;
import com.solvd.laba.football.service.PlayerService;
import com.solvd.laba.football.service.TeamService;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class TeamServiceImpl implements TeamService {
    @NonNull
    private final TeamRepository teamRepository;
    @NonNull
    private final PlayerService playerService;


    @Override
    public void create(Team team, long clubId, long leagueId, int leaguePosition) {
        // TODO add support for failure
        this.teamRepository.create(team, clubId, leagueId, leaguePosition);
        for (Player player : team.getPlayers()) {
            if (playerService.findById(player.getId()).isPresent()) {
                playerService.update(player, team.getId());
            } else {
                playerService.create(player, team.getId());
            }
        }
    }

    @Override
    public void update(Team team, long clubId, long leagueId, int leaguePosition) {
        // TODO add support for failure
        this.teamRepository.update(team, clubId, leagueId, leaguePosition);
        for (Player player : team.getPlayers()) {
            if (playerService.findById(player.getId()).isPresent()) {
                playerService.update(player, team.getId());
            } else {
                playerService.create(player, team.getId());
            }
        }
    }

    @Override
    public void delete(Team team) {
        // TODO add support for failure
        for (Player player : team.getPlayers()) {
            playerService.delete(player);
        }
        this.teamRepository.delete(team);
    }

    @Override
    public Optional<Team> findById(long id) {
        return this.teamRepository.findById(id);
    }

    @Override
    public List<Team> findAll() {
        return this.teamRepository.findAll();
    }

    @Override
    public List<Team> findByClubId(long id) {
        return this.teamRepository.findByClubId(id);
    }
}
