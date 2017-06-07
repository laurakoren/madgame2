package com.example.laura.madgame2.multiplayer.update;

/**
 * Created by Philipp on 07.06.17.
 */

public class UpdateMyNumber extends Update {

    private int myNumber = -9999;
    private  int playerNr = 0;

    public UpdateMyNumber(int myNumber, int playerNr) {
        this.myNumber = myNumber;
        this.playerNr = playerNr;
    }

    public int getMyNumber() {
        return myNumber;
    }

    public void setMyNumber(int myNumber) {
        this.myNumber = myNumber;
    }

    @Override
    public int getPlayerNr() {
        return playerNr;
    }

    @Override
    public void setPlayerNr(int playerNr) {
        this.playerNr = playerNr;
    }
}
