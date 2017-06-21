package com.example.laura.madgame2.multiplayer.update;

import java.util.List;

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

    @Override
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



    @Override
    public String toString() {
        return "UpdateDraw{" +
                "playerNr=" + playerNr +
                ", figureNr=" + figureNr +
                '}';
    }




}
