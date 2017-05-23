package com.example.laura.madgame2.multiplayer.update;

/**
 * Created by Philipp on 23.05.17.
 */

public class UpdateRolledDice extends Update {

    private int diceResult;
    private int playerNr;

    public UpdateRolledDice(int playerNr, int diceResult) {
        super();
        this.diceResult = diceResult;
    }

    public int getDiceResult() {
        return diceResult;
    }

    public void setDiceResult(int diceResult) {
        this.diceResult = diceResult;
    }

    public int getPlayerNr() {
        return playerNr;
    }

    public void setPlayerNr(int playerNr) {
        this.playerNr = playerNr;
    }

    @Override
    public String toString() {
        return "UpdateRolledDice{" +
                "diceResult=" + diceResult +
                ", playerNr=" + playerNr +
                '}';
    }
}
