package com.example.laura.madgame2;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.example.laura.madgame2.diceRoll.ShakeActivity;

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
        intent = new Intent(this, ShakeActivity.class);
        //mit startActitivityForResult Methode könnte Würfelergebnis reingeladen werden
        //startActivity(intent);
        startActivityForResult(intent, NUMBER_IDENTIFIER);
    }

    public void setMultiplayerActivity(View view){
        intent = new Intent(this, MultiplayerActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==NUMBER_IDENTIFIER){
            if(resultCode==RESULT_OK){
                this.number=data.getIntExtra("result",-1);
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText(number+" wurde gewürfelt!!!");
            }
        }
    }

    //####
    public void onClickPlayNow(View view){
        intent = new Intent(this, play_field.class);
        startActivity(intent);
    }



}
