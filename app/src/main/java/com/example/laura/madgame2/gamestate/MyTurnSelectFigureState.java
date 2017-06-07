package com.example.laura.madgame2.gamestate;

import android.widget.TextView;

import android.content.Context;

import com.example.laura.madgame2.gamestate.action.Action;
import com.example.laura.madgame2.gamestate.action.HighlightAction;
import com.example.laura.madgame2.gamestate.action.UpdateDiceRoll;
import com.example.laura.madgame2.gamestate.action.UpdatePlayerFigure;
import com.example.laura.madgame2.multiplayer.Client;
import com.example.laura.madgame2.multiplayer.Server;
import com.example.laura.madgame2.multiplayer.update.UpdateDraw;
import com.example.laura.madgame2.multiplayer.update.UpdatePlayersTurn;

import java.util.ArrayList;
import java.util.List;

/**
 * State that is active during this Player's turn. In this state the Player may select one of his figures to move.
 */
class MyTurnSelectFigureState extends AbstractState {

    private int diceRollResult;
    private boolean playerHasCheatedThisTurn;
    private int selectedFigure;
    public TextView outPutText;

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

                if (context.logic().draw(playerNr, figureNr, diceRollResult)) {
                    // move executed, continue with next state

                    if (diceRollResult == 6) {
                        // player has rolled 6, he may roll another time
                        context.putText("Erneut würfeln ");

                        context.setState(new MyTurnPreDiceRollState(playerHasCheatedThisTurn));
                    } else {
                        // the players move is over
                        context.endTurn();
                    }
                    if (Server.isServerRunning()) {
                        Server.getInstance().sendBroadcastUpdate(new UpdateDraw(playerNr, figureNr, diceRollResult));
                    } else {
                        Client.getInstance().sendUpdate(new UpdateDraw(playerNr, figureNr, diceRollResult));
                    }
                } else {
                    // cannot do that move
                    // outPutText = (TextView) getViewById("PlayerTurn");
                    context.putText("Sie können diesen Zug nicht ziehen ");
                }
            } else {
                // player has changed the figure
                selectedFigure = figureNr;

                int fieldToHighlight = context.logic().highlight(selectedFigure, playerNr, diceRollResult);
                list.add(new HighlightAction(fieldToHighlight));


                // TODO highlight result field
            }
        } else {
            // player has selected another Player's figure
            context.putText("Nicht Ihre Figur ");
        }

        Action fig = new UpdatePlayerFigure(playerNr, figureNr);

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
