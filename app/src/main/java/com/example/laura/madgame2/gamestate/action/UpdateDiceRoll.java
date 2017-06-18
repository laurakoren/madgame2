package com.example.laura.madgame2.gamestate.action;

/**
 * Created by Alex on 30.05.2017.
 */

public class UpdateDiceRoll implements Action {
    int diceRollResult;

    public UpdateDiceRoll(int diceRollResult) {
        this.diceRollResult = diceRollResult;
    }

    public int getDiceRollResult() {
        return diceRollResult;
    }

    public void setDiceRollResult(int diceRollResult) {
        this.diceRollResult = diceRollResult;
    }
}
