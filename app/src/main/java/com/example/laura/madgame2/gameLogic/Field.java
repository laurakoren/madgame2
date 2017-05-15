package com.example.laura.madgame2.gameLogic;

/**
 * A single field on the game board. Can be occupied by a player's figure.
 */
public class Field {

    private Field next;

    private Figure figure;

    private int fieldNr;

    private Player finishFieldForPlayer;

    private Field fork;

    Field(int fieldNr, Player finishFieldForPlayer) {
        this.fieldNr = fieldNr;
        this.finishFieldForPlayer = finishFieldForPlayer;
        this.figure = null;
        this.fork = null;
    }

    Field(int fieldNr) {
        this(fieldNr, null);
    }

    /**
     * Returns the next Field for the given Player.
     * That is, if the Player were to move a piece from this Field with a dice roll of one, the Field the piece would land on matches this methods return value.
     * If called with argument null, finish fields are ignored.
     */
    Field next(Player player) {
        if (player == null)
            return next;

        // if this is the last field before the player's finish go there
        if (fork != null && player.equals(fork.getFinishFieldOwner()))
            return fork;

        return next;
    }

    Field next(Player player, int times) {
        if (times < 0)
            return null;
        if (times == 0)
            return this;

        Field current = this;
        for (int i = 0; i < times; i++) {
            // if current field is null (i.e. end of path is crossed) return null
            if (current == null)
                return null;
            current = current.next(player);
        }
        return current;
    }

    void setNext(Field next) {
        this.next = next;
    }

    boolean hasFigure() {
        return this.figure != null;
    }

    Figure getFigure() {
        return figure;
    }

    /**
     * Puts a Figure on this Field.
     * Note: It is recommended to do this by calling moveTo on a given Figure.
     *
     * @param figure the Figure to place on this Field
     */
    void setFigure(Figure figure) {
        this.figure = figure;
    }

    int getFieldNr() {
        return fieldNr;
    }

    private Player getFinishFieldOwner() {
        return finishFieldForPlayer;
    }

    boolean isFinishField() {
        return finishFieldForPlayer != null;
    }

    void setFork(Field fork) {
        this.fork = fork;
    }
}
