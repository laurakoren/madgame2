package com.example.laura.madgame2;

import com.example.laura.madgame2.gamelogic.GameLogic;
import com.example.laura.madgame2.gamelogic.Player;

import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;
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
}
