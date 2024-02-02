package com.solvd.laba.xml.sax;

import com.solvd.laba.football.domain.*;
import com.solvd.laba.football.domain.interfaces.Identifiable;
import lombok.Getter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class SaxTeamsHandler extends DefaultHandler {
    @Getter
    private final List<Team> teams = new ArrayList<>();
    private Team parsedTeam;
    private Player parsedPlayer;
    private Person parsedPerson;
    private PlayerPerformance parsedPlayerPerformance;
    private StringBuilder tagContent = new StringBuilder();
    private final List<Position> allPositions;
    private final List<Game> allGames;

    /**
     * @param allPositions list of all positions, used when resolving references from parsed file
     * @param allGames     list of all games, used when resolving references from parsed file
     */
    public SaxTeamsHandler(List<Position> allPositions, List<Game> allGames) {
        this.allPositions = List.copyOf(allPositions);
        this.allGames = List.copyOf(allGames);
    }


    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case "team":
                this.parsedTeam = new Team();
                this.parsedTeam.setId(Long.parseLong(attributes.getValue("id")));
                break;
            case "player":
                this.parsedPlayer = new Player();
                this.parsedPlayer.setId(Long.parseLong(attributes.getValue("id")));
                break;
            case "person":
                this.parsedPerson = new Person();
                this.parsedPerson.setId(Long.parseLong(attributes.getValue("id")));
                break;
            case "playerPerformance":
                this.parsedPlayerPerformance = new PlayerPerformance();
                this.parsedPlayerPerformance.setId(Long.parseLong(attributes.getValue("id")));
                break;
            case "preferredPositionRef":
                Position preferredPosition = new Position();
                preferredPosition.setId(Long.parseLong(attributes.getValue("id")));
                this.parsedPlayer.setPreferredPosition(preferredPosition);
                break;
            case "positionRef":
                Position position = new Position();
                position.setId(Long.parseLong(attributes.getValue("id")));
                this.parsedPlayerPerformance.setPosition(position);
                break;
            case "gameRef":
                Game game = new Game();
                game.setId(Long.parseLong(attributes.getValue("id")));
                this.parsedPlayerPerformance.setGame(game);
                break;
            case "teamRef":
                Team team = new Team();
                team.setId(Long.parseLong(attributes.getValue("id")));
                this.parsedPlayerPerformance.setTeam(team);
                break;

            // clear StringBuilder for tags that contains text
            case "name":
            case "creationDate":
            case "closureDate":
            case "firstName":
            case "lastName":
            case "birthDate":
            case "defensivePerformance":
            case "offensivePerformance":
            case "cooperativePerformance":
            case "start":
            case "end":
                this.tagContent.setLength(0);
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case "team":
                this.teams.add(parsedTeam);
                break;
            case "name":
                this.parsedTeam.setName(this.tagContent.toString());
                break;
            case "creationDate":
                this.parsedTeam.setCreationDate(
                        XmlFieldParsers.parseDate(this.tagContent.toString()));
                break;
            case "closureDate":
                this.parsedTeam.setClosureDate(
                        XmlFieldParsers.parseDate(this.tagContent.toString()));
                break;
            case "player":
                this.parsedTeam.addPlayer(parsedPlayer);
                break;
            case "person":
                this.parsedPlayer.setPerson(parsedPerson);
                break;
            case "firstName":
                this.parsedPerson.setFirstName(this.tagContent.toString());
                break;
            case "lastName":
                this.parsedPerson.setLastName(this.tagContent.toString());
                break;
            case "birthDate":
                this.parsedPerson.setBirthDate(
                        XmlFieldParsers.parseDate(this.tagContent.toString()));
                break;
            case "playerPerformance":
                parsedPlayer.addPlayerPerformance(parsedPlayerPerformance);
                break;
            case "defensivePerformance":
                parsedPlayerPerformance.setDefensivePerformance(
                        Double.parseDouble(this.tagContent.toString()));
                break;
            case "offensivePerformance":
                parsedPlayerPerformance.setOffensivePerformance(
                        Double.parseDouble(this.tagContent.toString()));
                break;
            case "cooperativePerformance":
                parsedPlayerPerformance.setCooperativePerformance(
                        Double.parseDouble(this.tagContent.toString()));
                break;
            case "start":
                parsedPlayerPerformance.setStart(
                        XmlFieldParsers.parseTime(this.tagContent.toString()));
                break;
            case "end":
                parsedPlayerPerformance.setEnd(
                        XmlFieldParsers.parseTime(this.tagContent.toString()));
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        this.tagContent.append(ch, start, length);
    }

    @Override
    public void endDocument() throws SAXException {
        resolveReferences();
    }

    private void resolveReferences() {
        for (Team team : this.teams) {
            for (Player player : team.getPlayers()) {
                // resolve preferred position reference
                player.setPreferredPosition(
                        findElementById(this.allPositions, player.getPreferredPosition().getId())
                );
                for (PlayerPerformance playerPerformance : player.getPlayerPerformances()) {
                    // resolve position reference
                    playerPerformance.setPosition(
                            findElementById(this.allPositions,
                                    playerPerformance.getPosition().getId())
                    );
                    // resolve game reference
                    playerPerformance.setGame(
                            findElementById(this.allGames,
                                    playerPerformance.getGame().getId())
                    );
                    // resolve team reference
                    playerPerformance.setTeam(
                            findElementById(this.teams,
                                    playerPerformance.getTeam().getId())
                    );
                }
            }
        }
    }

    private <T extends Identifiable> T findElementById(List<T> elements, long id) {
        return elements.stream()
                .filter(position -> position.getId().equals(id))
                .findAny()
                .orElseThrow(() -> {
                    return new RuntimeException("Unable to find element with id = " + id);
                });
    }
}

