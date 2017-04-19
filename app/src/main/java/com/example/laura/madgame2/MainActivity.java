package com.example.laura.madgame2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.laura.madgame2.diceRoll.ShakeActivity;

public class MainActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void setRollDiceActivity(View view){
        intent = new Intent(this, ShakeActivity.class);
        startActivity(intent);
    }

    public void setMultiplayerActivity(View view){
        intent = new Intent(this, MultiplayerActivity.class);
        startActivity(intent);
    }


}
