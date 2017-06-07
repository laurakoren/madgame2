package com.example.laura.madgame2.gamestate.action;

public class KickFigureAction implements Action {
    public final int playerNr;
    public final int figureNr;

    public KickFigureAction(int playerNr, int figureNr) {
        this.playerNr = playerNr;
        this.figureNr = figureNr;
    }
}
