package com.example.laura.madgame2.gamestate;

import com.example.laura.madgame2.gamelogic.Field;
import com.example.laura.madgame2.gamelogic.Player;
import com.example.laura.madgame2.gamestate.action.Action;
import com.example.laura.madgame2.gamestate.action.HighlightAction;
import com.example.laura.madgame2.gamestate.action.WinningAction;
import com.example.laura.madgame2.multiplayer.update.UpdateDraw;

import java.util.ArrayList;
import java.util.List;

/**
 * State that is active during this Player's turn. In this state the Player may select one of his figures to move.
 */
class MyTurnSelectFigureState extends AbstractState {

    private int diceRollResult;
    private boolean previousPlayerHasCheated;
    private boolean playerHasCheatedThisTurn;
    private int selectedFigure;

    MyTurnSelectFigureState(int diceRollResult, boolean previousPlayerHasCheated, boolean playerHasCheatedThisTurn, int selectedFigure) {
        this.diceRollResult = diceRollResult;
        this.previousPlayerHasCheated = previousPlayerHasCheated;
        this.playerHasCheatedThisTurn = playerHasCheatedThisTurn;
        this.selectedFigure = selectedFigure;
    }

    @Override
    List<Action> chooseFigure(Controller context, int playerNr, int figureNr) {
        List<Action> result = new ArrayList<>();

        if (playerNr == context.currPlayerNr()) {
            if (figureNr == selectedFigure) {
                // player has confirmed his selection

                result = context.logic().draw(playerNr, figureNr, diceRollResult);

                if (result != null) {
                    // move executed, continue with next state

                    if (diceRollResult == 6) {
                        // player has rolled 6, he may roll another time
                        context.putText("Erneut würfeln ");

                        context.setState(new MyTurnPreDiceRollState(previousPlayerHasCheated, playerHasCheatedThisTurn));
                    } else {
                        // the players move is over
                        context.endTurn(playerHasCheatedThisTurn);
                    }

                    context.sendUpdate(new UpdateDraw(playerNr, figureNr, diceRollResult, playerHasCheatedThisTurn));
                } else {
                    // cannot do that move
                    context.putText("Sie können diesen Zug nicht ziehen ");
                }
            } else {
                // player has changed the figure
                selectedFigure = figureNr;

                Field field = context.logic().getResultField(playerNr, figureNr, diceRollResult);
                if (field != null)
                    result.add(new HighlightAction(playerNr, field.getFieldNr(), field.isFinishField()));
            }
        } else {
            // player has selected another Player's figure
            context.putText("Nicht Ihre Figur ");
        }

        Player winner = context.logic().getWinner();
        if (winner != null) {
            result.add(new WinningAction(winner, "Spieler "+winner.getPlayerNr()));
            // TODO update raushauen
        }

        return result;
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
