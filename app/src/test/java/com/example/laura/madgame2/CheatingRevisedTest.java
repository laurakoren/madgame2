package com.example.laura.madgame2;

import com.example.laura.madgame2.gamestate.action.Action;
import com.example.laura.madgame2.gamestate.action.MoveFigureAction;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;


/**
 * Created by laura on 19.06.2017.
 */

public class CheatingRevisedTest extends ControllerAbstractUnitTest{

    @Test
    public void cantStartWithoutSixRollReworked() {

        // try rolling a 1-5 without cheat
        for(int i=1;i<6;i++) {
            controller.rollDice();
            controller.diceRollResult(i, false);
            Assert.assertNull(controller.chooseFigure(0, 0));
        }

        // and finally a 6 without cheat
        controller.rollDice();
        controller.diceRollResult(6, false);
        Assert.assertNotNull(controller.chooseFigure(0, 0));
        Assert.assertNotNull(controller.chooseFigure(0, 0));

    }

    @Test
    public void cantUseDoubleCheat() {

        // testfall immer gültig weil direktes ansprechen der methode im bezug auf cheaten nicht abgefangen wird(momentane lösung durch ausgrauen des buttons)


            controller.rollDice();
            controller.diceRollResult(2, true);
            controller.diceRollResult(6, true);

            Assert.assertNotNull(controller.chooseFigure(0, 0));
    }


    @Test
    public void selectingAndConfirmingFigureOfThirdPlayer() {
        // roll a 6
        controller.rollDice();
        controller.diceRollResult(6, false);

        // move 2-4 player before first player
        for(int i=1; i<4; i++) {
            controller.chooseFigure(i, 0);

            List<Action> actions = controller.chooseFigure(i, 0);

            // check list for size must be null because of invalid state
            Assert.assertEquals(actions.size(), 0);
        }
    }

}
