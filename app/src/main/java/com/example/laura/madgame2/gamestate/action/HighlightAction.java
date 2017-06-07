package com.example.laura.madgame2.gamestate.action;

/**
 * Created by Alex on 31.05.2017.
 */

public class HighlightAction implements Action {

    public final int playerNr;
    public final int fieldNr;
    public final boolean isFinishField;

    public HighlightAction(int playerNr, int fieldNr, boolean isFinishField) {
        this.playerNr = playerNr;
        this.fieldNr = fieldNr;
        this.isFinishField = isFinishField;
    }
}
