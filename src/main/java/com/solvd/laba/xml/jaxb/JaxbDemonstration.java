package com.solvd.laba.xml.jaxb;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;

public class JaxbDemonstration {
    public static void main(String[] args) {
        System.out.println("hello");
        try {
            JAXBContext positionsContext = JAXBContext.newInstance(PositionsList.class);
            Unmarshaller positionsUnmarshaller = positionsContext.createUnmarshaller();

            PositionsList positionsList = (PositionsList) positionsUnmarshaller.unmarshal(new File("src/main/resources/positions.xml"));
            System.out.printf("loaded %d positions\n", positionsList.getPositions().size());
            for (var position : positionsList.getPositions()) {
                System.out.printf("- (%d) %s\n", position.getId(), position.getName());
            }

            JAXBContext gamesContext = JAXBContext.newInstance(GamesList.class);
            Unmarshaller gamesUnmarshaller = gamesContext.createUnmarshaller();

            GamesList gamesList = (GamesList) gamesUnmarshaller.unmarshal(new File("src/main/resources/games.xml"));
            System.out.printf("loaded %d games\n", gamesList.getGames().size());
            for (var position : gamesList.getGames()) {
                System.out.printf("- (%d) %s\n", position.getId(), position.getName());
            }

            JAXBContext teamsContext = JAXBContext.newInstance(TeamsList.class);
            Unmarshaller teamsUnmarshaller = teamsContext.createUnmarshaller();

            /*TeamsList teamsList = (TeamsList) teamsUnmarshaller.unmarshal(new File("src/main/resources/teams.xml"));
            System.out.println(gamesList.getGames().size());
            for (var team : teamsList.getTeams()) {
                System.out.printf("- (%d) %s\n", team.getId(), team.getName());
            }*/
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
