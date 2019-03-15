package com.example.challenge;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Plat> plats;
    private List<Action> actionsBurger, actionsSalade, actionsPizza,
                    actionsHotdog, actionsFrites, actionsSushi,
                    actionsTacos, actionsDonut;

    private int score = 0;
    private TextView tv_score;

    // ------ Noise detector -------

    private static final int POLL_INTERVAL = 300;
    private boolean mRunning = false;
    int RECORD_AUDIO = 0;
    private PowerManager.WakeLock mWakeLock;
    private Handler mHandler = new Handler();
    private NoiseDetector mSensor;



    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_score = findViewById(R.id.tv_score);

        ImageView plat1 = findViewById(R.id.plat1);
        ImageView plat2 = findViewById(R.id.plat2);
        ImageView plat3 = findViewById(R.id.plat3);
        ImageView plat4 = findViewById(R.id.plat4);

        plat4.setImageResource(R.drawable.burger);
        plats = new ArrayList<>();
        initActions();
        initPlats();

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

    public void initPlats() {
        plats.add(new Plat("Burger", actionsBurger, R.drawable.burger));
        plats.add(new Plat("Salade", actionsSalade, R.drawable.salade));
        plats.add(new Plat("Pizza", actionsPizza, R.drawable.pizza));
        plats.add(new Plat("HotDog", actionsHotdog, R.drawable.hotdog));
        plats.add(new Plat("Frites", actionsFrites, R.drawable.frites));
        plats.add(new Plat("Sushi", actionsSushi, R.drawable.sushi));
        plats.add(new Plat("Tacos", actionsTacos, R.drawable.tacos));
        plats.add(new Plat("Donut", actionsDonut, R.drawable.donut));
    }

    public void initActions() {
        Action masquerLuminosite = new Action("Masquer luminosité");
        Action touchScreen = new Action ("Toucher l'écran");
        Action bougerTablette = new Action ("Bouger la tablette");
        Action parler = new Action ("Parler");

        actionsBurger = new ArrayList<>();
        actionsBurger.add(masquerLuminosite);
        actionsBurger.add(touchScreen);

        actionsDonut = new ArrayList<>();
        actionsDonut.add(parler);
        actionsDonut.add(masquerLuminosite);

        actionsFrites = new ArrayList<>();
        actionsFrites.add(bougerTablette);
        actionsFrites.add(touchScreen);
        actionsFrites.add(bougerTablette);

        actionsHotdog = new ArrayList<>();
        actionsHotdog.add(touchScreen);
        actionsHotdog.add(parler);

        actionsPizza = new ArrayList<>();
        actionsPizza.add(bougerTablette);
        actionsPizza.add(touchScreen);

        actionsSalade = new ArrayList<>();
        actionsSalade.add(bougerTablette);
        actionsSalade.add(touchScreen);

        actionsSushi = new ArrayList<>();
        actionsSushi.add(bougerTablette);
        actionsSushi.add(touchScreen);

        actionsTacos = new ArrayList<>();
        actionsTacos.add(bougerTablette);
        actionsTacos.add(touchScreen);
    }

    // -- Sound level detector
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
    // --

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

    public boolean validerPlat(Plat plat){
        boolean platTermine = true;
        for (Action action: plat.getActions()){
            if (!action.isValide()){
                platTermine = false;
            }
        }

        this.score += 500;
        tv_score.setText("Score : " + String.valueOf(this.score));

        return platTermine;
    }


}
