package com.example.laura.madgame2.highscore;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laura.madgame2.R;

import java.util.ArrayList;
import java.util.List;

public class HighscoreActivity extends AppCompatActivity {

    private TextView tvSetGamesWon;
    private TextView tvSetAmountDiceRolls;
    private TextView tvSetAmountCheated;
    private TextView tvSetCheaterCaught;
    private Button btnReset;
    private List<TextView> tvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        tvSetGamesWon = (TextView) findViewById(R.id.tv_setGamesWon);
        tvSetAmountDiceRolls = (TextView) findViewById(R.id.tv_setAmountDiceRolls);
        tvSetAmountCheated = (TextView) findViewById(R.id.tv_setAmountCheated);
        tvSetCheaterCaught = (TextView) findViewById(R.id.tv_setCheaterCaught);
        tvList = new ArrayList<>();
        //in alphabetischer Reihenfolge in Liste hinzufügen
        tvList.add(tvSetAmountDiceRolls);
        tvList.add(tvSetCheaterCaught);
        tvList.add(tvSetAmountCheated);
        tvList.add(tvSetGamesWon);
        btnReset = (Button) findViewById(R.id.btn_Reset);


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = new CharSequence[]{"Ja", "Nein"};

                AlertDialog.Builder builder = new AlertDialog.Builder(HighscoreActivity.this);
                builder.setTitle("Möchstest du die Highscores wirklich löschen?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ("Ja".equals(options[which])) {
                            ScoreEdit.clearScores(tvList);
                            Toast.makeText(getApplication(), "Highscores wurden zurückgesetzt!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
            }
        });

        ScoreEdit.showScores(tvList);

    }


}
