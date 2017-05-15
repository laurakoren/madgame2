package com.example.laura.madgame2.gameLogic;

import java.util.ArrayList;
import java.util.List;

/**
 * The internal representation for the game board.
 */
public class GameLogic {

    private MovesFigures movingEntity;

    private List<List<Figure>> figures;

    public GameLogic(List<Player> players, int numFields, MovesFigures movingEntity) {
        this.movingEntity = movingEntity;

        figures = new ArrayList<>();
        Player tmpPlayer;
        List<Figure> tmpFigures;

        for (int i = 0; i < 4; i++) {
            tmpPlayer = players.get(i);
            tmpFigures = new ArrayList<>();

            for (int j = 0; j < 4; j++)
                tmpFigures.add(new Figure(tmpPlayer, j));

            tmpPlayer.setFigures(tmpFigures);
            figures.add(tmpFigures);
        }

        createBoardPath(numFields, players);
    }

    private void createBoardPath(int numFields, List<Player> players) {
        Field dummy = new Field(-1);
        Field current = dummy;

        for (int i = 0; i < numFields; i++) {
            current.setNext(new Field(i));
            current = current.next(null);

            // declare start fields
            if (i % 10 == 0 && players.get(i/10) != null)
                players.get(i / 10).setStartField(current);
        }

        // close circle
        Field first = dummy.next(null);
        current.setNext(first);
    }

    /**
     * Moves a figure for a given distance over the board. Returns true if the move is valid and was executed and returns false else. If false is returned, the board remains unchanged.
     *
     * @param figure   the figure to move
     * @param distance the distance to move it
     * @return true on success, false otherwise
     */
    public boolean draw(Figure figure, int distance) {
        if (figure == null || distance < 0 || distance > 6)
            return false;

        Field current = figure.getField();
        Player player = figure.getPlayer();

        // determine where the figure is
        if (current == null) {
            // figure is in the out area
            // if player rolled 6, then make the move, else return false
            return distance == 6 && moveFigureToStartField(figure);
        } else {
            // figure is somewhere on the game board
            for (int i = 0; i < distance; i++)
                current = current.next(player);

            return moveFigure(figure,current);
        }
    }

    public int highlight(Figure figure, int distance) {

        Field current = figure.getField();
        Player player = figure.getPlayer();

            // figure is somewhere on the game board
            for (int i = 0; i < distance; i++)
                current = current.next(player);
            return current.getFieldNr();


        }


    /**
     * Moves a figure to a field after checking the field for availability.
     * @param figure the figure to be moved
     * @param field the field to move it to
     * @return true on success, false otherwise
     */
    private boolean moveFigure(Figure figure, Field field) {
        if (field.hasFigure()) {
            // another figure is blocking the path
            Figure otherFigure = field.getFigure();

            if (otherFigure.getPlayer() == figure.getPlayer())
                // cant kick own figures, move is impossible
                return false;
            else
                // kick other figure
                moveFigureToOutField(otherFigure);
        }

        figure.setField(field);
        movingEntity.moveFigure(figure.getPlayer().getPlayerNr(), figure.getFigureNr(), field.getFieldNr());

        return true;
    }

    /**
     * Moves a figure from the game board into the out area aka kicks the figure out of the game.
     * @param figure the figure to be kicked
     */
    private void moveFigureToOutField(Figure figure) {
        figure.setField(null);
        movingEntity.moveFigureToOutField(figure.getPlayer().getPlayerNr(), figure.getFigureNr());
    }

    /**
     * Moves a figure from the out area onto the board by placing it onto its player's start field.
     * @param figure the figure to be moved
     * @return true on success, false otherwise
     */
    private boolean moveFigureToStartField(Figure figure) {
        return moveFigure(figure, figure.getPlayer().getStartField());
    }
}
