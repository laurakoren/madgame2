package com.example.laura.madgame2.multiplayer.update;

/**
 * Created by max on 20.06.2017.
 */

public class UpdatePlayerWon extends Update {

    public final int winnerNr;
    public final String name;

    public UpdatePlayerWon(int winnerNr, String name) {
        this.winnerNr = winnerNr;
        this.name = name;
    }

    @Override
    public int getPlayerNr() {
        return winnerNr;
    }

    @Override
    public void setPlayerNr(int playerNr) {
        // ignored
    }
}
