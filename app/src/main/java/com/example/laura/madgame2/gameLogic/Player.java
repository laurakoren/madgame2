package com.example.laura.madgame2.gameLogic;

import java.util.List;

/**
 * Created by Alex on 18.04.2017.
 */


public class Player {

    private int playerNr;

    private Field startField;

    private List<Figure> figures;

    public Player(int playerNr) {
        this.playerNr = playerNr;
    }

    public int getPlayerNr() {
        return playerNr;
    }

    public Field getStartField() {
        return startField;
    }

    void setStartField(Field startField) {
        this.startField = startField;
    }

    public Figure getFigure(int figureNr) {
        if (figures == null || figureNr < 0 || figureNr >= figures.size())
            return null;
        else
            return figures.get(figureNr);
    }

    public List<Figure> getFigures() {
        return figures;
    }

    void setFigures(List<Figure> figures) {
        this.figures = figures;
    }

    public boolean equals(Player that) {
        return this.getPlayerNr() == that.getPlayerNr();
    }
}

