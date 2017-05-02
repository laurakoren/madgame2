package com.example.laura.madgame2.gameLogic;

import android.view.View;
import android.view.ViewGroup;

/**
 * A single field on the game board. Can be occupied by a player's figure.
 */
public class Field {

    private Field next;

    private Figure figure;

    private View view;

    private boolean isFinish;
    private Field fork;

    Field(View view) {
        this.view = view;
        this.figure = null;

        this.fork = null;
        this.isFinish = false;
    }

    /**
     * Returns the next Field for the given Player.
     * That is, if the Player were to move a piece from this Field with a dice roll of one, the Field the piece would land on matches this methods return value.
     */
    Field next(Player player) {
        if (player == null)
            return next;

        // TODO: abzweigungen (->Zielfelder)

        return next;
    }

    Field next() {
        return next;
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

    ViewGroup.LayoutParams getViewLayout() {
        return view.getLayoutParams();
    }


}
