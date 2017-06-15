package com.example.laura.madgame2.gamestate;

import android.util.Log;
import android.widget.TextView;

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

    private TextView outputtext;
    private static Controller instance;

    private AbstractState state;

    private boolean isMultiplayerGame;

    private List<Player> players;
    private GameLogic logic;
    private int myPlayerNr;
    private int currentPlayerNr;
    private Player playerBefore;
    private boolean cheated;
    private boolean playerBeforeCheated;

    private Controller() {
        state = null;
        players = null;
        logic = null;
        myPlayerNr = -1;
        isMultiplayerGame = false;
        cheated = false;
        playerBefore = null;
        playerBeforeCheated = false;
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

    public void setOutputtext(TextView view) {
        outputtext = view;
    }

    void putText(String text) {
        if (outputtext != null)
            outputtext.setText(text);
    }

    public boolean rollDice() {
        return state.rollDice(this);
    }

    public void diceRollResult(int result, boolean hasCheated) {
        if (hasCheated) {
            cheated = hasCheated;
        }
        state.diceRollResult(this, result, hasCheated);
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

    void setState(AbstractState state) {
        this.state = state;
    }

    void endTurn(boolean playerHasCheatedThisTurn) {
        currentPlayerNr = (currentPlayerNr + 1) % 4;

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
        if (this.isMultiplayerGame) {
            if (Server.isServerRunning())
                Server.getInstance().sendBroadcastUpdate(update);
            else
                Client.getInstance().sendUpdate(update);
        }
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
            if(u.getPlayerNr() != myPlayerNr) {
               logic.handleUpdates(logic.draw(u.getPlayerNr(), u.getFigureNr(), u.getDiceResult()));

            }

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
        state.catchCheater(playerBeforeCheated);
    }
}
