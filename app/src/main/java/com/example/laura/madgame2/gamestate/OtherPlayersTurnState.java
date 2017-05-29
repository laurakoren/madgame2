package com.example.laura.madgame2.gamestate;

class OtherPlayersTurnState extends AbstractState {

    @Override
    void chooseFigure(Controller context, int playerNr, int figureNr) {
        // TODO nice-to-have functionality: highlight result from enemy's potential move, if he has rolled his dice yet OR ignore action
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
