package com.example.laura.madgame2.gamelogic;

import java.util.List;

/**
 * Created by Alex on 18.04.2017.
 */


public class Player {
    private boolean cheater;

    private boolean pauseNextTurn;

    private int playerNr;

    private Field startField;

    private List<Field> finishFields;

    private List<Figure> figures;


    public Player(int playerNr) {
        this.playerNr = playerNr;
        cheater=false;
        pauseNextTurn=false;
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

    void setFigures(List<Figure> figures) {
        this.figures = figures;
    }

    public boolean getCheater(){
       return cheater;
    }

    public  void setCheater(boolean cheat){
       cheater=cheat;
   }

    Field getFinishField(int fieldNr) {
        return finishFields.get(fieldNr);
    }

    void setFinishFields(List<Field> finishFields) {
        this.finishFields = finishFields;
    }

    public boolean getPauseNextTurn(){
        return this.pauseNextTurn;
    }

    public void setPauseNextTurn(boolean pauseNextTurn){
        this.pauseNextTurn=pauseNextTurn;
    }
}

