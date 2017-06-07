package com.example.laura.madgame2.multiplayer.update;

import java.io.Serializable;

/**
 * Created by Philipp on 21.05.17.
 */

public abstract class Update implements Serializable{

    public abstract int getPlayerNr();
    public abstract void setPlayerNr(int playerNr);
}
