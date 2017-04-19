package com.example.laura.madgame2.diceRoll;
/**
 * Created by Michi on 12.04.2017.
 */

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeDetector implements SensorEventListener {

    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;

    private OnShakeListener shakeListener;
    private long shakeTimestamp;
    private int shakecounter;

    public void setOnShakeListener(OnShakeListener listener) {
        this.shakeListener = listener;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // ignore
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (shakeListener != null) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;

            // wenn keine Bewegung wahrgenommen wird, dann ist gFoce nahe bei 1
            float gForce = (float)Math.sqrt(gX * gX + gY * gY + gZ * gZ);

            //wenn gForce größer als der min. Threshold ist, dann liegt ein Shake vor
            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                final long now = System.currentTimeMillis();

                // shakes die zu nahe beieinander liegen werden verworfen (500 ms)
                if (shakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return;
                }

                // wenn 3 sekunden nicht geshaked wird, dann wird der counter zurückgesetzt
                if (shakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                    shakecounter = 0;
                }

                shakeTimestamp = now;
                shakecounter++;

                shakeListener.onShake(shakecounter);
            }
        }
    }





}