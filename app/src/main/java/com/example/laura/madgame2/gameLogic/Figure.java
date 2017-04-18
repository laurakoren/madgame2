package com.example.laura.madgame2.gameLogic;

/**
 * A single game figure/piece. Belongs to a player and is placed on a field.
 */
public class Figure {

    /**
     * the Field this Figure is occupying
     */
    private Field field;

    // TODO: figur implementieren
    // TODO: zugehörigkeit zu spielern
    // TODO: startfelder

    public Field getField() {
        return field;
    }

    void setField(Field field) {
        this.field = field;
        //if (this.field.getFigure() != this)
        //    this.field.setFigure(this);
    }
}
