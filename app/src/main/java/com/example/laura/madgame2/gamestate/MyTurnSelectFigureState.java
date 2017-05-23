package com.example.laura.madgame2.gamestate;

/**
 * State that is active during this Player's turn. In this state the Player may roll his dice.
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


        // TODO if all figures are in "out fields" then the player may roll up to three times or until he rolls a 6

        if (playerNr == context.myPlayerNr()) {
            if (figureNr == selectedFigure) {
                // player has confirmed his selection

                // TODO move figure

                if (diceRollResult == 6)
                    // player has rolled 6, he may roll another time
                    context.setState(new MyTurnPreDiceRollState(playerHasCheatedThisTurn));
                else
                    context.setState(new OtherPlayersTurnState());
            } else {
                // player has changed the figure
                selectedFigure = figureNr;

                // TODO highlight result field
            }
        } else {
            // player has chosen another Player's figure
        }


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
