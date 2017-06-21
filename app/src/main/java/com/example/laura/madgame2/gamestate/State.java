package com.example.laura.madgame2.gamestate;

import com.example.laura.madgame2.gamestate.action.Action;

import java.util.List;

/**
 * Interface for states from State-Pattern
 */
interface State {
    /**
     * The player chooses a Figure
     *
     * @param playerNr the number of the Player whose Figure was chosen
     * @param figureNr the Figure's number
     */
    List<Action> chooseFigure(Controller context, int playerNr, int figureNr);

    /**
     * The player wants to roll the dice
     */
    boolean rollDice(Controller context);

    /**
     * The die has been cast. The results must be honored.
     *
     * @param result the numbers, that have been rolled
     */
    List<Action> diceRollResult(Controller context, int result, boolean hasCheated);

    List<Action> catchCheater(boolean playerBeforeHasCheated, Controller context);


}
