package com.solvd.laba.xml.sax;

import com.solvd.laba.football.domain.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class XmlReader {
    private final File gamesFile;
    private final Schema gamesSchema;
    private final File positionsFile;
    private final Schema positionsSchema;
    private final File teamsFile;
    private final Schema teamsSchema;
    private final SAXParser saxParser;

    public XmlReader(String gamesFilePath, String gamesSchemaFilePath,
                     String positionsFilePath, String positionsSchemaFilePath,
                     String teamsFilePath, String teamsSchemaFilePath) throws ParserConfigurationException, SAXException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        this.gamesFile = new File(gamesFilePath);
        this.gamesSchema = schemaFactory.newSchema(new File(gamesSchemaFilePath));

        this.positionsFile = new File(positionsFilePath);
        this.positionsSchema = schemaFactory.newSchema(new File(positionsSchemaFilePath));

        this.teamsFile = new File(teamsFilePath);
        this.teamsSchema = schemaFactory.newSchema(new File(teamsSchemaFilePath));

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        this.saxParser = saxParserFactory.newSAXParser();
    }

    public List<Team> readTeams() throws IOException, XmlSchemaException {
        // check whether files conform to xsd schema
        checkSchemaAndThrowOnFailure();

        try {
            SaxPositionsHandler positionsHandler = new SaxPositionsHandler();
            SaxGamesHandler gamesHandler = new SaxGamesHandler();
            this.saxParser.parse(this.positionsFile, positionsHandler);
            this.saxParser.parse(this.gamesFile, gamesHandler);
            SaxTeamsHandler teamsHandler = new SaxTeamsHandler(
                    positionsHandler.getPositions(), gamesHandler.getGames());
            this.saxParser.parse(this.teamsFile, teamsHandler);

            for (Position position : positionsHandler.getPositions()) {
                System.out.printf("- %s (%d)\n", position.getName(), position.getId());
            }
            for (Game game : gamesHandler.getGames()) {
                System.out.printf("- %s (%s) %s (%s)\n",
                        game.getName(),
                        game.getTime(),
                        game.getDuration(),
                        game.getFirstHalfDuration());
            }

            for (Team team : teamsHandler.getTeams()) {
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

            return teamsHandler.getTeams();
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkSchemaAndThrowOnFailure() throws XmlSchemaException, IOException {
        Validator gamesValidator = this.gamesSchema.newValidator();
        try {
            gamesValidator.validate(new StreamSource(this.gamesFile));
        } catch (SAXException e) {
            throw new XmlSchemaException("Xml file '%s' doesn't conform to schema"
                    .formatted(this.gamesFile), e);
        }

        Validator positionsValidator = this.positionsSchema.newValidator();
        try {
            positionsValidator.validate(new StreamSource(this.positionsFile));
        } catch (SAXException e) {
            throw new XmlSchemaException("Xml file '%s' doesn't conform to schema"
                    .formatted(this.positionsFile), e);
        }

        Validator teamsValidator = this.teamsSchema.newValidator();
        try {
            teamsValidator.validate(new StreamSource(this.teamsFile));
        } catch (SAXException e) {
            throw new XmlSchemaException("Xml file '%s' doesn't conform to schema"
                    .formatted(this.teamsFile), e);
        }

    }
}
