package com.example.laura.madgame2.gamestate;

import java.util.ArrayList;
import java.util.List;

class OtherPlayersTurnState extends AbstractState {

    @Override
    List<Action> chooseFigure(Controller context, int playerNr, int figureNr) {
        // TODO nice-to-have functionality: highlight result from enemy's potential move, if he has rolled his dice yet OR ignore action
        Action fig = new UpdatePlayerFigure(playerNr,figureNr);
        ArrayList<Action> list = new ArrayList<>();
        list.add(fig);
        return list;


    }

    @Override
    boolean rollDice(Controller context) {
        return false; // ignore action
    }

    @Override
    List<Action> diceRollResult(Controller context, int result, boolean hasCheated) {
        // ignore action
        Action dice = new UpdateDiceRoll(result);
        ArrayList<Action> list = new ArrayList<>();
        list.add(dice);
        return list;
    }
}
