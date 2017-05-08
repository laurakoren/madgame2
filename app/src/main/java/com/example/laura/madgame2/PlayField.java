package com.example.laura.madgame2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laura.madgame2.diceRoll.RollDiceActivity;
import com.example.laura.madgame2.gameLogic.GameLogic;
import com.example.laura.madgame2.gameLogic.MovesFigures;
import com.example.laura.madgame2.gameLogic.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayField extends AppCompatActivity implements MovesFigures {

    private Intent intent;

    private static final int NUMBER_IDENTIFIER = 1;

    private int numberRolled;

    private GameLogic gameLogic;

    private List<Player> players;
    private List<View> fieldViews;
    private List<List<View>> figureViews;
    private List<List<ViewGroup.LayoutParams>> outFields;

    private static final int NUM_FIELDS = 40;

    private int countTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_field);
        countTurn = 0;
        TextView outPutText = (TextView) getViewById("PlayerTurn");
        outPutText.setText("Spieler 0 starte Spiel!");

        players = new ArrayList<>();
        players.add(new Player(0));
        players.add(new Player(1));
        players.add(new Player(2));
        players.add(new Player(3));

        // save fieldViews into list
        fieldViews = new ArrayList<>();
        for (int i = 0; i < NUM_FIELDS; i++) {
            fieldViews.add(getViewById("iv_field" + i));
        }

        // save finish fieldViews into separate list
        //List<View> finishFields = new ArrayList<View>();

        figureViews = new ArrayList<>();
        List<View> tmpFigViews;

        outFields = new ArrayList<>();
        List<ViewGroup.LayoutParams> tmpLayoutParams;

        View tmpView;

        for (int player = 0; player < 4; player++) {
            tmpFigViews = new ArrayList<>();
            tmpLayoutParams = new ArrayList<>();
            for (int figure = 0; figure < 4; figure++) {
                tmpView = getViewById("iv_player" + player + "_figure" + figure);
                tmpFigViews.add(tmpView);
                tmpLayoutParams.add(tmpView.getLayoutParams());
            }
            figureViews.add(tmpFigViews);
            outFields.add(tmpLayoutParams);
        }

        gameLogic = new GameLogic(players, NUM_FIELDS, this);
    }

    public void diceRoll(View view) {
        onPause();
        intent = new Intent(this, RollDiceActivity.class);
        startActivityForResult(intent, NUMBER_IDENTIFIER);
    }

    /**
     * Onclick Methode für die einzelnen Spielfelder mit Figuren drauf
     */
    public void click(View viewIn) {

        String idOut = viewIn.getResources().getResourceEntryName(viewIn.getId());
        //int pt1=players.get(0).getPlayerNr();
        //players.get(0).getFigures().get(0);

        Pattern p = Pattern.compile("(.*)(\\d+)(.*)(\\d+)");
        Matcher m = p.matcher(idOut);

        if (m.matches()) {
            Toast.makeText(getApplication(), "Player: " + m.group(2) + " " + "Figure: " + m.group(4),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Returns the associated UI element (view) for the given id.
     *
     * @param id the id name declared in the activities xml layout
     * @return the View object
     */
    private View getViewById(String id) {
        return findViewById(getResources().getIdentifier(id, "id", getPackageName()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NUMBER_IDENTIFIER) {
            if (resultCode == RESULT_OK) {

                TextView outPutText = (TextView) getViewById("PlayerTurn");
                int player = countTurn;
                this.numberRolled = data.getIntExtra("result", -1);

                if (this.numberRolled != 6) {
                    gameLogic.draw(players.get(player).getFigure(0), this.numberRolled);
                    countTurn++;
                    countTurn %= 4;
                    outPutText.setText("Spieler " + countTurn + ", du bist dran!");
                } else {
                    gameLogic.draw(players.get(player).getFigure(0), this.numberRolled);
                    outPutText.setText("Spieler " + countTurn + ", du darfst erneut würfeln!");
                }


                //   this.numberRolled=data.getIntExtra("result",-1);
                //  gameLogic.draw(players.get(0).getFigure(0), this.numberRolled);
            }
        }
    }

    /**
     * Moves a figure to a given field by moving its ImageView to the field's ImageView.
     *
     * @param playerNr the player the figure belongs to
     * @param figureNr the number of the figure
     * @param fieldNr  the number of the field to move to
     */
    @Override
    public void moveFigure(int playerNr, int figureNr, int fieldNr) {
        figureViews.get(playerNr).get(figureNr).setLayoutParams(fieldViews.get(fieldNr).getLayoutParams());
    }

    @Override
    public void moveFigureToOutField(int playerNr, int figureNr) {
        figureViews.get(playerNr).get(figureNr).setLayoutParams(outFields.get(playerNr).get(figureNr));
    }
}
