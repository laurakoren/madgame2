package com.example.laura.madgame2.gameLogic;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * The internal representation for the game board.
 */
@SuppressWarnings("WeakerAccess")
public class GameLogic {
    private LinkedFieldCircle boardPath;

    private List<Player> players;

    private List<List<Figure>> figures;

    public GameLogic(List<Player> players, List<View> fieldViews, List<List<View>> figureViews) {
        this.players = players;

        figures = new ArrayList<>();
        Player tmpPlayer;
        List<Figure> tmp;

        for (int i = 0; i < 4; i++) {
            tmpPlayer = players.get(i);

            if (tmpPlayer != null) {
                tmp = new ArrayList<>();

                for (int j = 0; j < 4; j++) {
                    tmp.add(new Figure(tmpPlayer, figureViews.get(i).get(j)));
                }

                tmpPlayer.setFigures(tmp);
                figures.add(tmp);
            }
        }

        this.boardPath = new LinkedFieldCircle(fieldViews, players);
    }

    /**
     * Moves a figure for a given distance over the board. Returns true if the move is valid and was executed and returns false else. If false is returned, the board remains unchanged.
     *
     * @param figure   the piece to move
     * @param distance the distance to move it
     * @return true on success, false on failure
     */
    public boolean draw(Figure figure, int distance) {
        if (figure == null || distance < 0 || distance > 6)
            return false;

        Field current = figure.getField();

        Player player = figure.getPlayer();

        // determine where the figure is
        if (current == null) {
            // figure is in the out area
            if (distance == 6) {
                figure.moveTo(player.getStartField());
                return true;
            } else {
                return false;
            }
        } else {
            // figure is on the game board
            for (int i = 0; i < distance; i++) {
                current = current.next(player);
            }

            if (current.hasFigure()) {
                Figure otherFigure = current.getFigure();

                if (otherFigure.getPlayer() == player) {
                    return false;
                } else {
                    otherFigure.returnToOut();
                }
            }

            figure.moveTo(current);
            return true;
        }
    }
}
