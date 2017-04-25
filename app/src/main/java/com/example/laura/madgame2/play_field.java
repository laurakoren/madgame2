package com.example.laura.madgame2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.laura.madgame2.diceRoll.ShakeActivity;

public class play_field extends AppCompatActivity  {

    private Intent intent;

    private ShakeActivity shakeActivity = new ShakeActivity();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_field);


    }

    @Override
    public void onPause(){          // sichert den aktuellen Spiel-/GUI-Stand

        super.onPause();
    }

    @Override
    public void onResume (){        // ruft den gesicherten Spiel-/GUI-Stand auf

        super.onResume();


        Toast.makeText(play_field.this, shakeActivity.getRolledNumber() + " Gewürfelt!", Toast.LENGTH_SHORT).show();
        changePosition();
    }



    @SuppressWarnings("deprecation")
    public void diceRole (View view){

        onPause();
        intent = new Intent(this, ShakeActivity.class);
        startActivity(intent);



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

        ImageView imageView = (ImageView) findViewById(R.id.iv_red1);

    }
}
