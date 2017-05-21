package com.example.laura.madgame2.multiplayer;

import java.io.Serializable;

/**
 * Created by Philipp on 21.05.17.
 */

public class Update implements Serializable{

    private String type;

    public Update (String type){
        this.type = type;
    }

    @Override
    public String toString() {
        return "Update{" +
                "type='" + type + '\'' +
                '}';
    }
}
