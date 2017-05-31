package com.example.laura.madgame2.gamestate;

import com.example.laura.madgame2.gamelogic.GameLogic;
import com.example.laura.madgame2.gamelogic.MovesFigures;
import com.example.laura.madgame2.gamelogic.Player;
import com.example.laura.madgame2.multiplayer.update.Update;

import java.util.List;

/**
 * Controller for PlayField
 * aka Context from StatePattern
 */
public class Controller {

    private static Controller instance;

    private AbstractState state;

    private boolean isMultiplayerGame;

    private List<Player> players;
    private GameLogic logic;
    private int myPlayerNr;
    private int currentPlayerNr;

    private Controller() {
        state = null;
        players = null;
        logic = null;
        myPlayerNr = -1;
        isMultiplayerGame = false;
    }

    // singleton

    public static Controller getInstance() {
        if (instance == null)
            instance = new Controller();
        return instance;
    }

    // pass function calls to states

    public void chooseFigure(int playerNr, int figureNr) {
        state.chooseFigure(this, playerNr, figureNr);
    }

    public boolean rollDice() {
        return state.rollDice(this);
    }

    public void diceRollResult(int result, boolean hasCheated) {
        state.diceRollResult(this, result, hasCheated);
    }

    // accessor methods for states

    boolean isMP() {
        return isMultiplayerGame;
    }

    List<Player> players() {
        return players;
    }

    GameLogic logic() {
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

    void endTurn() {
        currentPlayerNr = (currentPlayerNr + 1) % 4;

        if (this.isMultiplayerGame)
            // assume this only gets called by "MyTurn" states, as receiveUpdate will be responsible else
            state = new OtherPlayersTurnState();
        else
            // in local multiplayer mode, just increment
            state = new MyTurnPreDiceRollState(false);
    }

    // network accessor methods for states

    public void receiveUpdate(Update update) {
        setState(new OtherPlayersTurnState());


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

        if (myPlayerNr == startingPlayerNr)
            state = new MyTurnPreDiceRollState(false);
        else
            state = new OtherPlayersTurnState();
    }

    public boolean init(MovesFigures movesFigures) {
        if (state == null)
            return false;
        logic = new GameLogic(players, 40, movesFigures);
        return true;
    }
}
