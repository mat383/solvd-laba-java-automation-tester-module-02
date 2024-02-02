package com.solvd.laba.xml.sax;

import com.solvd.laba.football.domain.Player;
import com.solvd.laba.football.domain.PlayerPerformance;
import com.solvd.laba.football.domain.Team;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class XmlReaderDemonstration {
    public static void main(String[] args) {
        // demonstration of xml parsing, schema validation is done
        // inside XmlReader, in checkSchemaAndThrowOnFailure

        String gamesFilePath = "src/main/resources/games.xml";
        String gamesSchemaFilePath = "src/main/resources/games.xsd";
        String positionsFilePath = "src/main/resources/positions.xml";
        String positionsSchemaFilePath = "src/main/resources/positions.xsd";
        String teamsFilePath = "src/main/resources/teams.xml";
        String teamsSchemaFilePath = "src/main/resources/teams.xsd";
        XmlReader xmlReader;

        try {
            xmlReader = new XmlReader(gamesFilePath, gamesSchemaFilePath,
                    positionsFilePath, positionsSchemaFilePath,
                    teamsFilePath, teamsSchemaFilePath);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }

        List<Team> teams;
        try {
            // parse & validate xml
            teams = xmlReader.readTeams();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (XmlSchemaException e) {
            throw new RuntimeException(e);
        }


        for (Team team : teams) {
            System.out.printf("- Team: %s\n", team.getName());
            for (Player player : team.getPlayers()) {
                System.out.printf("  - Player: %s, pref pos: %s\n",
                        player.getPerson().getFirstName(),
                        player.getPreferredPosition().getName());
                for (PlayerPerformance playerPerformance : player.getPlayerPerformances()) {
                    System.out.printf("    - Player Performance\n");
                    System.out.printf("      - Position: %s\n", playerPerformance.getPosition().getName());
                    System.out.printf("      - Game: %s\n", playerPerformance.getGame().getName());
                    System.out.printf("      - Team: %s\n", playerPerformance.getTeam().getName());
                    System.out.printf("      - Defensive Perf: %s\n", playerPerformance.getDefensivePerformance());
                    System.out.printf("      - Start: %s\n", playerPerformance.getStart());
                }
            }
        }
    }
}
