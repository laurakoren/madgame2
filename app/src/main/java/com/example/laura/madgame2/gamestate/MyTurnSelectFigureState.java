package com.example.laura.madgame2.gamestate;

import android.content.Context;

import com.example.laura.madgame2.gamestate.action.Action;
import com.example.laura.madgame2.gamestate.action.HighlightAction;
import com.example.laura.madgame2.gamestate.action.UpdateDiceRoll;
import com.example.laura.madgame2.gamestate.action.UpdatePlayerFigure;

import java.util.ArrayList;
import java.util.List;

/**
 * State that is active during this Player's turn. In this state the Player may select one of his figures to move.
 */
class MyTurnSelectFigureState extends AbstractState {

    private int diceRollResult;
    private boolean playerHasCheatedThisTurn;
    private int selectedFigure;

    MyTurnSelectFigureState(int diceRollResult, boolean playerHasCheatedThisTurn, int selectedFigure) {
        this.diceRollResult = diceRollResult;
        this.playerHasCheatedThisTurn = playerHasCheatedThisTurn;
        this.selectedFigure = selectedFigure;
    }

    @Override
    List<Action> chooseFigure(Controller context, int playerNr, int figureNr) {

        // TODO Check if the player cant make any move (e.g. because a few of his figures are in the finish, but the rest cant follow up) and inform him

        ArrayList<Action> list = new ArrayList<>();

        if (playerNr == context.currPlayerNr()) {
            if (figureNr == selectedFigure) {
                // player has confirmed his selection

                if (context.logic().draw(playerNr,figureNr,diceRollResult)) {
                    // move executed, continue with next state

                    if (diceRollResult == 6)
                        // player has rolled 6, he may roll another time
                        // TODO display message
                        context.setState(new MyTurnPreDiceRollState(playerHasCheatedThisTurn));
                    else
                        // the players move is over
                        context.endTurn();
                } else {
                    // cannot do that move
                    // TODO display message
                }
            } else {
                // player has changed the figure
                selectedFigure = figureNr;

                int fieldToHighlight = context.logic().highlight(selectedFigure,playerNr,diceRollResult);
                list.add(new HighlightAction(fieldToHighlight));


                // TODO highlight result field
            }
        } else {
            // player has selected another Player's figure
            // TODO display message
        }

        Action fig = new UpdatePlayerFigure(playerNr,figureNr);

        list.add(fig);
        return list;
    }

    @Override
    boolean rollDice(Controller context) {
        return false; // ignore action
    }

    @Override
    List<Action> diceRollResult(Controller context, int result, boolean hasCheated) {
        Action dice = new UpdateDiceRoll(result);
        ArrayList<Action> list = new ArrayList<>();
        list.add(dice);
        return list;

        // ignore action
    }
}
