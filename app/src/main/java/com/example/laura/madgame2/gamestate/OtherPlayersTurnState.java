package com.example.laura.madgame2.gamestate;

import com.example.laura.madgame2.gamestate.action.Action;

import java.util.List;

class OtherPlayersTurnState extends AbstractState {

    @Override
    List<Action> chooseFigure(Controller context, int playerNr, int figureNr) {
        // TODO nice-to-have functionality: highlight result from enemy's potential move, if he has rolled his dice yet OR ignore action
        return null; // ignore action
    }

    @Override
    boolean rollDice(Controller context) {
        return false; // ignore action
    }

    @Override
    List<Action> diceRollResult(Controller context, int result, boolean hasCheated) {
        return null; // ignore action
    }
}
