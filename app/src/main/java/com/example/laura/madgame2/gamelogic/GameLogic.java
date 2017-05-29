package com.example.laura.madgame2.gamelogic;

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

        initFigures();

        createBoardPath(numFields);
    }

    /**
     * Initializes the {@link com.example.laura.madgame2.gamelogic.Figure Figures} and associates them with Player Objects. <hr />
     * Note: Figures may be accessed via the {@link com.example.laura.madgame2.gamelogic.Player#getFigure(int) getFigure(int figureNr)} method of the {@link com.example.laura.madgame2.gamelogic.Player Player} class.
     */
    private void initFigures() {
        Player tmpPlayer;
        List<Figure> tmpFigures;

        for (int player = 0; player < NUM_PLAYERS; player++) {
            tmpPlayer = players.get(player);
            tmpFigures = new ArrayList<>();

            for (int fig = 0; fig < NUM_FIGURES; fig++)
                tmpFigures.add(new Figure(tmpPlayer, fig));

            tmpPlayer.setFigures(tmpFigures);
        }
    }

    /**
     * Initializes the linked list of fields also referred to as the "game path".
     * Moved out of constructor to avoid too long method declarations as this code is mostly independent.
     *
     * @param numFields the number of fields in the game path circle
     */
    private void createBoardPath(int numFields) {
        Field dummy = new Field(-1);
        Field current = dummy;
        Field tmpField;
        Player tmpPlayer;

        for (int fieldId = 0; fieldId < numFields; fieldId++) {
            current.setNext(new Field(fieldId));
            current = current.next(null);

            /* fetch the owner of the startfield/finish-path-fork.
             * startfields are on fields 0, 10, 20, 30
             * and finish-path-forks on fields 39, 9, 19, 29
             * for players 0, 1, 2 and 3 respectively */
            tmpPlayer = players.get(((fieldId + 1) / 10) % NUM_PLAYERS);

            // declare start fields
            if (fieldId % 10 == 0 && tmpPlayer != null) {
                tmpPlayer.setStartField(current);

                // declare finish fields
            } else if (fieldId % 10 == 9 && tmpPlayer != null) {
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

/*
    public selectfigure(Figure figure){
        figure = findViewById(R.id.Figur);

    }
*/

    /**
     * Returns the winner of this game, or null if no one has won yet.
     * If more than one player fulfill the winning condition, the one with the lowest playerNr will be returned.
     *
     * @return the winner, or null if there is none
     */
    public Player getWinner() {
        Player player;

        for (int playerNr = 0; playerNr < NUM_PLAYERS; playerNr++) {
            player = players.get(playerNr);
            if (player != null && hasWon(player))
                return player;
        }

        return null;
    }

    /**
     * Checks whether a Player fulfills the winning condition, i.e. all of his Figures are on finish fields.
     *
     * @param player the Player to check
     * @return true if the player has won
     */
    private boolean hasWon(Player player) {
        if (player == null)
            return false;

        // if all figures of a player are on finish fields...
        for (int figure = 0; figure < NUM_FIGURES; figure++)
            if (!player.getFigure(figure).getField().isFinishField())
                return false;

        // ...he wins
        return true;
    }

    /**
     * Checks whether a given player has no Figures on the Field, i.e. all of his Figures are on "out fields".
     *
     * @param player the Player to check for
     * @return true if all the Player's Figures are on out fields
     */
    public boolean hasNoFiguresOnField(Player player) {
        if (player == null)
            return false;

        for (int i = 0; i < NUM_FIGURES; i++) {
            if (player.getFigure(i).getField() != null)
                return false;
        }

        return true;
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

    public int highlight(Figure figure, int distance) {

        Field current = figure.getField();
        Player player = figure.getPlayer();

        if (current == null)
            return player.getStartField().getFieldNr();

        // figure is somewhere on the game board
        for (int i = 0; i < distance; i++)
            current = current.next(player);
        return current.getFieldNr();
    }


    /**
     * Moves a figure to a field after checking the field for availability.
     *
     * @param figure the figure to be moved
     * @param field  the field to move it to
     * @return true on success, false otherwise
     */
    private boolean moveFigure(Figure figure, Field field) {
        if (figure == null)
            return false;
        if (field == null)
            return moveFigureToOutField(figure);
        if (field.hasFigure()) {
            // another figure is blocking the path
            Figure otherFigure = field.getFigure();

            if (figure.getPlayer() == otherFigure.getPlayer())
                // cant kick own figures, move is impossible
                return false;
            else
                // kick other figure
                moveFigureToOutField(otherFigure);
        }

        // move figure in model (gamelogic)
        int i = 0;

        //figure.setFigureNr(i);
      //  figure.setField(field);

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
    private boolean moveFigureToOutField(Figure figure) {
        if (figure != null) {
            figure.setField(null);
            movingEntity.moveFigureToOutField(figure.getPlayer().getPlayerNr(), figure.getFigureNr());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Moves a figure from the out area onto the board by placing it onto its player's start field.
     *
     * @param figure the figure to be moved
     * @return true on success, false otherwise
     */
    private boolean moveFigureToStartField(Figure figure) {
        if (figure == null)
            return false;
        return moveFigure(figure, figure.getPlayer().getStartField());
    }
}
