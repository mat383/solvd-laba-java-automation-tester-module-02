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
            System.out.println(positionsList.getPositions().size());
            for (var position : positionsList.getPositions()) {
                System.out.printf("- (%d) %s\n", position.getId(), position.getName());
            }

            JAXBContext gamesContext = JAXBContext.newInstance(GamesList.class);
            Unmarshaller gamesUnmarshaller = gamesContext.createUnmarshaller();

            GamesList gamesList = (GamesList) gamesUnmarshaller.unmarshal(new File("src/main/resources/games.xml"));
            System.out.println(gamesList.getGames().size());
            for (var position : gamesList.getGames()) {
                System.out.printf("- (%d) %s\n", position.getId(), position.getName());
            }

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
