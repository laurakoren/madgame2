package com.example.laura.madgame2.diceRoll;
/**
 * Created by Michi on 12.04.2017.
 */

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class ShakeDetector implements SensorEventListener {

    // geringste Bewegung die als Shake gelten soll
    private static final int MIN_SHAKE_ACCELERATION = 5;

    // minimale Anzahl an Bewegungen
    private static final int MIN_MOVEMENTS = 2;

    private static final int MAX_SHAKE_DURATION = 500;

    //float Arrays für Schwerkraft und Beschleunigung
    private float[] gravity = { 0.0f, 0.0f, 0.0f };
    private float[] linAcc = { 0.0f, 0.0f, 0.0f };

    private static final int X = 0;
    private static final int Y = 1;
    private static final int Z = 2;

    private OnShakeListener shakeListener;

    private long startTime = 0;
    private int moveCount = 0;


    public ShakeDetector(OnShakeListener shakeListener) {
        this.shakeListener = shakeListener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //wenn Accelerometer einen Shake wahrnimmt
        setCurrentAcceleration(event);

        float maxLinearAcceleration = getMaxCurrLinAcc();

        if (maxLinearAcceleration > MIN_SHAKE_ACCELERATION) {
            long now = System.currentTimeMillis();

            // starTime setzen wenn diese auf 0 war!
            if (startTime == 0) {
                startTime = now;
            }

            long elapsedTime = now - startTime;


            if (elapsedTime > MAX_SHAKE_DURATION) {
                //zuviel Zeit vergangen, damit kein Shake!
                resetShakeDetection();
            }
            else {
                moveCount++;

                //prüfe ob genügend Bewegungen für Shake gemacht wurden
                if (moveCount > MIN_MOVEMENTS) {
                    shakeListener.onShake();

                    //reset für nächsten Shake!!!!
                    resetShakeDetection();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        return;
    }

    private void setCurrentAcceleration(SensorEvent event) {
        //siehe Android developer Site für Formeln!!!
        final float alpha = 0.8f;

        gravity[X] = alpha * gravity[X] + (1 - alpha) * event.values[X];
        gravity[Y] = alpha * gravity[Y] + (1 - alpha) * event.values[Y];
        gravity[Z] = alpha * gravity[Z] + (1 - alpha) * event.values[Z];

        // Schwerkrafteffekte werden entfernt!
        linAcc[X] = event.values[X] - gravity[X];
        linAcc[Y] = event.values[Y] - gravity[Y];
        linAcc[Z] = event.values[Z] - gravity[Z];

    }

    //maximale Beschleunigung zurückgeben
    private float getMaxCurrLinAcc() {
        float maxLinAcc = linAcc[X];

        if (maxLinAcc  <  linAcc[Y]) {
            maxLinAcc  = linAcc[Y];
        }
        if (maxLinAcc  < linAcc[Z]) {
            maxLinAcc  = linAcc[Z];
        }
        return maxLinAcc ;
    }

    private void resetShakeDetection() {
        startTime = 0;
        moveCount = 0;
    }



}