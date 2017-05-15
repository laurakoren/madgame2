package com.example.laura.madgame2.gameLogic;

import java.util.ArrayList;
import java.util.List;

/**
 * The internal representation for the game board.
 */
public class GameLogic {

    private static final int NUM_PLAYERS = 4;
    private static final int NUM_FIGURES = 4;

    private List<Player> players;
    private MovesFigures movingEntity;

    public GameLogic(List<Player> players, int numFields, MovesFigures movingEntity) {
        this.players = players;
        this.movingEntity = movingEntity;

        Player tmpPlayer;
        List<Figure> tmpFigures;

        for (int player = 0; player < NUM_PLAYERS; player++) {
            tmpPlayer = players.get(player);
            tmpFigures = new ArrayList<>();

            for (int fig = 0; fig < NUM_FIGURES; fig++)
                tmpFigures.add(new Figure(tmpPlayer, fig));

            tmpPlayer.setFigures(tmpFigures);
        }

        createBoardPath(numFields, players);
    }

    /**
     * Initializes the linked list of fields also referred to as the "game path".
     * Outsourced to avoid one confusingly long constructor method.
     *
     * @param numFields the number of fields in the game path circle
     * @param players a list of players participating in the game
     */
    private void createBoardPath(int numFields, List<Player> players) {
        Field dummy = new Field(-1);
        Field current = dummy;
        Field tmpField;
        Player tmpPlayer;

        for (int fieldId = 0; fieldId < numFields; fieldId++) {
            current.setNext(new Field(fieldId));
            current = current.next(null);

            // declare start fields
            if (fieldId % 10 == 0 && players.get(fieldId / 10) != null) {
                players.get(fieldId / 10).setStartField(current);

                // declare finish fields
            } else if (fieldId % 10 == 9 && players.get(((fieldId + 1) / 10) % NUM_PLAYERS) != null) {
                tmpPlayer = players.get(((fieldId + 1) / 10) % NUM_PLAYERS);
                // create first finish field
                tmpField = new Field(0, tmpPlayer);
                // connect finish to path
                current.setFork(tmpField);

                // create rest of the finish fields
                for (int j = 1; j < NUM_FIGURES; j++) {
                    tmpField.setNext(new Field(j, tmpPlayer));
                    tmpField = tmpField.next(null);
                }

            }
        }

        // close circle
        Field first = dummy.next(null);
        current.setNext(first);
    }

    /**
     * Returns the winner of this game, or null if no one has won yet.
     * If more than one player fulfill the winning condition, the one with the lowest playerNr will be returned.
     *
     * @return the winner
     */
    public Player getWinner() {
        Player player;
        boolean hasWon;

        for (int playerNr = 0; playerNr < NUM_PLAYERS; playerNr++) {
            player = players.get(playerNr);
            if (player != null) {
                hasWon = true;

                // if all figures of a player are on finish fields...
                for (int figure = 0; figure < NUM_FIGURES; figure++) {
                    if (!player.getFigure(figure).getField().isFinishField()) {
                        hasWon = false;
                        break;
                    }
                }

                // ...he wins
                if (hasWon)
                    return player;
            }
        }

        return null;
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
            current = current.next(player, distance);

            // move figure only if field is not null
            return current != null && moveFigure(figure, current);
        }
    }

    /**
     * Moves a figure to a field after checking the field for availability.
     *
     * @param figure the figure to be moved
     * @param field  the field to move it to
     * @return true on success, false otherwise
     */
    private boolean moveFigure(Figure figure, Field field) {
        if (field.hasFigure()) {
            // another figure is blocking the path
            Figure otherFigure = field.getFigure();

            if (figure.getPlayer().equals(otherFigure.getPlayer()))
                // cant kick own figures, move is impossible
                return false;
            else
                // kick other figure
                moveFigureToOutField(otherFigure);
        }

        // move figure in model (gamelogic)
        figure.setField(field);

        // move figure in view (playfield)
        if (field.isFinishField())
            movingEntity.moveFigureToFinishField(figure.getPlayer().getPlayerNr(), figure.getFigureNr(), field.getFieldNr());
        else
            movingEntity.moveFigure(figure.getPlayer().getPlayerNr(), figure.getFigureNr(), field.getFieldNr());

        return true;
    }

    /**
     * Moves a figure from the game board into the out area aka kicks the figure out of the game.
     *
     * @param figure the figure to be kicked
     */
    private void moveFigureToOutField(Figure figure) {
        figure.setField(null);
        movingEntity.moveFigureToOutField(figure.getPlayer().getPlayerNr(), figure.getFigureNr());
    }

    /**
     * Moves a figure from the out area onto the board by placing it onto its player's start field.
     *
     * @param figure the figure to be moved
     * @return true on success, false otherwise
     */
    private boolean moveFigureToStartField(Figure figure) {
        return moveFigure(figure, figure.getPlayer().getStartField());
    }
}
