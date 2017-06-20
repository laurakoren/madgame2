package com.example.laura.madgame2.gamelogic;

import com.example.laura.madgame2.gamestate.action.Action;
import com.example.laura.madgame2.gamestate.action.KickFigureAction;
import com.example.laura.madgame2.gamestate.action.MoveFigureAction;

import java.util.ArrayList;
import java.util.List;

/**
 * The internal representation for the game board.
 */
public class GameLogic {

    private static final int NUM_PLAYERS = 4;
    private static final int NUM_FIGURES = 4;

    private List<Player> players;

    public GameLogic(List<Player> players, int numFields) {
        this.players = players;

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
                List<Field> finishFields = new ArrayList<>();

                // create first finish field
                tmpField = new Field(0, tmpPlayer);
                finishFields.add(tmpField);
                // connect finish to path
                current.setFork(tmpField);

                // create rest of the finish fields
                for (int j = 1; j < NUM_FIGURES; j++) {
                    tmpField.setNext(new Field(j, tmpPlayer));
                    tmpField = tmpField.next(null);
                    finishFields.add(tmpField);
                }

                tmpPlayer.setFinishFields(finishFields);
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
    public boolean hasWon(Player player) {
        if (player == null)
            return false;

        // if all figures of a player are on finish fields...
        for (int figure = 0; figure < NUM_FIGURES; figure++)
            if (player.getFigure(figure).getField() == null || !player.getFigure(figure).getField().isFinishField())
                return false;

        // ...he wins
        return true;
    }

    /**
     * Checks whether a given player has no Figures on the Field, i.e. all of his Figures are on "out fields".
     *
     * @param playerNr the number of the player to check for
     * @return true if all the Player's Figures are on out fields
     */
    public boolean hasNoFiguresOnField(int playerNr) {
        Player player = players.get(playerNr);

        for (int i = 0; i < NUM_FIGURES; i++) {
            if (player.getFigure(i).getField() != null)
                return false;
        }

        return true;
    }

    /**
     * Checks whether a Player can move any Figure for a given distance.
     * If that is not the case, the Player may be forced to end his turn.
     *
     * @param playerNr the number of the Player for whom to check
     * @param distance the distance to move Figures
     * @return true if the Player can move any of his Figures for the given distance
     */
    public boolean hasValidMoves(int playerNr, int distance) {
        // check for each figure whether getResultField returns null
        for (int figureNr = 0; figureNr < NUM_FIGURES; figureNr++)
            if (this.getResultField(playerNr, figureNr, distance) != null)
                return true;

        return false;
    }

    /**
     * Returns the Position of a Figure if it were to be drawn over a given distance.
     * This method does not move Figures on the board.
     *
     * @param playerNr the number of the Player that owns the Figure
     * @param figureNr the number of the Figure
     * @param distance the distance to hypothetically move the figure
     * @return the field reached by the move
     * @see #draw(int, int, int)
     */
    public Field getResultField(int playerNr, int figureNr, int distance) {
        if (distance < 0)
            return null;

        Player player = players.get(playerNr);
        Figure figure = player.getFigure(figureNr);
        Field current = figure.getField();
        Field result;

        // determine where the figure is
        if (current == null) {
            // figure is in the out area
            // if player rolled 6, then make the move, else return false
            if (distance == 6)
                result = player.getStartField();
            else
                return null;
        } else {
            // figure is somewhere on the game board
            result = current.next(player, distance);
        }

        // finally, check if result field is already occupied
        if (result != null && result.hasFigure() && figure.getPlayer() == result.getFigure().getPlayer())
            // cant kick own figures, move is impossible
            return null;
        else
            // kicking is handled in the draw method
            return result;
    }

    /**
     * Moves a figure for a given distance over the board.
     * Returns a List of Actions if the move is valid and was executed and returns null else.
     * If null is returned, the board remains unchanged.
     *
     * @param playerNr the number of the player who owns the figure
     * @param figureNr the number of the figure
     * @param distance the distance to move it
     * @return a List of Actions on success, null otherwise
     */
    public List<Action> draw(int playerNr, int figureNr, int distance) {
        if (distance < 0)
            return new ArrayList<>();
        List<Action> actions = new ArrayList<>();
        Figure figure = players.get(playerNr).getFigure(figureNr);

        // find out, where the figure lands
        Field result = this.getResultField(playerNr, figureNr, distance);
        if (result == null)
            return new ArrayList<>();

        // check if there are figures to kick
        if (result.hasFigure()) {
            Figure otherFigure = result.getFigure();
            otherFigure.setField(null);
            actions.add(new KickFigureAction(otherFigure.getPlayer().getPlayerNr(), otherFigure.getFigureNr()));
        }

        // move the figure accordingly
        figure.setField(result);
        actions.add(new MoveFigureAction(playerNr, figureNr, result.getFieldNr(), result.isFinishField()));

        return actions;
    }

    public void handleUpdates(List<Action> actions) {
        if (actions != null) {
            for (Action action : actions) {
                if (action instanceof MoveFigureAction) {
                    MoveFigureAction a = (MoveFigureAction) action;
                    if (a.moveToFinishField) {
                        players.get(a.playerNr).getFigure(a.figureNr).setField(players.get(a.playerNr).getFinishField(a.fieldNr));
                    } else {
                        players.get(a.playerNr).getFigure(a.figureNr).setField(players.get(0).getStartField().next(null, a.fieldNr));
                    }

                } else if (action instanceof KickFigureAction) {
                    KickFigureAction a = (KickFigureAction) action;
                    players.get(a.playerNr).getFigure(a.figureNr).setField(null);
                }
            }
        }
    }
}
