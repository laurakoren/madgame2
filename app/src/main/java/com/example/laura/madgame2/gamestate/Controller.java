package com.example.laura.madgame2.gamestate;

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

    private Controller() {
        state = null;
        players = null;
        logic = null;
        myPlayerNr = -1;
        isMultiplayerGame = false;
        cheated = false;
        playerBefore = null;
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
        playerBefore = new Player(currentPlayerNr);
        playerBefore.setCheater(cheated);
        currentPlayerNr = (currentPlayerNr + 1) % 4;

        if (this.isMultiplayerGame) {
            // assume this only gets called by "MyTurn" states, as receiveUpdate will be responsible else
            state = new OtherPlayersTurnState();

            // TODO send playerHasCheatedThisTurn to other instances
            sendUpdate(new UpdatePlayersTurn(currentPlayerNr));
        } else
            // in local multiplayer mode, just increment
            state = new MyTurnPreDiceRollState(playerHasCheatedThisTurn, false);
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
            // TODO receive playerHasCheatedThisTurn
            currentPlayerNr = myPlayerNr;
            setState(new MyTurnPreDiceRollState(false, false));
        }

        //TODO vielleich bessere Umsetzung um auf die Figur im View zu bewegen..
        if (update instanceof UpdateDraw) {
            /*if (update.getPlayerNr() != myPlayerNr) {
                MovesFigures p = logic.getMovingEntity();
                ((PlayField) p).updateField((UpdateDraw) update);
            }*/
            UpdateDraw u = (UpdateDraw) update;
            logic.draw(u.getPlayerNr(), u.getFigureNr(), u.getDiceResult());
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

    public boolean startGame(List<Player> players) {
        // TODO replace by setPlayers
        this.players = players;
        if (!isMP()) {
            return false;
        }
        if (Server.isServerRunning()) {
            myPlayerNr = 0;
            state = new MyTurnPreDiceRollState(false, false);
        } else {
            state = new OtherPlayersTurnState();
        }
        return true;
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
}
