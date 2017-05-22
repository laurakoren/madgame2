package com.example.laura.madgame2.gamelogic;

public interface MovesFigures {
    void moveFigure(int playerNr, int figureNr, int fieldNr);

    void moveFigureToOutField(int playerNr, int figureNr);

    void moveFigureToFinishField(int playerNr, int figureNr, int finishFieldNr);
}
