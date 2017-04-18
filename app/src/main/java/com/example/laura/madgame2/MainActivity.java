package com.example.laura.madgame2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.laura.madgame2.diceRoll.ShakeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ShakeActivity wird auf die MainActivity raufgepackt
        Intent intent = new Intent(this, ShakeActivity.class);
        startActivity(intent);
    }
}
