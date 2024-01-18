package com.solvd.laba.football.service.impl;

import com.solvd.laba.football.domain.Person;
import com.solvd.laba.football.domain.Player;
import com.solvd.laba.football.domain.PlayerPerformance;
import com.solvd.laba.football.domain.Position;
import com.solvd.laba.football.persistence.PlayerRepository;
import com.solvd.laba.football.service.PersonService;
import com.solvd.laba.football.service.PlayerPerformanceService;
import com.solvd.laba.football.service.PlayerService;
import com.solvd.laba.football.service.PositionService;
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
    @NonNull
    private final PlayerPerformanceService playerPerformanceService;
    @NonNull
    private final PositionService positionService;

    @Override
    public void create(Player player, long teamId) {
        // TODO add support for failure
        createOrUpdatePerson(player.getPerson());

        this.playerRepository.create(player, teamId);

        createOrUpdatePlayerPerformances(player);
        createOrUpdatePreferredPosition(player.getPreferredPosition());
    }

    @Override
    public void update(Player player, long teamId) {
        // TODO add support for failure
        createOrUpdatePerson(player.getPerson());

        this.playerRepository.update(player, teamId);

        createOrUpdatePlayerPerformances(player);
        createOrUpdatePreferredPosition(player.getPreferredPosition());
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
    public List<Player> findByTeamId(long id) {
        return this.playerRepository.findByTeamId(id);
    }


    /**
     * loads & fills in player object with:
     * player performances
     * position
     *
     * @param player
     */
    private void loadDetails(Player player) {

    }


    private void createOrUpdatePerson(Person person) {
        // TODO avoid unnecessary calls
        if (this.personService.findById(person.getId()).isPresent()) {
            this.personService.update(person);
        } else {
            this.personService.create(person);
        }
    }

    private void createOrUpdatePlayerPerformances(Player player) {
        // TODO avoid unnecessary calls
        for (PlayerPerformance playerPerformance : player.getPlayerPerformances()) {
            if (this.playerPerformanceService.findById(playerPerformance.getId()).isPresent()) {
                this.playerPerformanceService.update(playerPerformance);
            } else {
                this.playerPerformanceService.create(playerPerformance);
            }
        }
    }

    private void createOrUpdatePreferredPosition(Position preferredPosition) {
        // TODO avoid unnecessary calls
        if (this.positionService.findById(preferredPosition.getId()).isPresent()) {
            this.positionService.update(preferredPosition);
        } else {
            this.positionService.create(preferredPosition);
        }
    }

}
