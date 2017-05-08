package com.example.laura.madgame2;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.example.laura.madgame2.diceRoll.RollDiceActivity;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    private static final int NUMBER_IDENTIFIER = 1;
    private int number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void setRollDiceActivity(View view){
        intent = new Intent(this, RollDiceActivity.class);
        //mit startActitivityForResult Methode könnte Würfelergebnis reingeladen werden
        //startActivity(intent);
        startActivityForResult(intent, NUMBER_IDENTIFIER);                                //'''''''''''
    }

    public void setMultiplayerActivity(View view){
        intent = new Intent(this, MultiplayerActivity.class);
        startActivity(intent);
    }

    //####
    public void onClickPlayNow(View view){
        intent = new Intent(this, PlayField.class);
        startActivity(intent);
    }



}
