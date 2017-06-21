package com.example.laura.madgame2.gamestate;

import com.example.laura.madgame2.gamestate.action.Action;
import com.example.laura.madgame2.gamestate.action.NotificationAction;
import com.example.laura.madgame2.gamestate.action.UpdateDiceRoll;

import java.util.ArrayList;
import java.util.List;

import static com.example.laura.madgame2.gamestate.action.NotificationAction.Type.ALERT;
import static com.example.laura.madgame2.gamestate.action.NotificationAction.Type.TEXTFIELD;
import static com.example.laura.madgame2.gamestate.action.NotificationAction.Type.TOAST;

/**
 * State that is active during this Player's turn. In this state the Player may roll his dice.
 */
class MyTurnPreDiceRollState implements State {

    private boolean previousPlayerHasCheated;
    private boolean playerHasCheatedThisTurn;
    private int unluckyThrowsCount;


    MyTurnPreDiceRollState(boolean previousPlayerHasCheated, boolean playerHasCheatedThisTurn) {
        this.previousPlayerHasCheated = previousPlayerHasCheated;
        this.playerHasCheatedThisTurn = playerHasCheatedThisTurn;
        this.unluckyThrowsCount = 0;
    }

    @Override
    public List<Action> chooseFigure(Controller context, int playerNr, int figureNr) {
        List<Action> l = new ArrayList<>();
        l.add(new NotificationAction(TOAST, "", "Zuerst würfeln!"));

        return l; // ignore action
    }

    @Override
    public boolean rollDice(Controller context) {
        return true;
    }

    @Override
    public List<Action> diceRollResult(Controller context, int result, boolean hasCheated) {
        ArrayList<Action> list = new ArrayList<>();

        if (result != 6 && context.logic().hasNoFiguresOnField(context.currPlayerNr())) {
            // player has no figures on field

            if (++unluckyThrowsCount < 3) {
                // he may roll again (up to 3 times)
                list.add(new NotificationAction(TEXTFIELD, "", "Sie können erneut würfeln (" + unluckyThrowsCount + "/3)"));

            } else {
                // player has used up his 3 rolls
                list.add(new NotificationAction(TEXTFIELD, "", "Chance vertan"));
                context.endTurn(playerHasCheatedThisTurn);
            }
        } else if (context.logic().hasValidMoves(context.currPlayerNr(), result)) {
            // normal procedure: continue to next state
            context.setState(new MyTurnSelectFigureState(result, previousPlayerHasCheated, playerHasCheatedThisTurn, -1));
        } else {
            // player can't move any of his figures with the number he rolled
            list.add(new NotificationAction(ALERT, "", "Keine Züge möglich, nächster ist dran"));
            context.endTurn(hasCheated || playerHasCheatedThisTurn);
        }

        Action dice = new UpdateDiceRoll(result);
        list.add(dice);
        return list;
    }

    @Override
    public List<Action> catchCheater(boolean playerBeforeHasCheated, Controller context) {
        List<Action> result = new ArrayList<>();

        if (playerBeforeHasCheated) {
            int cheaterNr = (((context.getMyPlayerNr() - 1) % 4) + 4) % 4;
            context.addCheater(cheaterNr);
            result.add(new NotificationAction(TOAST, "", "erwischt, " + context.getPlayerName() + " wird bestraft"));
        } else {
            context.endTurn(false);
            result.add(new NotificationAction(TOAST, "", "fälschlich beschuldigt, du wirst bestraft"));
        }

        return result;
    }
}
