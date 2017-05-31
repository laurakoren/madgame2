package com.example.laura.madgame2.gamestate;

import com.example.laura.madgame2.gamestate.action.Action;
import com.example.laura.madgame2.gamestate.action.UpdateDiceRoll;
import com.example.laura.madgame2.gamestate.action.UpdatePlayerFigure;

import java.util.ArrayList;
import java.util.List;

/**
 * State that is active during this Player's turn. In this state the Player may roll his dice.
 */
class MyTurnPreDiceRollState extends AbstractState {

    private boolean playerHasCheatedThisTurn;
    private int unluckyThrowsCount;

    MyTurnPreDiceRollState(boolean playerHasCheatedThisTurn) {
        this.playerHasCheatedThisTurn = playerHasCheatedThisTurn;
        this.unluckyThrowsCount = 0;
    }

    @Override
    List<Action> chooseFigure(Controller context, int playerNr, int figureNr) {
        Action fig = new UpdatePlayerFigure(playerNr,figureNr);
        ArrayList<Action> list = new ArrayList<>();
        list.add(fig);
        return list;

        // ignore action
    }

    @Override
    boolean rollDice(Controller context) {
        return true;
    }

    @Override
    List<Action> diceRollResult(Controller context, int result, boolean hasCheated) {

        if (result != 6 && context.logic().hasNoFiguresOnField(context.currPlayerNr())) {
            // player has no figures on field

            if (++unluckyThrowsCount < 3) {
                // he may roll again (up to 3 times)
                // TODO display message
            } else {
                // player has used up his 3 rolls
                // TODO display message
                context.endTurn();
            }
        } else {
            // normal procedure: continue to next state
            context.setState(new MyTurnSelectFigureState(result, playerHasCheatedThisTurn, -1));
        }

        Action dice = new UpdateDiceRoll(result);
        ArrayList<Action> list = new ArrayList<>();
        list.add(dice);
        return list;
    }
}
