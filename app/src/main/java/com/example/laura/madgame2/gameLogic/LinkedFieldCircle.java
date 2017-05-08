package com.example.laura.madgame2.gameLogic;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.laura.madgame2.PlayField;

import java.util.List;

/**
 * Model for the circular path on the game board.
 */
class LinkedFieldCircle {
    // TODO: besseren Namen für die Klasse finden

    private Field first;

    public LinkedFieldCircle(List<View> fields, List<Player> players) {
        Field dummy = new Field(null);
        Field current = dummy;
        Field tmp = null;

        for (int i = 0; i < fields.size(); i++) {

            current.setNext(new Field(fields.get(i)));
            current = current.next();

            // declare start fields
            if (i % 10 == 0 && players.get(i/10) != null) {
                players.get(i / 10).setStartField(current);
            }
        }

        // close circle
        first = dummy.next();
        current.setNext(first);
    }

    public Field getFirst() {
        return first;
    }
}
