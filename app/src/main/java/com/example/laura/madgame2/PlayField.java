package com.example.laura.madgame2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.laura.madgame2.diceRoll.RollDiceActivity;

public class PlayField extends AppCompatActivity  {

    private Intent intent;

    private RollDiceActivity shakeActivity;

    private static final int NUMBER_IDENTIFIER = 1;

    private int numberRolled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_field);
        this.shakeActivity=new RollDiceActivity();
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
    public void diceRole (View view){

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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==NUMBER_IDENTIFIER){
            if(resultCode==RESULT_OK){
                this.numberRolled=data.getIntExtra("result",-1);
            }
        }
    }
}
