package com.example.challenge;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    // ------ Noise detector -------

    private static final int POLL_INTERVAL = 300;
    private boolean mRunning = false;
    int RECORD_AUDIO = 0;
    private PowerManager.WakeLock mWakeLock;
    private Handler mHandler = new Handler();
    private NoiseDetector mSensor;

    private Runnable mSleepTask = new Runnable() {
        public void run() {
            start();
        }
    };

    private Runnable mPollTask = new Runnable() {
        public void run() {
            double amp = mSensor.getAmplitude();
            System.out.println("Sound:" +amp);
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);
        }
    };

    private void start() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    RECORD_AUDIO);
        }

        mSensor.start();
        if (!mWakeLock.isHeld()) {
            mWakeLock.acquire();
        }
        mHandler.postDelayed(mPollTask, POLL_INTERVAL);
    }

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SensorManager mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);


        // ----- Sound level detector -----

        mSensor = new NoiseDetector();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "NoiseAlert");


        // --------- Light Sensor ---------


        Sensor LightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (LightSensor != null) {
            mySensorManager.registerListener(
                    LightSensorListener,
                    LightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);

        }
        // ----- Accelerometer Sensor -----

        Sensor AccelerometerSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (AccelerometerSensor != null) {
            mySensorManager.registerListener(
                    AccelerometerSensorListener,
                    AccelerometerSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

        // ---- Screen touch coordinate ---

        View myView = findViewById(R.id.main);
        myView.setOnTouchListener(onTouchListener);



    }


    // -- Listenners

    private final SensorEventListener LightSensorListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                float currentLight = event.values[0];
                System.out.println("Light :"+currentLight);
            }
        }
    };

    private final SensorEventListener AccelerometerSensorListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float currentAccelerationY = event.values[1];
                if(currentAccelerationY < 0) {
                    currentAccelerationY = 0;
                }
                System.out.println("Acceleration:" +currentAccelerationY);
            }
        }

    };

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float posY = event.getY();
            if(posY > v.getHeight()) {
                posY = v.getHeight();
            }
            System.out.println("Touch:" + posY);
            return true;
        }
    };
}
