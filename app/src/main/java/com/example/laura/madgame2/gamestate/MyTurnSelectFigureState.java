package com.example.laura.madgame2.gamestate;

/**
 * State that is active during this Player's turn. In this state the Player may roll his dice.
 */
class MyTurnSelectFigureState extends AbstractState {

    private int diceRollResult;
    private boolean playerHasCheatedThisTurn;

    MyTurnSelectFigureState(int diceRollResult, boolean playerHasCheatedThisTurn) {
        this.diceRollResult = diceRollResult;
        this.playerHasCheatedThisTurn = playerHasCheatedThisTurn;
    }

    @Override
    void chooseFigure(Controller context, int playerNr, int figureNr) {
        // TODO move figure OR highlight result field

        // TODO if all figures are in "out fields" then the player may roll up to three times or until he rolls a 6

        if (diceRollResult == 6)
            // player has rolled 6, he may roll another time
            context.setState(new MyTurnPreDiceRollState(playerHasCheatedThisTurn));
        else
            context.setState(new OtherPlayersTurnState());
    }

    @Override
    void rollDice(Controller context) {
        // ignore action
    }

    @Override
    void diceRollResult(Controller context, int result, boolean hasCheated) {
        // ignore action
    }
}
