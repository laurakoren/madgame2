package com.example.laura.madgame2.gamestate;

/**
 * State that is active during this Player's turn. In this state the Player may roll his dice.
 */
class MyTurnPreDiceRollState extends AbstractState {

    private boolean playerHasCheatedThisTurn;
    private int unluckyThrowsCount;

    MyTurnPreDiceRollState(boolean playerHasCheatedThisTurn) {
        this.playerHasCheatedThisTurn = playerHasCheatedThisTurn;
        this.unluckyThrowsCount = 0;
    }

    @Override
    void chooseFigure(Controller context, int playerNr, int figureNr) {
        // ignore action
    }

    @Override
    boolean rollDice(Controller context) {
        return true;
    }

    @Override
    void diceRollResult(Controller context, int result, boolean hasCheated) {

        if (result != 6 && context.logic().hasNoFiguresOnField(context.currPlayerNr())) {
            // player has no figures on field

            if (++unluckyThrowsCount < 3) {
                // he may roll again (up to 3 times)
                // TODO display message
            } else {
                // player has used up his 3 rolls
                // TODO display message
                context.endTurn();
            }
        } else {
            // normal procedure: continue to next state
            context.setState(new MyTurnSelectFigureState(result, playerHasCheatedThisTurn, -1));
        }
    }
}
