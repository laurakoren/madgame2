package com.example.laura.madgame2.gamestate.action;

public class MoveFigureAction implements Action {
    public final int playerNr;
    public final int figureNr;
    public final int fieldNr;
    public final boolean moveToFinishField;

    public MoveFigureAction(int playerNr, int figureNr, int fieldNr, boolean moveToFinishField) {
        this.playerNr = playerNr;
        this.figureNr = figureNr;
        this.fieldNr = fieldNr;
        this.moveToFinishField = moveToFinishField;
    }
}
