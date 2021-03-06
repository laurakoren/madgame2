package com.example.laura.madgame2;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.laura.madgame2.diceroll.RollDiceActivity;
import com.example.laura.madgame2.highscore.HighscoreActivity;
import com.example.laura.madgame2.highscore.ScoreEdit;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    private static final int NUMBER_IDENTIFIER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScoreEdit.initializeFile("Highscore", this);
    }

    public void setRollDiceActivity(View view) {
        intent = new Intent(this, RollDiceActivity.class);
        startActivityForResult(intent, NUMBER_IDENTIFIER);
    }

    public void setMultiplayerActivity(View view) {
        intent = new Intent(this, MultiplayerActivity.class);
        startActivity(intent);
    }


    public void onClickPlayNow(View view) {
        intent = new Intent(this, PlayField.class);
        startActivity(intent);
    }


    public void onClickShowHighscores(View view) {
        intent = new Intent(this, HighscoreActivity.class);
        startActivity(intent);
    }


}
