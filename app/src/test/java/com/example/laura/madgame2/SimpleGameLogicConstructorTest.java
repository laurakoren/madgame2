package com.example.laura.madgame2;

import com.example.laura.madgame2.gameLogic.GameLogic;
import com.example.laura.madgame2.gameLogic.MovesFigures;
import com.example.laura.madgame2.gameLogic.Player;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The GameLogic constructor is used in the main gamelogic JUnit test class LogicUnitTest in the @Before method and is therefore not tested but only run.
 */
public class SimpleGameLogicConstructorTest {
    @Test
    public void constructorDoesntThrowExceptions() {
        List<Player> players = new ArrayList<>(Arrays.asList(new Player(0), new Player(1), new Player(2), new Player(3)));
        new GameLogic(players, 40, new MovesFigures() {
            public void moveFigure(int playerNr, int figureNr, int fieldNr) {}
            public void moveFigureToOutField(int playerNr, int figureNr) {}
        });
    }
}
