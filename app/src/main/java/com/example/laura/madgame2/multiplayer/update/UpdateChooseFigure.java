package com.example.laura.madgame2.multiplayer.update;

/**
 * Created by Philipp on 23.05.17.
 */

public class UpdateChooseFigure extends Update {

    private int playerNr;
    private int figureNr;

    public UpdateChooseFigure(int playerNr, int figureNr) {
        this.playerNr = playerNr;
        this.figureNr = figureNr;
    }

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

    @Override
    public String toString() {
        return "UpdateChooseFigure{" +
                "playerNr=" + playerNr +
                ", figureNr=" + figureNr +
                '}';
    }
}
