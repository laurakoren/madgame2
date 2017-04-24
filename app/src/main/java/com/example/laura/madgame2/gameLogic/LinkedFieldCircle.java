package com.example.laura.madgame2.gameLogic;

/**
 * Model for the circular path on the game board.
 */
class LinkedFieldCircle {
    // TODO: besseren Namen für die Klasse finden

    private Field first;

    public LinkedFieldCircle() {
        this.first = new Field(1);

        Field current = first;

        // platziert alle felder in den ersten paar zeilen der matrix. muss noch umgebaut werden.
        /*for (int i = 2; i <= 40; i++) {
            current.setNext(new Field(((i - 1) % 11) + 1, ((i - 1) / 11) + 1));
            //current.next().setPrev(current);

            current = current.next();
        }*/

        current.setNext(first);
    }

    public Field getFirst() {
        return first;
    }
}
