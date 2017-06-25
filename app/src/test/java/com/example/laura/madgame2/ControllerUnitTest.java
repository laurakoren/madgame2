package com.example.laura.madgame2;

import com.example.laura.madgame2.gamestate.action.Action;
import com.example.laura.madgame2.gamestate.action.HighlightAction;
import com.example.laura.madgame2.gamestate.action.MoveFigureAction;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ControllerUnitTest extends ControllerAbstractUnitTest {
    @Test
    public void cantStartWithoutSixRoll() {
        // try rolling a 2
        controller.rollDice();
        controller.diceRollResult(2, false);
        assertEmpty(controller.chooseFigure(0, 0));

        // and a 4
        controller.rollDice();
        controller.diceRollResult(4, false);
        assertEmpty(controller.chooseFigure(0, 0));

        // and finally a 6
        controller.rollDice();
        controller.diceRollResult(6, false);
        assertNotEmpty(controller.chooseFigure(0, 0));
        assertNotEmpty(controller.chooseFigure(0, 0));
    }

    @Test
    public void mayRollDiceOnlyInRightState() {
        // player may roll dice while in PreDiceRoll state
        Assert.assertTrue(controller.rollDice());
        controller.diceRollResult(6, false);

        // but not in SelectFigureState
        Assert.assertFalse(controller.rollDice());
    }

    @Test
    public void selectingFigureHighlightsResultField() {
        // roll a 6
        controller.rollDice();
        controller.diceRollResult(6, false);

        // select a figure
        List<Action> actions = controller.chooseFigure(0, 0);

        // check list for size and content
        Assert.assertEquals(actions.size(), 1);
        Assert.assertTrue(actions.get(0) instanceof HighlightAction);
    }

    @Test
    public void selectingAndConfirmingFigureMovesIt() {
        // roll a 6
        controller.rollDice();
        controller.diceRollResult(6, false);

        // select a figure, then confirm
        controller.chooseFigure(0, 0);
        List<Action> actions = controller.chooseFigure(0, 0);

        // check list for size and content
        assertSize(actions, 1);
        Assert.assertTrue(actions.get(0) instanceof MoveFigureAction);
    }
/*
    @Test
    public void endTurnTest() {
        // wrong:
        // cant move figure of second player
        assertEmpty(draw(1, 0, 6));

        // right:
        // move figure of first player
        assertNotEmpty(draw(0, 0, 6));
        assertNotEmpty(draw(0, 0, 2));

        // move figure of second player
        assertNotEmpty(draw(1, 0, 6));
        assertNotEmpty(draw(1, 0, 2));
    }
    */
}
