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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    // ------ Plats -------
    private List<Plat> plats;
    private List<Action> actionsBurger, actionsSalade, actionsPizza,
                    actionsHotdog, actionsFrites, actionsSushi,
                    actionsTacos, actionsDonut;

    private int nb_plat = 10;
    private List<Plat> platsGame;
    private int iPlatGame = 0;

    private List<Plat> platsActive;
    private int nb_plat_active = 4;
    //5 sec
    private int timerPlatDuration = 5000;
    private Timer timerPlat = new Timer();

    private int score = 0;
    private TextView tv_score;

    // ------ Noise detector -------

    private static final int POLL_INTERVAL = 300;
    private boolean mRunning = false;
    int RECORD_AUDIO = 0;
    private PowerManager.WakeLock mWakeLock;
    private Handler mHandler = new Handler();
    private NoiseDetector mSensor;

    private float currentLight, posY, currentAccelerationY;
    private double amp;


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

        plats = new ArrayList<>();
        platsGame = new ArrayList<>();
        platsActive = new ArrayList<>();
        initActions();
        initPlats();
        initPlatsGame(nb_plat);
        initActivePlat();
        initTimer();
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

    //initialise les plats pour la partie
    public void initPlatsGame(int nb) {
        int nb_plat_dispo = plats.size();

        for(int k=0;k<nb;k++){
            int iPlat = new Random().nextInt(nb_plat_dispo);
            if(iPlat<0){
                iPlat=0;
            }
            Plat platAdded = plats.get(iPlat);
            platsGame.add(platAdded);
        }
    }

    //retourne le plat en cours
    public Plat getCurrentPlat(){
        if(platsGame.size() >= iPlatGame){
            return platsGame.get(iPlatGame);
        }
        return null;
    }

    //initialise les plats actifs
    public void initActivePlat(){
        for(int k=0;k<nb_plat_active;k++){
            platsActive.add(platsGame.get(k));
        }
    }

    //retourne les plats actifs
    public void getActivePlat(){
        for(int k=0;k<nb_plat_active;k++){
            platsActive.add(platsGame.get(iPlatGame+k));
        }
    }

    //initialise le timer
    public void initTimer(){
        System.out.println("Init timer");
        timerPlat.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Your database code here
                System.out.println("Update active list");
                getActivePlat();
            }
        }, timerPlatDuration,timerPlatDuration);
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
            amp = mSensor.getAmplitude();
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
                currentLight = event.values[0];
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
                currentAccelerationY = event.values[1];
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
            posY = event.getY();
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


    public void validerAction(Action actionAValider) {
        if (actionAValider.getNom().equals("Masquer luminosité")) {
            actionAValider.setValide(currentLight < 1f);
        } else if (actionAValider.getNom().equals("Toucher l'écran")) {
            actionAValider.setValide(posY > 0f);
            posY = 0;
        } else if (actionAValider.getNom().equals("Bouger la tablette")) {
            actionAValider.setValide(currentAccelerationY > 10f);
        } else {
            actionAValider.setValide(amp > 10L);
        }
    }
}
