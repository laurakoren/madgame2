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

    @Test
    public void cantKickOwnFigures() {
        Figure a = players.get(0).getFigure(0);
        Figure b = players.get(0).getFigure(1);
        Figure c = players.get(0).getFigure(2);

        logic.draw(0, 0, 6);
        logic.draw(0, 0, 4);
        logic.draw(0, 1, 6);
        assertEmpty(logic.draw(0, 1, 4));
        assertEmpty(logic.draw(0, 2, 6));

        Assert.assertNotNull(a.getField());
        Assert.assertNotNull(b.getField());
        Assert.assertNull(c.getField());
    }

    @Test
    public void canEnterFinishFieldsButNotGoBeyond() {
        for (int i = 0; i < 8; i++)
            assertNotEmpty(logic.draw(0, 0, 6));

        assertEmpty(logic.draw(0, 0, 6));
        assertNotEmpty(logic.draw(0, 0, 1));

        for (int i = 0; i < 6; i++)
            assertEmpty(logic.draw(0, 0, i));
    }

    @Test
    public void gameWinningCriterion() {
        Assert.assertNull(logic.getWinner());

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 7; j++) {
                logic.draw(0, i, 6);
                Assert.assertNull(logic.getWinner());
            }
            Assert.assertNotNull(logic.draw(0, i, 3));
            Assert.assertNotNull(logic.draw(0, i, i + 1));
        }

        Assert.assertNotNull(logic.getWinner());
        Assert.assertEquals(logic.getWinner(), players.get(0));
    }

    @Test
    public void hasNoValidMoves() {
        Assert.assertNull(logic.getWinner());

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 7; j++) {
                logic.draw(0, i, 6);
                Assert.assertTrue(logic.hasValidMoves(0, 3));
            }
            Assert.assertNotNull(logic.draw(0, i, 2));
            Assert.assertNotNull(logic.draw(0, i, i + 1));
        }

        for (int i = 1; i <= 4; i++)
            Assert.assertTrue(logic.hasValidMoves(0, i));

        for (int i = 5; i <= 6; i++)
            Assert.assertFalse(logic.hasValidMoves(0, i));
    }
}
