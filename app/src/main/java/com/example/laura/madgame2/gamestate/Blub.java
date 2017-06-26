package com.example.laura.madgame2.gamestate;

import com.example.laura.madgame2.gamestate.action.Action;

import java.util.List;

/**
 * Created by Alex on 26.06.2017.
 */

public class Blub implements State{

    @Override
    public List<Action> chooseFigure(Controller context, int playerNr, int figureNr) {
        return null;
    }

    @Override
    public boolean rollDice(Controller context) {
        return false;
    }

    @Override
    public List<Action> diceRollResult(Controller context, int result, boolean hasCheated) {
        return null;
    }

    @Override
    public List<Action> catchCheater(boolean playerBeforeHasCheated, Controller context) {
        return null;
    }
}
