package com.example.laura.madgame2.gamestate;

/**
 * State that is active during this Player's turn. In this state the Player may roll his dice.
 */
class MyTurnPreDiceRollState extends AbstractState {

    private boolean playerHasCheatedThisTurn;

    MyTurnPreDiceRollState(boolean playerHasCheatedThisTurn) {
        this.playerHasCheatedThisTurn = playerHasCheatedThisTurn;
    }

    @Override
    void chooseFigure(Controller context, int playerNr, int figureNr) {
        // ignore action
    }

    @Override
    void rollDice(Controller context) {
        // TODO initiate diceRoll and ensure that player doesn't cheat more than once a turn
    }

    @Override
    void diceRollResult(Controller context, int result, boolean hasCheated) {
        // TODO continue to next state

        context.setState(new MyTurnSelectFigureState(result, playerHasCheatedThisTurn));
    }
}
