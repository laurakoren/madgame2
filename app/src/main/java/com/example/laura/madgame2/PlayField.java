package com.example.laura.madgame2;
import android.view.View.OnClickListener;
import android.widget.Toast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.laura.madgame2.diceRoll.RollDiceActivity;
import com.example.laura.madgame2.gameLogic.GameLogic;
import com.example.laura.madgame2.gameLogic.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayField extends AppCompatActivity  {

    private Intent intent;

    private RollDiceActivity shakeActivity;

    private static final int NUMBER_IDENTIFIER = 1;

    private int numberRolled;

    private GameLogic gameLogic;

    private List<Player> players;

    private static final int NUM_FIELDS = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_field);
        this.shakeActivity=new RollDiceActivity();

        players = new ArrayList<>();
        players.add(new Player(0));
        players.add(null);
        players.add(null);
        players.add(null);

        // save fields into list
        List<View> fields = new ArrayList<>();
        for (int i = 0; i < NUM_FIELDS; i++) {
            fields.add(getViewById("iv_field" + i));
        }

        // save finish fields into separate list
        //List<View> finishFields = new ArrayList<View>();

        List<List<View>> figures = new ArrayList<>();
        List<View> tmp;
        for (int player = 0; player < 4; player++) {
            tmp = new ArrayList<>();
            for (int figure = 0; figure < 4; figure++) {
                tmp.add(getViewById("iv_player" + player + "_figure" + figure));
            }
            figures.add(tmp);
        }

        gameLogic = new GameLogic(players, fields, figures);

        gameLogic.draw(players.get(0).getFigures().get(0), 6, players.get(0));





    }

    @Override
    public void onPause(){          // sichert den aktuellen Spiel-/GUI-Stand

        super.onPause();
    }

    @Override
    public void onResume (){        // ruft den gesicherten Spiel-/GUI-Stand auf

        super.onResume();


        // Toast.makeText(Play_field.this, shakeActivity.getRolledNumber() + " Gewürfelt!", Toast.LENGTH_SHORT).show();
        changePosition();
    }



    @SuppressWarnings("deprecation")
    public void diceRoll(View view){

        onPause();
        intent = new Intent(this, RollDiceActivity.class);
        startActivityForResult(intent, NUMBER_IDENTIFIER);



    }
    @SuppressWarnings("deprecation")
    public void changePosition(){
        /**Todo: Die gewuerfelte Augenzahl mir gewähltem Spielstein ziehen
         * ImageView auf jeder Pos. des Spielfeld's werden je nach Augenzahl und Spieler angesprochen und das jeweilige Image des Spielers,
         * zb. "red1", "green2" usw. angezeigt.
         * wenn Spielzug nicht möglich,  wird der jeweilige Spielstein des Spielers der am Zug ist gesperrt, dass er nur Regelkonforme Züge machen kann.
         *
         * Implementierung: onGoing
         *
         */

        ImageView imageView = (ImageView) findViewById(R.id.iv_player0_figure0);

    }

    public void moveFigureToField(int figureNr, int playerNr, int fieldNr) {

        View fig = getViewById("iv_" + playerNr + "_figure" + figureNr);
        View field = getViewById("iv_field" + fieldNr);

        fig.setLayoutParams(field.getLayoutParams());
    }

    public void testAction(View view) {
        gameLogic.draw(players.get(0).getFigures().get(0), 1, players.get(0));
    }

    //Onclick Methode für die einzelnen Spielfelder mit Figuren drauf

    public void click(View viewIn){

        String idOut = viewIn.getResources().getResourceEntryName(viewIn.getId());
        //int pt1=players.get(0).getPlayerNr();
        //players.get(0).getFigures().get(0);
        Toast.makeText(getApplication(), idOut,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Returns the associated UI element (view) for the given id.
     * @param id the id name declared in the activities xml layout
     * @return the View object
     */
    private View getViewById(String id) {
        return findViewById(getResources().getIdentifier(id, "id", getPackageName()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==NUMBER_IDENTIFIER){
            if(resultCode==RESULT_OK){
                this.numberRolled=data.getIntExtra("result",-1);
                gameLogic.draw(players.get(0).getFigures().get(0), this.numberRolled, players.get(0));
            }
        }
    }
}
