package com.example.laura.madgame2.gamestate;

/**
 * Created by Alex on 30.05.2017.
 */

public class UpdateDiceRoll implements Action{
    int diceRollResult;

    public UpdateDiceRoll(int diceRollResult){
        this.diceRollResult =diceRollResult;
    }
}
