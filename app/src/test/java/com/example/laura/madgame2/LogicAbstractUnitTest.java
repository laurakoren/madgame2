package com.example.laura.madgame2;

import android.view.View;
import android.widget.ImageView;

import com.example.laura.madgame2.gameLogic.GameLogic;
import com.example.laura.madgame2.gameLogic.MovesFigures;
import com.example.laura.madgame2.gameLogic.Player;

import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

abstract class LogicAbstractUnitTest {

    static final int NUM_FIELDS = 40;
    static final int NUM_PLAYERS = 4;
    static final int NUM_FIGURES = 4;

    GameLogic logic;
    List<Player> players;

    MovesFigures movingEntity;

    @Before
    public void init() {
        players = new ArrayList<>();
        for (int i = 0; i < NUM_PLAYERS; i++)
            players.add(new Player(i));

        movingEntity = new MovesFigures() {
            public void moveFigure(int playerNr, int figureNr, int fieldNr) {
            }
            public void moveFigureToOutField(int playerNr, int figureNr) {
            }
            public void moveFigureToFinishField(int playerNr, int figureNr, int finishFieldNr) {

            }
        };

        logic = new GameLogic(players, NUM_FIELDS, movingEntity);
    }

    @After
    public void destroy() {
        players = null;
        logic = null;
    }
}
