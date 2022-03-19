package com.example.chesstimer;

import static java.lang.String.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "tag";
    TextView p1TextBox, p2TextBox, p1MoveBox, p2MoveBox;
    ImageButton setting, pause, reset;

    // tick length in milliseconds
    private static final int TICK_LENGTH = 100;

    private CountDownTimer p1CountDownTimer;
    private CountDownTimer p2CountDownTimer;
//    private AudioManager audioManager;

    private static long startTimeInMillis;
    private static long p1StartTimeInMillis;
    private static long p2StartTimeInMillis;
    private static long p1TimeLeftInMillis;
    private static long p2TimeLeftInMillis;

    private static boolean startWithDifferentTime;
    private static boolean isSoundOn;
    private static boolean isVibrateOn;
    private static boolean finishSoundOn;

    private int numberOfMovesP1;
    private int numberOfMovesP2;
    private int onTheClock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        p1TextBox = findViewById(R.id.textView);
        p2TextBox = findViewById(R.id.textView3);
        p1MoveBox = findViewById(R.id.textView2);
        p2MoveBox = findViewById(R.id.textView4);
        pause = findViewById(R.id.imageButton2);

//        setting = findViewById(R.id.imageButton);
//        reset = findViewById(R.id.imageButton3);


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        startTimeInMillis = Long.parseLong(sharedPref.getString(SettingsActivity.KEY_START_TIME_IN_MILLIS, "600000"));
        startWithDifferentTime = sharedPref.getBoolean(SettingsActivity.KEY_SAME_START_TIME, true);


        if (startWithDifferentTime) {
            p1StartTimeInMillis = Long.parseLong(sharedPref.getString(SettingsActivity.KEY_P1_START_TIME, "600000"));
            ;
            p2StartTimeInMillis = Long.parseLong(sharedPref.getString(SettingsActivity.KEY_P2_START_TIME, "600000"));
        } else {
            p1StartTimeInMillis = startTimeInMillis;
            p2StartTimeInMillis = startTimeInMillis;
        }

        p1TimeLeftInMillis = p1StartTimeInMillis;
        p2TimeLeftInMillis = p2StartTimeInMillis;

        isSoundOn = sharedPref.getBoolean(SettingsActivity.KEY_SOUND_ON, true);
        isVibrateOn = sharedPref.getBoolean(SettingsActivity.KEY_VIBRATE_ON, false);
        finishSoundOn = sharedPref.getBoolean(SettingsActivity.KEY_MATCH_FINISH_SOUND, true);

        numberOfMovesP1 = 0;
        numberOfMovesP2 = 0;
        onTheClock = 0;

//        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        Log.d(TAG, "onCreate:");
        Log.d("TAG", "onCreate: startTimeInMillis " + startTimeInMillis);
        Log.d(TAG, "onCreate: startWithDifferentTime " + startWithDifferentTime);
        Log.d(TAG, "onCreate: p1StartTimeInMillis " + p1StartTimeInMillis);
        Log.d(TAG, "onCreate: p2StartTimeInMillis " + p2StartTimeInMillis);
        Log.d(TAG, "onCreate: ontheClock"+ onTheClock);

        updateCountDownText();

        //testing-
