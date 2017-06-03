package com.example.laura.madgame2.multiplayer.update;

/**
 * Created by Philipp on 23.05.17.
 */

public class UpdateDraw extends Update {

    private int playerNr;
    private int figureNr;
    private int diceResult;

    public UpdateDraw(int playerNr, int figureNr, int diceResult) {
        super();
        this.diceResult = diceResult;
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

    public int getDiceResult() {
        return diceResult;
    }

    public void setDiceResult(int diceResult) {
        this.diceResult = diceResult;
    }

    @Override
    public String toString() {
        return "UpdateDraw{" +
                "playerNr=" + playerNr +
                ", figureNr=" + figureNr +
                '}';
    }




}