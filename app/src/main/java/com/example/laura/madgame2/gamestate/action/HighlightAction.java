package com.example.laura.madgame2.gamestate.action;

/**
 * Created by Alex on 31.05.2017.
 */

public class HighlightAction implements Action {

    int highlightedField;

    public HighlightAction (int highlightedField) {
        this.highlightedField = highlightedField;}


    public int getHighlightedField() {
        return highlightedField;
    }







}
