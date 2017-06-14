package com.example.laura.madgame2.gamestate.action;

import com.example.laura.madgame2.gamelogic.Player;

public class WinningAction implements Action {
    public final Player winner;
    public final String name;


    public WinningAction(Player winner, String name) {
        this.winner = winner;
        this.name = name;
    }
}
