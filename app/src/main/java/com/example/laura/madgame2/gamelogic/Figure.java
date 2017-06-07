package com.example.laura.madgame2.gamelogic;

/**
 * A single game figure/piece. Belongs to a player and is placed on a field.
 */
public class Figure {

    private int figureNr;

    /**
     * The Field this Figure is occupying. If this Figure is in the "out area", field is set to null.
     */
    private Field field;

    private Player player;

    Figure(Player player, int figureNr) {
        this.player = player;
        this.figureNr = figureNr;

        this.field = null;
    }

    public int getFigureNr() {
        return figureNr;
    }

    public Field getField() {
        return field;
    }

    /**
     * Moves this Figure to another field.
     *
     * @param field the Field to move to
     */
    void setField(Field field) {
        // remove this from former field
        if (this.field != null)
            this.field.setFigure(null);

        // assign new field
        this.field = field;

        if (field != null)
            field.setFigure(this);
    }

    Player getPlayer() {
        return player;
    }
}
