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
    public void create(Team team, long clubId, Long leagueId, Integer leaguePosition) {
        if ((leaguePosition != null && leagueId == null)
                || (leaguePosition == null && leagueId != null)) {
            throw new IllegalArgumentException(
                    "League position and league id both have to be either null or have some  value.");
        }

        // TODO add support for failure
        this.teamRepository.create(team, clubId, leagueId, leaguePosition);
        for (Player player : team.getPlayers()) {
            if (this.playerService.findById(player.getId()).isPresent()) {
                this.playerService.update(player, team.getId());
            } else {
                this.playerService.create(player, team.getId());
            }
        }
    }

    @Override
    public void update(Team team, long clubId, Long leagueId, Integer leaguePosition) {
        if ((leaguePosition != null && leagueId == null)
                || (leaguePosition == null && leagueId != null)) {
            throw new IllegalArgumentException(
                    "League position and league id both have to be either null or have some  value.");
        }

        // TODO add support for failure
        this.teamRepository.update(team, clubId, leagueId, leaguePosition);
        for (Player player : team.getPlayers()) {
            if (this.playerService.findById(player.getId()).isPresent()) {
                this.playerService.update(player, team.getId());
            } else {
                this.playerService.create(player, team.getId());
            }
        }
    }

    @Override
    public void delete(Team team) {
        // TODO add support for failure
        for (Player player : team.getPlayers()) {
            this.playerService.delete(player);
        }
        this.teamRepository.delete(team);
    }

    @Override
    public Optional<Team> findById(long id) {
        Optional<Team> team = this.teamRepository.findById(id);
        team.ifPresent(this::loadPlayers);
        return team;
    }

    @Override
    public List<Team> findAll() {
        List<Team> teams = this.teamRepository.findAll();
        teams.forEach(this::loadPlayers);
        return teams;
    }

    @Override
    public List<Team> findByClubId(long id) {
        List<Team> teams = this.teamRepository.findByClubId(id);
        teams.forEach(this::loadPlayers);
        return teams;
    }


    /**
     * loads players belonging to team and adds them to team
     */
    private void loadPlayers(Team team) {
        this.playerService.findByTeamId(team.getId())
                .forEach(team::addPlayer);
    }
}