//        Log.d(TAG, "onCreate: starting both the timers");
//        p1StartTimer();
//        p2StartTimer();
    }


    //On-Click methods:
    public void player1OnClick(View view) {
        Log.d(TAG, "player1OnClick: OnTheClock" + onTheClock);
//        if(numberOfMovesP1 == 0 && numberOfMovesP2 == 0 && onTheClock == 0){
//            p1PauseTimer();
//            p2StartTimer();
//            Log.d(TAG, "player1OnClick: first Move OnTheClock " + onTheClock);
//            return;
//        }

        if(numberOfMovesP1 == 0 && numberOfMovesP2 == 0 && onTheClock == 0){
            Log.d(TAG, "player1OnClick: First Move");
        }

        if (onTheClock == 2) return;

       p1PauseTimer();
       p2StartTimer();

//        Log.d(TAG, "player1OnClick: playing Sound");
//

        pause.setVisibility(View.VISIBLE);

        Log.d(TAG, "player1OnClick: OnTheClock " + onTheClock);

    }

    public void player2OnClick(View view) {
        Log.d(TAG, "player2OnClick: onTheClock " + onTheClock);

//        if(numberOfMovesP1 == 0 && numberOfMovesP2 == 0 && onTheClock == 0){
//            p2PauseTimer();
//            p1StartTimer();
//            Log.d(TAG, "player2OnClick: first Move OnTheClock " + onTheClock);
//            return;
//        }
        if(numberOfMovesP1 == 0 && numberOfMovesP2 == 0 && onTheClock == 0){
            Log.d(TAG, "player2OnClick: First Move");
        }

        if (onTheClock == 1) return;

        p2PauseTimer();
        p1StartTimer();

        pause.setVisibility(View.VISIBLE);


        Log.d(TAG, "player2OnClick: OnTheClock " + onTheClock);

    }

    public void settingOnClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void pauseOnClick(View view){
        try{
            p1CountDownTimer.cancel();
            p2CountDownTimer.cancel();
        }catch (Exception e){
            Log.d(TAG, "pauseOnClick: " +e);
        }
        pause.setVisibility(View.INVISIBLE);
    }

    public void resetOnClick(View view){
       resetBothTimer();
    }

    public void onMatchFinish(){

    }


    //Timer methods
    public void p1StartTimer() {
        Log.d(TAG, "p1StartTimer: ");
        Log.d(TAG, "p1StartTimer: p1TimeLeftInMillis "+p1TimeLeftInMillis);
        Log.d(TAG, "p1StartTimer: p2TimeLeftInMillis "+ p2TimeLeftInMillis);


        p1CountDownTimer = new CountDownTimer(p1TimeLeftInMillis, TICK_LENGTH) {
            @Override
            public void onTick(long millisUntilFinished) {
                p1TimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
//                Log.d(TAG, "onTick: p1");
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "onFinish: finished p1 countdown");
//                p1TextBox.setText("00:00:00");
                p1TimeLeftInMillis = 0;
                Log.d(TAG, "onFinish: p1TimeLeftInMillis "+p1TimeLeftInMillis);

            }
        }.start();

        onTheClock = 1;
        numberOfMovesP1++;
        p1TextBox.setBackground(this.getDrawable(R.drawable.running_clock_background));
        p2TextBox.setBackground(this.getDrawable(R.drawable.not_running_clock_background));
    }

    public void p1PauseTimer() {
        try {
            p1CountDownTimer.cancel();
        } catch (Exception e) {
            Log.d(TAG, "p1PauseTimer: " + e);
        }

        onTheClock = 0;
//        p1TextBox.setBackground(this.getDrawable(R.drawable.running_clock_background));

    }

    public void p2StartTimer() {

        Log.d(TAG, "p2StartTimer: ");
        Log.d(TAG, "p2StartTimer: p2TimeLeftInMillis "+p2TimeLeftInMillis);
        Log.d(TAG, "p2StartTimer: p1TimeLeftInMillis"+p1TimeLeftInMillis);

        p2CountDownTimer = new CountDownTimer(p2TimeLeftInMillis, TICK_LENGTH) {
            @Override
            public void onTick(long millisUntilFinished) {
                p2TimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
//                Log.d(TAG, "onTick: p2");
            }

            @Override
            public void onFinish() {
                //                p2TextBox.setText("00:00:00");
                p2TimeLeftInMillis = 0;
                Log.d(TAG, "onFinish: finished p2 countdown");
                Log.d(TAG, "onFinish: p2TimeLeftInMillis "+p2TimeLeftInMillis);

            }
        }.start();

        onTheClock = 2;
        numberOfMovesP2++;
        p2TextBox.setBackground(this.getDrawable(R.drawable.running_clock_background));
        p1TextBox.setBackground(this.getDrawable(R.drawable.not_running_clock_background));

    }

    public void p2PauseTimer() {
        try {
            p2CountDownTimer.cancel();

        } catch (Exception e) {
            Log.d(TAG, "p2PauseTimer: " + e);
        }

        onTheClock = 0;
//        p2TextBox.setBackground(this.getDrawable(R.drawable.running_clock_background));
    }

    public void resetBothTimer() {
        p1TextBox.setBackground(this.getDrawable(R.drawable.running_clock_background));
        p2TextBox.setBackground(this.getDrawable(R.drawable.running_clock_background));

        try {
            p1PauseTimer();
            p2PauseTimer();
        }catch (Exception e){
            Log.d(TAG, "resetOnClick: "+e);
        }
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        startTimeInMillis = Long.parseLong(sharedPref.getString(SettingsActivity.KEY_START_TIME_IN_MILLIS, "600000"));
        startWithDifferentTime = sharedPref.getBoolean(SettingsActivity.KEY_SAME_START_TIME, true);


        if (startWithDifferentTime) {
            p1StartTimeInMillis = Long.parseLong(sharedPref.getString(SettingsActivity.KEY_P1_START_TIME, "600000"));
            ;
            p2StartTimeInMillis = Long.parseLong(sharedPref.getString(SettingsActivity.KEY_P2_START_TIME, "600000"));
        } else {
            p1StartTimeInMillis = startTimeInMillis;
            p2StartTimeInMillis = startTimeInMillis;
        }

        p1TimeLeftInMillis = p1StartTimeInMillis;
        p2TimeLeftInMillis = p2StartTimeInMillis;

        numberOfMovesP1 = 0;
        numberOfMovesP2 = 0;
        onTheClock = 0;
        updateCountDownText();

    }

    public void updateCountDownText() {
        int p1Minutes = (int) (p1TimeLeftInMillis / 1000) / 60;
        int p1Seconds = (int) (p1TimeLeftInMillis / 1000) % 60;
//        int p1MilliSeconds = (int) (p1TimeLeftInMillis % TICK_LENGTH);

        int p2Minutes = (int) (p2TimeLeftInMillis / 1000) / 60;
        int p2Seconds = (int) (p2TimeLeftInMillis / 1000) % 60;
//        int p2MilliSeconds = (int) (p2TimeLeftInMillis % TICK_LENGTH);

        String p1TimeFormatted = format("%02d:%02d", p1Minutes, p1Seconds);
        String p2TimeFormatted = format("%02d:%02d", p2Minutes, p2Seconds);

        p1TextBox.setText(p1TimeFormatted);
        p2TextBox.setText(p2TimeFormatted);

        p1MoveBox.setText(format("Moves: %02d", numberOfMovesP1));
        p2MoveBox.setText(format("Moves: %02d", numberOfMovesP2));


    }


    public void tickSound(Boolean isSoundOn){

    }

    public void vibrateOn(Boolean isVibrateOn){

    }

    public void finalSound(Boolean finishSoundOn){

    }

    /*@TODO */
    public void restoreDefaults() {

    }


}