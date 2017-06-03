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

import java.util.ArrayList;
import java.util.List;

public class HighscoreActivity extends AppCompatActivity {

    private  TextView tv_setGamesWon;
    private  TextView tv_setAmountDiceRolls;
    private  TextView tv_setAmountCheated;
    private  TextView tv_setCheaterCaught;
    private  Button btn_Reset;
    private List<TextView> tv_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        tv_setGamesWon = (TextView)findViewById(R.id.tv_setGamesWon);
        tv_setAmountDiceRolls = (TextView)findViewById(R.id.tv_setAmountDiceRolls);
        tv_setAmountCheated = (TextView)findViewById(R.id.tv_setAmountCheated);
        tv_setCheaterCaught = (TextView)findViewById(R.id.tv_setCheaterCaught);
        tv_list = new ArrayList<TextView>();
        //in alphabetischer Reihenfolge in Liste hinzufügen
        tv_list.add(tv_setAmountDiceRolls);
        tv_list.add(tv_setCheaterCaught);
        tv_list.add(tv_setAmountCheated);
        tv_list.add(tv_setGamesWon);
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
                           ScoreEdit.clearScores(tv_list);
                           Toast.makeText(getApplication(), "Highscores wurden zurückgesetzt!",
                                   Toast.LENGTH_SHORT).show();
                       }
                    }
                });
                builder.show();
                            }
        });

      ScoreEdit.showScores(tv_list);

    }


}
