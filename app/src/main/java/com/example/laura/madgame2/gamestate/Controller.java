package com.example.laura.madgame2.gamestate;

import android.os.AsyncTask;
import android.util.Log;

import com.example.laura.madgame2.PlayField;
import com.example.laura.madgame2.diceroll.RollDiceActivity;
import com.example.laura.madgame2.gamelogic.GameLogic;
import com.example.laura.madgame2.gamelogic.Player;
import com.example.laura.madgame2.gamestate.action.Action;
import com.example.laura.madgame2.multiplayer.Client;
import com.example.laura.madgame2.multiplayer.Server;
import com.example.laura.madgame2.multiplayer.update.Update;
import com.example.laura.madgame2.multiplayer.update.UpdateDraw;
import com.example.laura.madgame2.multiplayer.update.UpdateMyNumber;
import com.example.laura.madgame2.multiplayer.update.UpdatePlayersTurn;

import java.util.List;

/**
 * Controller for PlayField
 * aka Context from StatePattern
 */
public class Controller {
    private static Controller instance;

    private State state;

    private boolean isMultiplayerGame;

    private List<Player> players;
    private GameLogic logic;
    private int myPlayerNr;
    private int currentPlayerNr;
    private Player playerBefore;
    private boolean cheated;
    private boolean playerBeforeCheated;
    private PlayField playField;

    private Controller() {
        state = null;
        players = null;
        logic = null;
        myPlayerNr = -1;
        isMultiplayerGame = false;
        cheated = false;
        playerBefore = null;
        playerBeforeCheated = false;
        playField = null;
    }

    // singleton

    public static Controller getInstance() {
        if (instance == null)
            instance = new Controller();
        return instance;
    }

    // pass function calls to states

    public List<Action> chooseFigure(int playerNr, int figureNr) {
        return state.chooseFigure(this, playerNr, figureNr);

    }

    public boolean rollDice() {
        return state.rollDice(this);
    }

    public List<Action> diceRollResult(int result, boolean hasCheated) {
        cheated = hasCheated;
        return state.diceRollResult(this, result, hasCheated);
    }

    // accessor methods for states

    public boolean isMP() {
        return isMultiplayerGame;
    }

    List<Player> players() {
        return players;
    }

    public GameLogic logic() {
        return logic;
    }

    int myPlayerNr() {
        return myPlayerNr;
    }

    int currPlayerNr() {
        return currentPlayerNr;
    }

    void setState(State state) {
        this.state = state;
    }

    void endTurn(boolean playerHasCheatedThisTurn) {
        currentPlayerNr = (currentPlayerNr + 1) % 4;
        RollDiceActivity.setCheat(false);

        if (this.isMultiplayerGame) {
            state = new OtherPlayersTurnState();
            sendUpdate(new UpdatePlayersTurn(currentPlayerNr, cheated));
            playerBeforeCheated = false;
            cheated = false;
        } else {
            playerBefore = new Player(currentPlayerNr);
            playerBefore.setCheater(cheated);
            // in local multiplayer mode, just increment
            state = new MyTurnPreDiceRollState(playerHasCheatedThisTurn, false);
        }
    }

    // network accessor methods for states

    public void sendUpdate(Update update) {
        new Updater().execute(update);
    }

    public void receiveUpdate(Update update) {
        if (update instanceof UpdateMyNumber) {
            myPlayerNr = ((UpdateMyNumber) update).getMyNumber();
        }

        if (update instanceof UpdatePlayersTurn) {
            currentPlayerNr = myPlayerNr;
            UpdatePlayersTurn u = (UpdatePlayersTurn) update;
            playerBeforeCheated = u.isPlayerBeforeCheated();
            setState(new MyTurnPreDiceRollState(playerBeforeCheated, false));
        }

        if (update instanceof UpdateDraw) {
            UpdateDraw u = (UpdateDraw) update;
            if (u.getPlayerNr() != myPlayerNr) {
                List<Action> actionUpdates = logic.draw(u.getPlayerNr(), u.getFigureNr(), u.getDiceResult());
                logic.handleUpdates(actionUpdates);
                playField.updateField(actionUpdates);

            }
            Log.d("CONTROLLER", myPlayerNr + ", " + u.getDiceResult() + "; " + u.getPlayerNr());
        }
    }

    // setter and init

    public void setMP(boolean isMP) {
        this.isMultiplayerGame = isMP;
    }

    public void setPlayers(List<Player> players, int startingPlayerNr, int myPlayerNr) {
        if (startingPlayerNr < 0 || startingPlayerNr >= players.size())
            throw new IllegalArgumentException("startingPlayer index out of bounds for players list");

        this.players = players;
        this.myPlayerNr = myPlayerNr;
        this.currentPlayerNr = startingPlayerNr;
        this.playerBefore = new Player((players.size() - 1) - startingPlayerNr);

        if (myPlayerNr == startingPlayerNr)
            state = new MyTurnPreDiceRollState(false, false);
        else
            state = new OtherPlayersTurnState();
    }

    public void startGame(List<Player> players) {
        setPlayers(players, 0, myPlayerNr);
    }

    public boolean init() {
        if (state == null)
            return false;
        logic = new GameLogic(players, 40);

        return true;
    }

    public Player getPlayerBefore() {
        return playerBefore;
    }

    public void setMyPlayerNr(int myPlayerNr) {
        this.myPlayerNr = myPlayerNr;
    }

    public void catchCheater() {

        if (isMultiplayerGame) {
            state.catchCheater(playerBeforeCheated);
        } else {
            state.catchCheater(playerBefore.getCheater());
        }
    }

    public void setPlayField(PlayField playField) {
        this.playField = playField;
    }

    private class Updater extends AsyncTask<Update, Void, Void> {

        @Override
        protected Void doInBackground(Update... params) {
            if (isMultiplayerGame) {
                if (Server.isServerRunning()) {
                    Server.getInstance().sendBroadcastUpdate(params[0]);
                } else {
                    Client.getInstance().sendUpdate(params[0]);
                }

            }
            return null;
        }
    }
}