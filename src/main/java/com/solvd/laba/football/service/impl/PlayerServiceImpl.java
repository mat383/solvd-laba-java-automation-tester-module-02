package com.solvd.laba.football.service.impl;

import com.solvd.laba.football.domain.Player;
import com.solvd.laba.football.persistence.PlayerRepository;
import com.solvd.laba.football.service.PersonService;
import com.solvd.laba.football.service.PlayerService;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    @NonNull
    private final PlayerRepository playerRepository;
    @NonNull
    private final PersonService personService;

    @Override
    public void create(Player player, long teamId) {
        // TODO add support for failure
        if (personService.findById(player.getPerson().getId()).isPresent()) {
            personService.update(player.getPerson());
        } else {
            personService.create(player.getPerson());
        }
        this.playerRepository.create(player, teamId);
    }

    @Override
    public void update(Player player, long teamId) {
        // TODO add support for failure
        personService.update(player.getPerson());
        this.playerRepository.update(player, teamId);
    }

    @Override
    public void delete(Player player) {
        // TODO add support for failure
        this.playerRepository.delete(player);
        personService.delete(player.getPerson());
    }

    @Override
    public Optional<Player> findById(long id) {
        return this.playerRepository.findById(id);
    }

    @Override
    public List<Player> findAll() {
        return this.playerRepository.findAll();
    }


    @Override
    public List<Player> findByTeamId(long teamId) {
        return this.playerRepository.findByTeamId(teamId);
    }
}
