package com.example.laura.madgame2.diceroll;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.laura.madgame2.R;

import java.util.Random;


public class RollDiceActivity extends AppCompatActivity {

    private Button rollButton;

    private ImageView diceView;

    private Random randomNumber = new Random();

    private int rolledNumber;

    private Button cheatButton;

    private static boolean cheated = false;

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

        rollButton = (Button) findViewById(R.id.roll_button);
        diceView = (ImageView) findViewById(R.id.dice_view);
        cheatButton = (Button) findViewById(R.id.cheat_button);

        this.mp = MediaPlayer.create(this, R.raw.dicesound);


        //normaler Würfelbutton
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rolledNumber = randomNumber.nextInt(6) + 1; //nextInt(6) gibt Zahlen von 0 bis 5 -> daher + 1
                doAnimationAndSound();
                Toast.makeText(RollDiceActivity.this, rolledNumber + " Gewürfelt!", Toast.LENGTH_SHORT).show();
                setButtonsOff();
                sendData();
            }
        });

        //Button zum Schummeln
        cheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                final CharSequence[] numbers = new CharSequence[]{"1", "2", "3", "4", "5", "6"};
                //DialogBox wo man eintragen kann welche Zahl man gerne würfeln würde.
                new AlertDialog.Builder(RollDiceActivity.this).
                        setTitle("Wähle die Zahl aus die du würfeln möchtest!")
                        .setItems(numbers, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //da ein CharSequenze als Parameter erwartet wurde muss diese noch zu einem Integer gemacht werden
                                rolledNumber = Integer.parseInt(numbers[which].toString());
                                doAnimationAndSound();
                                setCheat(true);
                                setButtonsOff();
                                sendData();
                            }
                        }).show();
            }
        });

        if (cheated) {
            cheatButton.setEnabled(false);
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        //Listener für Shake Event! Es wird beim Shaken normal gewürfelt
        mShakeDetector.setOnShakeListener(new OnShakeListener() {
            @Override
            public void onShake(int count) {
                rolledNumber = randomNumber.nextInt(6) + 1; //nextInt(6) gibt Zahlen von 0 bis 5 -> daher + 1
                doAnimationAndSound();
                setCheat(false); //redundant?
                setButtonsOff();
                sendData();

            }
        });


    }

    //Methode die den Würfel animiert und den Würfelsound abspielt
    private void doAnimationAndSound() {
        changePicture(rolledNumber);
        Animation animateDice = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animatedice);
        mp.start();
        diceView.startAnimation(animateDice);

    }

    //Methode die dem Würfelergebnis das richtige Bild zuordnet
    private void changePicture(int wurf) {
        switch (wurf) {
            case 1:
                diceView.setImageResource(R.drawable.one);
                break;
            case 2:
                diceView.setImageResource(R.drawable.two);
                break;
            case 3:
                diceView.setImageResource(R.drawable.three);
                break;
            case 4:
                diceView.setImageResource(R.drawable.four);
                break;
            case 5:
                diceView.setImageResource(R.drawable.five);
                break;
            case 6:
                diceView.setImageResource(R.drawable.six);
                break;
            default:
                diceView.setImageResource(R.drawable.dice_standard);
        }

    }


    private void setButtonsOff() {
        this.cheatButton.setClickable(false);
        this.rollButton.setClickable(false);
    }

    //Methode um einen Rückgabewert der Acitivity zu setzen
    private void sendData() {
        //Fenster soll erst nach gewisser Zeit (0,5 sek) geschlossen werden!
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", rolledNumber);
                returnIntent.putExtra("hasCheated", cheated);
                setResult(RESULT_OK, returnIntent);
                //Fenster schließen nach dem Würfeln
                finish();
            }
        }, 500);
    }


    public int getRolledNumber() {
        return this.rolledNumber;
    }

    public static boolean getCheat() {
        return cheated;
    }

    public static void setCheat(boolean help) {
        cheated = help;
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