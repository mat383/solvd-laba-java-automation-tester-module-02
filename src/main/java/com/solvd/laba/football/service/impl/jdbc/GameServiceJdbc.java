package com.solvd.laba.football.service.impl.jdbc;

import com.solvd.laba.football.domain.Game;
import com.solvd.laba.football.domain.PlayerPerformance;
import com.solvd.laba.football.persistence.GameRepository;
import com.solvd.laba.football.service.GameService;
import com.solvd.laba.football.service.PlayerPerformanceService;
import com.solvd.laba.football.service.PlayerService;
import com.solvd.laba.football.service.TeamService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class GameServiceJdbc implements GameService {
    GameRepository gameRepository;
    TeamService teamService;
    PlayerService playerService;
    PlayerPerformanceService playerPerformanceService;

    @Override
    public void create(Game game) {
        this.gameRepository.create(game);
    }

    @Override
    public void update(Game game) {
        this.gameRepository.update(game);
    }

    @Override
    public void delete(Game game) {
        this.gameRepository.delete(game);
    }

    @Override
    public Optional<Game> findById(long id) {
        Optional<Game> game = this.gameRepository.findById(id);
        game.ifPresent(this::loadDetails);
        return game;
    }

    @Override
    public List<Game> findAll() {
        List<Game> games = this.gameRepository.findAll();
        games.forEach(this::loadDetails);
        return games;
    }

    /**
     * Load details related to game:
     * - player performances that take part in the game
     * - team that play
     *
     * @param game
     */
    private void loadDetails(Game game) {
        List<PlayerPerformance> playerPerformances =
                this.playerPerformanceService.findByGameId(game.getId());

        // find teams taking part in the game
        Long idOfTeamA = playerPerformances.get(0).getGame().getId();
        Long idOfTeamB = playerPerformances.stream().unordered()
                .filter(playerPerformance -> !playerPerformance.getTeam().getId().equals(idOfTeamA))
                .findAny()
                .get().getTeam().getId();

        assert idOfTeamA != null;
        assert idOfTeamB != null;

        // load teams
        game.setTeamA(this.teamService.findById(idOfTeamA).get());
        game.setTeamB(this.teamService.findById(idOfTeamB).get());

        // update player performances to refer to loaded team
        for (PlayerPerformance playerPerformance : playerPerformances) {
            if (playerPerformance.getTeam().getId().equals(idOfTeamA)) {
                playerPerformance.setTeam(game.getTeamA());
            } else if (playerPerformance.getTeam().getId().equals(idOfTeamB)) {
                playerPerformance.setTeam(game.getTeamB());
            } else {
                throw new RuntimeException("Error: player performance's team id " +
                        "doesn't match any of two teams that take part in game. " +
                        "Game id: " + game.getId() + ", player performance id: " + playerPerformance.getId()
                        + ", non matching team id: " + playerPerformance.getTeam().getId());
            }
        }

    }
}
