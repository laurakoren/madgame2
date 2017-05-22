package com.example.laura.madgame2.gamestate;

import com.example.laura.madgame2.gamelogic.GameLogic;
import com.example.laura.madgame2.gamelogic.Player;

import java.util.List;

/**
 * Controller for PlayField
 * aka Context from StatePattern
 */
public class Controller {

    private AbstractState state;
        private List<Player> players;
        private GameLogic logic;
        private int myPlayerNr;

    public Controller(List<Player> players, GameLogic logic, int startingPlayerNr, int myPlayerNr) {
            if (startingPlayerNr < 0 || startingPlayerNr >= players.size())
                throw new IllegalArgumentException("startingPlayer index out of bounds for players list");

            this.players = players;
            this.logic = logic;
            this.myPlayerNr = myPlayerNr;

            if (myPlayerNr == startingPlayerNr)
                state = new MyTurnPreDiceRollState(false);
            else
                state = null;
    }

    // pass function call to state

    public void chooseFigure(int playerNr, int figureNr) {
        state.chooseFigure(this, playerNr, figureNr);
    }

    public void rollDice() {
        state.rollDice(this);
    }

    public void diceRollResult(int result, boolean hasCheated) {
        state.diceRollResult(this, result, hasCheated);
    }

    // accessor methods for states

    List<Player> players() {
        return players;
    }

    GameLogic logic() {
        return logic;
    }

    int myPlayerNr() {
        return myPlayerNr;
    }

    void setState(AbstractState state) {
        this.state = state;
    }
}
