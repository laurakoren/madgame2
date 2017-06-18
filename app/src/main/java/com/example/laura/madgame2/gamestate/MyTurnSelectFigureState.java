package com.example.laura.madgame2.gamestate;

import android.os.AsyncTask;
import android.util.Log;

import com.example.laura.madgame2.gamelogic.Field;
import com.example.laura.madgame2.gamelogic.Player;
import com.example.laura.madgame2.gamestate.action.Action;
import com.example.laura.madgame2.gamestate.action.HighlightAction;
import com.example.laura.madgame2.gamestate.action.NotificationAction;
import com.example.laura.madgame2.gamestate.action.WinningAction;
import com.example.laura.madgame2.multiplayer.update.UpdateDraw;

import java.util.ArrayList;
import java.util.List;

import static com.example.laura.madgame2.gamestate.action.NotificationAction.Type.TEXTFIELD;

/**
 * State that is active during this Player's turn. In this state the Player may select one of his figures to move.
 */
class MyTurnSelectFigureState implements State {

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
    public List<Action> chooseFigure(Controller context, int playerNr, int figureNr) {
        List<Action> result = new ArrayList<>();

        if (playerNr == context.currPlayerNr()) {
            if (figureNr == selectedFigure) {
                // player has confirmed his selection
                result = context.logic().draw(playerNr, figureNr, diceRollResult);
                if (context.isMP()) {
                    new DrawUpdate(context).execute(playerNr, figureNr, diceRollResult);
                }

                if (!result.isEmpty()) {
                    // move executed, continue with next state

                    if (diceRollResult == 6) {
                        // player has rolled 6, he may roll another time
                        result.add(new NotificationAction(TEXTFIELD, "", "Erneut würfeln"));

                        context.setState(new MyTurnPreDiceRollState(previousPlayerHasCheated, playerHasCheatedThisTurn));
                    } else {
                        // the players move is over
                        context.endTurn(playerHasCheatedThisTurn);
                    }
                } else {
                    // cannot do that move
                    result.add(new NotificationAction(TEXTFIELD, "", "Dieser Zug ist ungültig"));
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
            result.add(new NotificationAction(TEXTFIELD, "", "Nicht Ihre Figur"));
        }

        Player winner = context.logic().getWinner();
        if (winner != null) {
            result.add(new WinningAction(winner, "Spieler " + winner.getPlayerNr()));
            // TODO update raushauen
        }

        return result;
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
    public void catchCheater(boolean playerBeforeHasCheated) {
        //TODO punishment for cheating
        if (playerBeforeHasCheated) {
            Log.d("Cheater", "player before has cheated");
        } else {
            Log.d("Cheater", "player before has not cheated");
        }
    }

    private class DrawUpdate extends AsyncTask<Integer, Void, Void> {

        private Controller controller;

        protected DrawUpdate() {
        }

        public DrawUpdate(Controller controller) {
            this.controller = controller;
        }

        @Override
        protected Void doInBackground(Integer... params) {
            controller.sendUpdate(new UpdateDraw(params[0], params[1], params[2]));
            return null;
        }
    }

}
