package com.example.laura.madgame2;

import com.example.laura.madgame2.gamestate.Controller;
import com.example.laura.madgame2.gamestate.action.Action;

import junit.framework.Assert;

import org.junit.Before;

import java.util.Collection;
import java.util.List;

/**
 * Abstract for Testing involving the Controller. Makes use of the setUp and tearDown methods of {@link LogicAbstractUnitTest} and provides a few helper methods.
 */
abstract class ControllerAbstractUnitTest extends LogicAbstractUnitTest {
    Controller controller;

    @Before
    public void setUp() {
        super.setUp();

        controller = Controller.getInstance();

        controller.setPlayers(players, 0, 0);
        controller.init();
    }

    /**
     * Moves a figure and "skips" the other players
     */
    List<Action> drawAndSkip(int playerNr, int figureNr, int distance) {
        List<Action> result = draw(playerNr, figureNr, distance);
        skipTurns(playerNr);
        return result;
    }

    /**
     * Macro for rolling dice, selecting and confirming a figure.
     */
    List<Action> draw(int playerNr, int figureNr, int distance) {
        controller.rollDice();
        controller.diceRollResult(distance, false);
        controller.chooseFigure(playerNr, figureNr);
        return controller.chooseFigure(playerNr, figureNr);
    }

    /**
     * "Skips" other players starting from a given player resulting in another turn for the player. "Skipping" means everyone else rolls 1 and moves his first figure.
     */
    private void skipTurns(int playerNr) {
        for (int i = 1; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                draw((playerNr + i) % 4, 0, 1);
            }
        }
    }

    void assertEmpty(Collection<?> c) {
        Assert.assertTrue(c.isEmpty());
    }

    void assertNotEmpty(Collection<?> c) {
        Assert.assertFalse(c.isEmpty());
    }
}
