package com.example.laura.madgame2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laura.madgame2.diceroll.RollDiceActivity;
import com.example.laura.madgame2.gamelogic.GameLogic;
import com.example.laura.madgame2.gamelogic.MovesFigures;
import com.example.laura.madgame2.gamelogic.Player;
import com.example.laura.madgame2.gamestate.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayField extends AppCompatActivity implements MovesFigures {

    private Intent intent;
    int s0=0;
    public void setS0(int S0){S0=s0;}
    int s1=0;
    int s2=0;
    int s3=0;
    public void setS1(int S1){S1=s1;}
    public void setS2(int S2){S2=s2;}
    public void setS3(int S3){S3=s3;}
    public int getS0(){return s0;}
    public int getS1(){return s1;}
    public int getS2(){return s2;}
    public int getS3(){return s3;}

    private static final int NUMBER_IDENTIFIER = 1;

    private int numberRolled;
    private static boolean initialized = false;

    private GameLogic gameLogic;
    private List<Player> players;

    private List<View> fieldViews;
    private List<List<View>> finishFieldViews;
    private List<List<View>> figureViews;
    private List<List<ViewGroup.LayoutParams>> outFields;
    private TextView outPutText;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor edit;

    private static final int NUM_FIELDS = 40;

    private int countTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_field);

        sharedPref = getSharedPreferences("Highscore",Context.MODE_PRIVATE);
        edit = sharedPref.edit();

        //Felder fürs Speichern von Highscores initialisieren
        if(initialized==false) {
            initializeValues();
        }



        countTurn = 0;
        outPutText = (TextView) getViewById("PlayerTurn");
        outPutText.setText("Spieler 0 starte Spiel!");


        players = new ArrayList<>();
        players.add(new Player(0));
        players.add(new Player(1));
        players.add(new Player(2));
        players.add(new Player(3));

        // save fieldViews into list
        fieldViews = new ArrayList<>();
        for (int i = 0; i < NUM_FIELDS; i++)
            fieldViews.add(getViewById("iv_field" + i));

        // save finishFieldViews, figureViews and outField coords into list
        finishFieldViews = new ArrayList<>();
        List<View> tmpFinViews;
        figureViews = new ArrayList<>();
        List<View> tmpFigViews;
        outFields = new ArrayList<>();
        List<ViewGroup.LayoutParams> tmpLayoutParams;

        View tmpView;
        for (int player = 0; player < 4; player++) {
            tmpFinViews = new ArrayList<>();
            tmpFigViews = new ArrayList<>();
            tmpLayoutParams = new ArrayList<>();

            for (int count = 0; count < 4; count++) {
                // add finishFieldViews to array
                tmpFinViews.add(getViewById("iv_player" + player + "_finish" + count));

                // add player views and out field coords to arrays
                tmpView = getViewById("iv_player" + player + "_figure" + count);
                tmpFigViews.add(tmpView);
                tmpLayoutParams.add(tmpView.getLayoutParams());
            }

            finishFieldViews.add(tmpFinViews);
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

    public void testState(View view){
        Controller cont = Controller.getInstance();


    }

    /**
     * Onclick Methode für die einzelnen Spielfelder mit Figuren drauf
     */
    public void click(View viewIn) {

        String idOut = viewIn.getResources().getResourceEntryName(viewIn.getId());

        Pattern p = Pattern.compile("(.*)(\\d+)(.*)(\\d+)");
        Matcher m = p.matcher(idOut);

        if (m.matches()) {
            Toast.makeText(getApplication(), "Player: " + m.group(2) + " " + "Figure: " + m.group(4),
                    Toast.LENGTH_SHORT).show();
        }

        int fieldreturn = gameLogic.highlight(players.get(Integer.parseInt(m.group(2))).getFigure(Integer.parseInt(m.group(4))), 5);
        final View img = getViewById("iv_field" + fieldreturn);
        img.setBackgroundResource(R.drawable.fig_empty);

        new CountDownTimer(2000, 1000) {
            public void onFinish() {
                img.setBackgroundResource(R.drawable.clear_circle);
            }

            public void onTick(long millisUntilFinished) {
                // unused
            }
        }.start();

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

                outPutText = (TextView) getViewById("PlayerTurn");
                int player = countTurn;
                this.numberRolled = data.getIntExtra("result", -1);

                if (this.numberRolled != 6 && player==0 ) {
                    gameLogic.draw(players.get(player).getFigure(0), this.numberRolled);
                    countTurn++;
                    countTurn %= 4;
                    outPutText.setText("Spieler " + countTurn + ", du bist dran!");
                } else if(player==0){
                    gameLogic.draw(players.get(player).getFigure(s0), this.numberRolled);
                    outPutText.setText("Spieler " + countTurn + ", du darfst erneut würfeln!");
                    s0=s0+1;

                    if(s0==5){
                        s0=0;
                    }}
                if (this.numberRolled != 6 && player==1 ) {
                    saveAmountDiceRolls();
                } else {
                    gameLogic.draw(players.get(player).getFigure(0), this.numberRolled);
                    countTurn++;
                    countTurn %= 4;
                    outPutText.setText("Spieler " + countTurn + ", du bist dran!");
                } else if (player==1){
                    gameLogic.draw(players.get(player).getFigure(s1), this.numberRolled);
                    outPutText.setText("Spieler " + countTurn + ", du darfst erneut würfeln!");
                    saveAmountDiceRolls();
                    s1=s1+1;
                    if(s1==5){
                        s1=0;
                    }
                }
                if (this.numberRolled != 6 && player==2 ) {
                    gameLogic.draw(players.get(player).getFigure(0), this.numberRolled);
                    countTurn++;
                    countTurn %= 4;
                    outPutText.setText("Spieler " + countTurn + ", du bist dran!");
                } else if(player==2) {
                    gameLogic.draw(players.get(player).getFigure(s2), this.numberRolled);
                    outPutText.setText("Spieler " + countTurn + ", du darfst erneut würfeln!");
                    s2=s2+1;

                    if(s2==5){
                        s2=0;
                    }}
                if (this.numberRolled != 6 && player==3 ) {
                    gameLogic.draw(players.get(player).getFigure(0), this.numberRolled);
                    countTurn++;
                    countTurn %= 4;
                    outPutText.setText("Spieler " + countTurn + ", du bist dran!");
                } else if(player==3) {
                    gameLogic.draw(players.get(player).getFigure(s3), this.numberRolled);
                    outPutText.setText("Spieler " + countTurn + ", du darfst erneut würfeln!");
                    s3 = s3 + 1;

                    if (s3 == 5) {
                        s3 = 0;
                    }
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
        moveTo(figureViews.get(playerNr).get(figureNr), fieldViews.get(fieldNr));
    }

    @Override
    public void moveFigureToOutField(int playerNr, int figureNr) {
        figureViews.get(playerNr).get(figureNr).setLayoutParams(outFields.get(playerNr).get(figureNr));
    }

    @Override
    public void moveFigureToFinishField(int playerNr, int figureNr, int finishFieldNr) {
        moveTo(figureViews.get(playerNr).get(figureNr), finishFieldViews.get(playerNr).get(finishFieldNr));
    }

    /**
     * Helper function to move a view to the location of another view
     *
     * @param target      to view to move
     * @param destination the view to move it to
     */
    private void moveTo(View target, View destination) {
        if (target != null && destination != null)
            target.setLayoutParams(destination.getLayoutParams());
    }

    /**
     * Helper function do display a toast message.
     *
     * @param s the message to display
     */
    public void toast(String s) {
        Toast.makeText(getApplication(), s, Toast.LENGTH_SHORT).show();
    }

    /**
     * Functions to save the highscores in the Highscores file
     */


    //muss aufgerufen werden, wenn ein Spieler gewonnen hat.
    private void saveAmountGamesWon(){
        int amount = Integer.parseInt(sharedPref.getString("gamesWon", "100"));
        amount++;
        edit.putString("gamesWon",amount+"");
        edit.apply();
    }


    private void saveAmountDiceRolls(){
        int amount = Integer.parseInt(sharedPref.getString("amountDiceRolls", "100"));
        amount++;
        edit.putString("amountDiceRolls",amount+"");
        edit.apply();
    }


    private void initializeValues(){
        initialized=true;
        edit.putString("gamesWon","0");
        edit.putString("amountDiceRolls","0");
        edit.apply();
    }



}
