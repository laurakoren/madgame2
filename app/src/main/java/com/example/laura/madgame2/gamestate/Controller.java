package com.example.laura.madgame2.gamestate;

import android.os.AsyncTask;
import android.util.Log;

import com.example.laura.madgame2.PlayField;
import com.example.laura.madgame2.diceroll.RollDiceActivity;
import com.example.laura.madgame2.gamelogic.GameLogic;
import com.example.laura.madgame2.gamelogic.Player;
import com.example.laura.madgame2.gamestate.action.Action;
import com.example.laura.madgame2.gamestate.action.WinningAction;
import com.example.laura.madgame2.highscore.ScoreEdit;
import com.example.laura.madgame2.multiplayer.Client;
import com.example.laura.madgame2.multiplayer.Server;
import com.example.laura.madgame2.multiplayer.update.Update;
import com.example.laura.madgame2.multiplayer.update.UpdateDraw;
import com.example.laura.madgame2.multiplayer.update.UpdateMyNumber;
import com.example.laura.madgame2.multiplayer.update.UpdatePlayersTurn;
import com.example.laura.madgame2.multiplayer.update.UpdatePlayerWon;

import java.util.ArrayList;
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
    private Integer myPlayerNr;
    private int currentPlayerNr;
    private Player playerBefore;
    private boolean cheated;
    private boolean playerBeforeCheated;
    private PlayField playField;
    private List<Integer> playerWhoCheated;

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
        playerWhoCheated = new ArrayList<>();
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

    public void setMP(boolean isMP) {
        this.isMultiplayerGame = isMP;
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

    public Integer getMyPlayerNr() {
        return myPlayerNr;
    }

    int currPlayerNr() {
        return currentPlayerNr;
    }

    void setState(State state) {
        this.state = state;
    }

    // network accessor methods for states

    void endTurn(boolean playerHasCheatedThisTurn) {
        RollDiceActivity.setCheat(false);
        if (this.isMultiplayerGame) {
            currentPlayerNr = (currentPlayerNr + 1) % 4;
            state = new OtherPlayersTurnState();
            sendUpdate(new UpdatePlayersTurn(currentPlayerNr, cheated, playerWhoCheated));
            playerBeforeCheated = false;
            cheated = false;
        } else {
            skipPlayerThisTurn();
            playerBefore = new Player(currentPlayerNr);
            playerBefore.setCheater(cheated);
            int number = (currentPlayerNr - 1) % 4;
            if (number < 0) {
                number *= -1;
            }
            players.get(number).setCheater(cheated);
            // in local multiplayer mode, just increment
            state = new MyTurnPreDiceRollState(playerHasCheatedThisTurn, false);
        }
    }

    public void sendUpdate(Update update) {
        new Updater().execute(update);
    }

    // setter and init

    public void receiveUpdate(Update update) {
        if (update instanceof UpdateMyNumber) {
            myPlayerNr = ((UpdateMyNumber) update).getMyNumber();
        } else if (update instanceof UpdatePlayersTurn) {
            currentPlayerNr = myPlayerNr;
            UpdatePlayersTurn u = (UpdatePlayersTurn) update;
            playerWhoCheated = u.getPlayerCaughtCheating();
            if (playerWhoCheated.contains(myPlayerNr)) {
                playerWhoCheated.remove(myPlayerNr);
                endTurn(false);
            } else {
                playerBeforeCheated = u.isPlayerBeforeCheated();
                setState(new MyTurnPreDiceRollState(playerBeforeCheated, false));
            }

        } else if (update instanceof UpdateDraw) {

            UpdateDraw u = (UpdateDraw) update;
            if (u.getPlayerNr() != myPlayerNr) {
                List<Action> actionUpdates = logic.draw(u.getPlayerNr(), u.getFigureNr(), u.getDiceResult());
                logic.handleUpdates(actionUpdates);
                playField.updateField(actionUpdates);
            }
        } else if (update instanceof UpdatePlayerWon) {

            UpdatePlayerWon u = (UpdatePlayerWon) update;
            List<Action> list = new ArrayList<>();
            list.add(new WinningAction(players.get(u.winnerNr), u.name));
            playField.handleUpdates(list);
        }
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
            state.catchCheater(playerBeforeCheated, this);

        } else {
            int number = (currentPlayerNr - 1) % 4;

            if (number < 0) {
                number *= -1;
            }

            boolean help = players.get(number).getCheater();
            state.catchCheater(help, this);

            if (help) {
                players.get(number).setPauseNextTurn(true);
                ScoreEdit.updateScore("cheaterCaught");
            } else {
                players.get(currentPlayerNr).setPauseNextTurn(true);
            }
        }
    }

    public void setPlayField(PlayField playField) {
        this.playField = playField;
    }

    String getPlayerName() {
        if (this.isMultiplayerGame) {
            if (Server.isServerRunning()) {
                return Server.getInstance().getPlayerName();
            } else {
                return Client.getPlayerName();
            }
        } else {
            return "Spieler " + currentPlayerNr + 1;
        }
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

    private void skipPlayerThisTurn() {
        int help = (currentPlayerNr + 1) % 4;
        while (players.get(help).getPauseNextTurn() == true) {
            players.get(help).setPauseNextTurn(false);
            help = (help + 1) % 4;
        }

        if (players.get(help).getPauseNextTurn() == false) {
            currentPlayerNr = help;
        }
    }

    public boolean addCheater(Integer playerNr) {
        if (!playerWhoCheated.contains(playerNr)) {
            playerWhoCheated.add(playerNr);
            return true;
        }
        return false;
    }

}