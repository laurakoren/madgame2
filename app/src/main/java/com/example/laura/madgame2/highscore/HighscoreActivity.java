package com.example.laura.madgame2.highscore;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.laura.madgame2.R;
import com.example.laura.madgame2.diceroll.RollDiceActivity;

import org.w3c.dom.Text;

public class HighscoreActivity extends AppCompatActivity {

    TextView tv_setGamesWon;
    TextView tv_setAmountDiceRolls;
    Button btn_Reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        tv_setGamesWon = (TextView)findViewById(R.id.tv_setGamesWon);
        tv_setAmountDiceRolls = (TextView)findViewById(R.id.tv_setAmountDiceRolls);
        btn_Reset = (Button)findViewById(R.id.btn_Reset);



        btn_Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                final CharSequence[] options = new CharSequence[]{"Ja","Nein"};

                AlertDialog.Builder builder = new AlertDialog.Builder(HighscoreActivity.this);
                builder.setTitle("Möchstest du die Highscores wirklich löschen?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       if(options[which].equals("Ja")){
                           clearScores();
                           Toast.makeText(getApplication(), "Highscores wurden zurückgesetzt!",
                                   Toast.LENGTH_SHORT).show();
                       }
                    }
                });
                builder.show();
                            }
        });

      showScores();

    }

    private void showScores(){
        if(getSharedPreferences("Highscore", Context.MODE_PRIVATE)!=null){
            SharedPreferences sharedPref = getSharedPreferences("Highscore", Context.MODE_PRIVATE);
            String number1 = sharedPref.getString("gamesWon","0");
            tv_setGamesWon.setText(number1);
            String number2 = sharedPref.getString("amountDiceRolls","0");
            tv_setAmountDiceRolls.setText(number2);
        }
    }

    private void clearScores(){
        SharedPreferences sharedPref = getSharedPreferences("Highscore", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
        initialDefaultValues();
    }

    private void initialDefaultValues(){
        tv_setGamesWon.setText("0");
        tv_setAmountDiceRolls.setText("0");
    }



}
