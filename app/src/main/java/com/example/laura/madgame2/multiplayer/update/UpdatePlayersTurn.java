package com.example.laura.madgame2.multiplayer.update;

/**
 * Created by Alex on 31.05.2017.
 */

public class UpdatePlayersTurn extends Update {

    private int playerNr;

    public UpdatePlayersTurn(int playerNr){
        this.playerNr=playerNr;
    }

    public int getPlayerNr() {
        return playerNr;
    }

    public void setPlayerNr(int playerNr) {
        this.playerNr = playerNr;
    }

}
