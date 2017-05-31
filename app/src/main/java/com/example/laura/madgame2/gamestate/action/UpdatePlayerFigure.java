package com.example.laura.madgame2.gamestate.action;

/**
 * Created by Alex on 30.05.2017.
 */

public class UpdatePlayerFigure implements Action{

    public int getPlayerNr() {
        return playerNr;
    }

    public void setPlayerNr(int playerNr) {
        this.playerNr = playerNr;
    }

    public int getFigureNr() {
        return figureNr;
    }

    public void setFigureNr(int figureNr) {
        this.figureNr = figureNr;
    }

    int playerNr,figureNr;

    public UpdatePlayerFigure(int playerNr, int figureNr){
        this.figureNr=figureNr;
        this.playerNr=playerNr;
    }

}
