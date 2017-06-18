package com.example.laura.madgame2;

import com.example.laura.madgame2.gamelogic.GameLogic;
import com.example.laura.madgame2.gamelogic.Player;
import com.example.laura.madgame2.gamestate.action.Action;
import com.example.laura.madgame2.gamestate.action.NotificationAction;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Abstract for Testing that involves GameLogic featuring setUp and tearDown methods.
 */
abstract class LogicAbstractUnitTest {

    static final int NUM_FIELDS = 40;
    static final int NUM_PLAYERS = 4;
    static final int NUM_FIGURES = 4;

    GameLogic logic;
    List<Player> players;

    @Before
    public void setUp() {
        players = new ArrayList<>();
        for (int i = 0; i < NUM_PLAYERS; i++)
            players.add(new Player(i));

        logic = new GameLogic(players, NUM_FIELDS);
    }

    @After
    public void tearDown() {
        players = null;
        logic = null;
    }

    void assertEmpty(Collection<Action> c) {
        for (Action action : c)
            Assert.assertTrue(action instanceof NotificationAction);
    }

    void assertNotEmpty(Collection<Action> c) {
        boolean result = false;

        for (Action action : c)
            if (!(action instanceof NotificationAction))
                result = true;

        Assert.assertTrue(result);
    }

    void assertSize(Collection<Action> c, int size) {
        int result = 0;

        for (Action action : c)
            if (!(action instanceof NotificationAction))
                result++;

        Assert.assertEquals(size, result);
    }
}
