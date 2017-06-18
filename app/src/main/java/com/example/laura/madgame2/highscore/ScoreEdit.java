package com.example.laura.madgame2.highscore;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Michael on 30.05.2017.
 */

public class ScoreEdit {


    private static SharedPreferences.Editor edit;
    private static SharedPreferences sharedPref;
    private static Context context;

    //somit kann die Klasse nicht von außen instantiiert werden
    private ScoreEdit(){
    }

    //wird 1x aufgerufen in der MainActivity zum Initialisieren
    public static void initializeFile(String filename, Context cont) {
            context = cont;
            if (context.getSharedPreferences(filename, Context.MODE_PRIVATE) != null) {
                sharedPref = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
                edit = sharedPref.edit();
                //Die Felder werden gleich mit Key und Wert 0 initialisiert
                if(sharedPref.getAll().values().isEmpty()){
                putValues();
            }}

    }


    public static void updateScore(String key){
        if(sharedPref == null){
            return;
        }
        int amount = Integer.parseInt(sharedPref.getString(key, "-1"));
        amount++;
        edit.putString(key, Integer.toString(amount));
        edit.apply();
    }



    public static void showScores(List<TextView>tvs) {
       Object[] a = sharedPref.getAll().values().toArray();
       for(int i = 0; i < a.length; i++){
            for(int j = 0; j < tvs.size(); j++){
                tvs.get(i).setText(a[j].toString());
                i++;
            }
        }
    }

    public static void clearScores(List<TextView>tvs){
        edit.clear();
        edit.commit();
        setValuesToZero(tvs);
        putValues();
    }

    private static void setValuesToZero(List<TextView>tvs){
        for(int i = 0; i < tvs.size(); i++){
            tvs.get(i).setText("0");
        }
    }

    private static void putValues(){
        edit.putString("amountDiceRolls", "0");
        edit.putString("gamesWon", "0");
        edit.putString("gamesCheated", "0");
        edit.putString("cheaterCaught","0");
        edit.apply();
    }


}
