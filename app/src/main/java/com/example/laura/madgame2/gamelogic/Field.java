package com.example.laura.madgame2.gamelogic;

/**
 * A single field on the game board. Can be occupied by a player's figure.
 */
public class Field {

    private int fieldNr;

    private Field next;
    private Field fork;

    private Player finishFieldForPlayer;

    private Figure figure;

    Field(int fieldNr, Player finishFieldOwner) {
        this.fieldNr = fieldNr;
        this.finishFieldForPlayer = finishFieldOwner;
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
     *
     * @param player the Player whose move this is
     * @return the Field that results from this part of the move
     * @see #next(Player, int)
     */
    Field next(Player player) {
        if (player == null)
            return next;

        // if this is the last field before the player's finish go there
        if (fork != null && player == fork.getFinishFieldOwner())
            return fork;

        return next;
    }

    /**
     * Returns the Field that results from moving a given distance from this field.
     * i.e. returns the result of calling {@link #next(Player) next()} on this Field for a given number of times.
     *
     * @param player the Player whose move this is
     * @param times  the number of Fields to progress
     * @return the Field that results from this move
     */
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
     * Note: It is recommended to do this by calling {@link com.example.laura.madgame2.gamelogic.Figure#setField(Field) setField()} on a given Figure.
     *
     * @param figure the Figure to place on this Field
     */
    void setFigure(Figure figure) {
        this.figure = figure;
    }

    public int getFieldNr() {
        return fieldNr;
    }

    private Player getFinishFieldOwner() {
        return finishFieldForPlayer;
    }

    public boolean isFinishField() {
        return finishFieldForPlayer != null;
    }

    void setFork(Field fork) {
        this.fork = fork;
    }
}
