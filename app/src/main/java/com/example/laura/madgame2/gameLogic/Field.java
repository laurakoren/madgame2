package com.example.laura.madgame2.gameLogic;

/**
 * A single field on the game board. Can be occupied by a player's figure.
 */
public class Field {
    //private int matrixCoordsX;
    //private int matrixCoordsY;
    private int fieldNr;

    private Field next;
    //private Field prev;

    private Figure figure;

    // TODO: abzweigungen (->Zielfelder)


    public Field(int fieldNr) {
       // this.matrixCoordsX = matrixCoordsX;
       // this.matrixCoordsY = matrixCoordsY;
        this.fieldNr=fieldNr;
        this.figure = null;
    }

    public Field next() {
        return next;
    }

    public void setNext(Field next) {
        this.next = next;
    }

    public boolean hasFigure() {
        return this.figure != null;
    }

    public Figure getFigure() {
        return figure;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
        //if (this.figure.getField() != this)
        //    this.figure.setField(this);
    }


}
