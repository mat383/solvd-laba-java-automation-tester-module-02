package com.solvd.laba.football.service.impl.jdbc;

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
public class PlayerServiceJdbc implements PlayerService {
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
        for (PlayerPerformance playerPerformance : player.getPlayerPerformances()) {
            this.playerPerformanceService.delete(playerPerformance);
        }
        this.playerRepository.delete(player);
        this.personService.delete(player.getPerson());
    }

    @Override
    public Optional<Player> findById(long id) {
        return this.playerRepository.findById(id).map(this::loadDetails);
    }

    @Override
    public List<Player> findAll() {
        return this.playerRepository.findAll().stream()
                .map(this::loadDetails)
                .toList();
    }


    @Override
    public List<Player> findByTeamId(long id) {
        return this.playerRepository.findByTeamId(id).stream()
                .map(this::loadDetails)
                .toList();
    }


    /**
     * loads & fills in player object with:
     * player performances
     * position
     * person
     *
     * @return returns passed player object
     */
    private Player loadDetails(Player player) {
        // load & add player performances
        this.playerPerformanceService
                .findByPlayerId(player.getId())
                .forEach(player::addPlayerPerformance);

        // load & add preferred position
        Position fullyLoadedPreferredPosition = this.positionService
                .findById(player.getPreferredPosition().getId()).get();
        player.setPreferredPosition(fullyLoadedPreferredPosition);

        // load & add person
        Person fullyLoadedPerson = this.personService
                .findById(player.getPerson().getId()).get();
        player.setPerson(fullyLoadedPerson);

        return player;
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
