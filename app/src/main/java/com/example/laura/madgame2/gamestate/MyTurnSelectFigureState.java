package com.example.laura.madgame2.gamestate;

/**
 * State that is active during this Player's turn. In this state the Player may select one of his figures to move.
 */
class MyTurnSelectFigureState extends AbstractState {

    private int diceRollResult;
    private boolean playerHasCheatedThisTurn;
    private int selectedFigure;

    MyTurnSelectFigureState(int diceRollResult, boolean playerHasCheatedThisTurn, int selectedFigure) {
        this.diceRollResult = diceRollResult;
        this.playerHasCheatedThisTurn = playerHasCheatedThisTurn;
        this.selectedFigure = selectedFigure;
    }

    @Override
    void chooseFigure(Controller context, int playerNr, int figureNr) {

        // TODO Check if the player cant make any move (e.g. because a few of his figures are in the finish, but the rest cant follow up) and inform him

        if (playerNr == context.currPlayerNr()) {
            if (figureNr == selectedFigure) {
                // player has confirmed his selection

                if (context.logic().draw(playerNr,figureNr,diceRollResult)) {
                    // move executed, continue with next state

                    if (diceRollResult == 6)
                        // player has rolled 6, he may roll another time
                        // TODO display message
                        context.setState(new MyTurnPreDiceRollState(playerHasCheatedThisTurn));
                    else
                        // the players move is over
                        context.endTurn();
                } else {
                    // cannot do that move
                    // TODO display message
                }
            } else {
                // player has changed the figure
                selectedFigure = figureNr;

                // TODO highlight result field
            }
        } else {
            // player has selected another Player's figure
            // TODO display message
        }
    }

    @Override
    boolean rollDice(Controller context) {
        return false; // ignore action
    }

    @Override
    void diceRollResult(Controller context, int result, boolean hasCheated) {
        // ignore action
    }
}
