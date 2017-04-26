package com.example.laura.madgame2.gameLogic;

import android.view.View;
import android.view.ViewGroup;

/**
 * A single game figure/piece. Belongs to a player and is placed on a field.
 */
public class Figure {

    /**
     * the Field this Figure is occupying. if this Figure is in the out area field is set to null.
     */
    private Field field;

    /**
     * this Figure's UI element
     */
    private View view;

    /**
     * container of settings to easily reset this figure to its outField
     */
    private ViewGroup.LayoutParams outField;

    private Player player;

    // TODO: figur implementieren
    // TODO: zugehörigkeit zu spielern

    Figure(Player player, View view) {
        this.player = player;
        this.view = view;

        this.field = null;
        this.outField = view.getLayoutParams();
    }

    Field getField() {
        return field;
    }

    /**
     * Moves this Figure to another field.
     *
     * @param field the Field to move to
     */
    void moveTo(Field field) {
        // remove this from former field
        if (this.field != null) {
            this.field.setFigure(null);
        }

        // assign new field
        this.field = field;
        field.setFigure(this);

        // move figure icon
        view.setLayoutParams(field.getViewLayout());
    }

    /**
     * Moves this Figure back to its start field.
     */
    void returnToOut() {
        // remove this from former field
        if (this.field != null) {
            this.field.setFigure(null);
        }

        // reset field and location on board
        this.field = null;
        view.setLayoutParams(outField);
    }

    /**
     * Returns this Figure's start field, i.e. the Field it is placed on at the beginning of the game and returned to if kicked.
     */
    ViewGroup.LayoutParams getOutField(){
        return outField;
    }

    Player getPlayer() {
        return player;
    }
}
