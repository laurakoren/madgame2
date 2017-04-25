package com.example.laura.madgame2.diceRoll;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.laura.madgame2.R;

import java.util.Random;

public class ShakeActivity extends AppCompatActivity {
    boolean test = false;
    private Button roll_button;

    private ImageView dice_view;

    private Random randomNumber = new Random();

    private int rolledNumber;

    private Button cheat_button;

    private boolean cheated = false;

    //Variablen fürs Erkennen ob Gerät geschüttelt wurde
    private ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    //zum Abspielen des Sounds
    private MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_roll);

        roll_button = (Button) findViewById(R.id.roll_button);
        dice_view = (ImageView) findViewById(R.id.dice_view);
        cheat_button = (Button) findViewById(R.id.cheat_button);

        this.mp = MediaPlayer.create(this, R.raw.dicesound);


        //normaler Würfelbutton
        roll_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rolledNumber = randomNumber.nextInt(6) + 1; //nextInt(6) gibt Zahlen von 0 bis 5 -> daher + 1
                        doAnimationAndSound();
                        //wird hier extra nochmal auf false gesetzt, falls im vorherigen Zug gecheated wurde
                        cheated = false;
                        Toast.makeText(ShakeActivity.this, rolledNumber + " Gewürfelt!", Toast.LENGTH_SHORT).show();
                    }
                });

        //Button zum Schummeln
        cheat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                final CharSequence numbers[] = new CharSequence[] {"1", "2", "3", "4", "5", "6"};

                //DialogBox wo man eintragen kann welche Zahl man gerne würfeln würde.
                AlertDialog.Builder builder = new AlertDialog.Builder(ShakeActivity.this);
                builder.setTitle("Wähle die Zahl aus die du würfeln möchtest!");
                builder.setItems(numbers, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //da ein CharSequenze als Parameter erwartet wurde muss diese noch zu einem Integer gemacht werden
                        rolledNumber = Integer.parseInt(numbers[which].toString());
                        doAnimationAndSound();
                        cheated = true;
                    }
                });
                builder.show();
            }
        });

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new OnShakeListener() {
            @Override
            public void onShake(int count) {
                rolledNumber = randomNumber.nextInt(6) + 1; //nextInt(6) gibt Zahlen von 0 bis 5 -> daher + 1
                doAnimationAndSound();
                cheated = false;
            }
        });


    }

    //Methode die den Würfel animiert und den Würfelsound abspielt
    private void doAnimationAndSound(){
        changePicture(rolledNumber);
        Animation animateDice = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animatedice);
        mp.start();
        dice_view.startAnimation(animateDice);

    }
    //Methode die dem Würfelergebnis das richtige Bild zuordnet
    private void changePicture(int wurf){
        switch(wurf){
            case 1:
                dice_view.setImageResource(R.drawable.one);
                break;
            case 2:
                dice_view.setImageResource(R.drawable.two);
                break;
            case 3:
                dice_view.setImageResource(R.drawable.three);
                break;
            case 4:
                dice_view.setImageResource(R.drawable.four);
                break;
            case 5:
                dice_view.setImageResource(R.drawable.five);
                break;
            case 6:
                dice_view.setImageResource(R.drawable.six);
                break;
            default:
                dice_view.setImageResource(R.drawable.dice_standard);
        }

    }


    public boolean getCheat(){
        return this.cheated;
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);

    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }


}
