package com.example.laura.madgame2;

import com.example.laura.madgame2.gamelogic.Field;
import com.example.laura.madgame2.gamelogic.Figure;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class LogicUnitTest extends LogicAbstractUnitTest {

    @Test
    public void allFiguresInOutFieldsAtStart() {
        for (int i = 0; i < NUM_PLAYERS; i++) {
            for (int j = 0; j < NUM_FIGURES; j++) {
                Assert.assertNull(players.get(i).getFigure(i).getField());
            }
        }
    }

    @Test
    public void rollingSixMovesFigureFromOutToStartField() {
        for (int playerNr = 0; playerNr < NUM_PLAYERS; playerNr++) {
            logic.draw(playerNr, 0, 6);

            Assert.assertEquals(players.get(playerNr).getFigure(0).getField(), players.get(playerNr).getStartField());
        }
    }

    @Test
    public void rollingNotSixDoesntMoveFigureOutOfStart() {
        Random random = new Random(System.currentTimeMillis());

        for (int i = 1; i < 100; i++) {
            logic.draw(0, 0, random.nextInt(5) + 1);

            Assert.assertNull(players.get(0).getFigure(0).getField());
        }
    }

    @Test
    public void drawingFigureToOccupiedFieldKicksOtherFigure() {
        Figure a = players.get(0).getFigure(0);
        Figure b = players.get(1).getFigure(0);

        logic.draw(1, 0, 6);

        Field f = b.getField();

        logic.draw(0, 0, 6);
        logic.draw(0, 0, 6);
        logic.draw(0, 0, 4);

        Assert.assertNull(b.getField());
        Assert.assertEquals(a.getField(), f);
    }

    @Test
    public void drawingFigureOnStartFieldFromOutKicksOtherFigures() {
        Figure a = players.get(0).getFigure(0);
        Figure b = players.get(1).getFigure(0);

        logic.draw(0, 0, 6);
        logic.draw(0, 0, 6);
        logic.draw(0, 0, 4);

        Field f = a.getField();

        logic.draw(1, 0, 6);

        Assert.assertNull(a.getField());
        Assert.assertEquals(b.getField(), f);
    }

    // TODO test case: cant kick own figures
    // TODO test case: finish fields (+isFinishField)
    // TODO test case: game winning criteria
}
