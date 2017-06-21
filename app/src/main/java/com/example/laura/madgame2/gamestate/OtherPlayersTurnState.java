package com.example.laura.madgame2.gamestate;


import com.example.laura.madgame2.gamestate.action.Action;
import com.example.laura.madgame2.gamestate.action.NotificationAction;

import java.util.ArrayList;
import java.util.List;

import static com.example.laura.madgame2.gamestate.action.NotificationAction.Type.TOAST;

class OtherPlayersTurnState implements State {

    @Override
    public List<Action> chooseFigure(Controller context, int playerNr, int figureNr) {
        // TODO nice-to-have functionality: highlight result from enemy's potential move, if he has rolled his dice yet OR ignore action
        List<Action> l = new ArrayList<>();
        l.add(new NotificationAction(TOAST, "", "Du bist nicht am Zug"));
        return l; // ignore action
    }

    @Override
    public boolean rollDice(Controller context) {
        return false; // ignore action
    }

    @Override
    public List<Action> diceRollResult(Controller context, int result, boolean hasCheated) {
        return new ArrayList<>(); // ignore action
    }

    @Override
    public List<Action> catchCheater(boolean playerBeforeHasCheated, Controller context) {
        List<Action> l = new ArrayList<>();
        l.add(new NotificationAction(TOAST, "", "Du bist nicht am Zug"));
        return l; // ignore action
    }
}
