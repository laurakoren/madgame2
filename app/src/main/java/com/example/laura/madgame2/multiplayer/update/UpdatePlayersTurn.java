package com.example.laura.madgame2.multiplayer.update;

import java.util.List;

/**
 * Created by Alex on 31.05.2017.
 */

public class UpdatePlayersTurn extends Update {

    private int playerNr;
    private  boolean playerBeforeCheated;
    private List<Integer> playerCaughtCheating;

    public UpdatePlayersTurn(int playerNr, boolean playerBeforeCheated, List<Integer> playerCaughtCheating){
        this.playerNr=playerNr;
        this.playerBeforeCheated = playerBeforeCheated;
        this.playerCaughtCheating = playerCaughtCheating;
    }

    public int getPlayerNr() {
        return playerNr;
    }

    public void setPlayerNr(int playerNr) {
        this.playerNr = playerNr;
    }

    public boolean isPlayerBeforeCheated() {
        return playerBeforeCheated;
    }

    public List<Integer> getPlayerCaughtCheating() {
        return playerCaughtCheating;
    }
}
