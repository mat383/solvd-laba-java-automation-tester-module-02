package com.solvd.laba.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solvd.laba.xml.jaxb.GamesList;
import com.solvd.laba.xml.jaxb.PositionsList;

import java.io.File;
import java.io.IOException;

public class JsonDemonstration {
    public static void main(String[] args) {
        final File positionsFile = new File("src/main/resources/json/positions.json");
        final File gamesFile = new File("src/main/resources/json/games.json");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            PositionsList positionsList = objectMapper.readValue(positionsFile, PositionsList.class);

            System.out.printf("loaded %d positions\n", positionsList.getPositions().size());
            for (var position : positionsList.getPositions()) {
                System.out.printf("- (%d) %s\n", position.getId(), position.getName());
            }


            GamesList gamesList = objectMapper.readValue(gamesFile, GamesList.class);
            System.out.printf("loaded %d games\n", gamesList.getGames().size());
            for (var game : gamesList.getGames()) {
                System.out.printf("- (%d) %s at %s\n", game.getId(), game.getName(), game.getTime());
            }


            /*TeamsList teamsList = (TeamsList) teamsUnmarshaller.unmarshal(new File("src/main/resources/teams.xml"));
            System.out.println(gamesList.getGames().size());
            for (var team : teamsList.getTeams()) {
                System.out.printf("- (%d) %s\n", team.getId(), team.getName());
            }*/
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
