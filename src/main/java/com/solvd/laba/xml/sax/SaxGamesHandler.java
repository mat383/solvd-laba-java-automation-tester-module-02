package com.solvd.laba.xml.sax;

import com.solvd.laba.football.domain.Game;
import lombok.Getter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class SaxGamesHandler extends DefaultHandler {

    @Getter
    private List<Game> games = new ArrayList<>();
    private Game parsedGame;
    private StringBuilder tagContent = new StringBuilder();


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case "game":
                this.parsedGame = new Game();
                this.parsedGame.setId(Long.parseLong(attributes.getValue("id")));
                break;
            case "name":
            case "time":
            case "duration":
            case "firstHalfDuration":
                this.tagContent.setLength(0);
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case "game":
                this.games.add(parsedGame);
                break;
            case "name":
                this.parsedGame.setName(this.tagContent.toString());
                break;
            case "time":
                this.parsedGame.setTime(
                        XmlFieldParsers.parseDateTime(this.tagContent.toString()));
                break;
            case "duration":
                this.parsedGame.setDuration(
                        XmlFieldParsers.parseDuration(this.tagContent.toString()));
                break;
            case "firstHalfDuration":
                this.parsedGame.setFirstHalfDuration(
                        XmlFieldParsers.parseDuration(this.tagContent.toString()));
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        this.tagContent.append(ch, start, length);
    }
}

