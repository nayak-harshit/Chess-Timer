package com.example.chesstimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView p1TextBox, p2TextBox, p1MoveBox, p2MoveBox;
    ImageButton setting, pause, reset;

    // tick length in milliseconds
    private static final int TICK_LENGTH = 100;

    private CountDownTimer p1CountDownTimer;
    private CountDownTimer p2CountDownTimer;

    private static long startTimeInMillis;
    private static long p1StartTimeInMillis;
    private static long p2StartTimeInMillis;
    private static long p1TimeLeftInMillis;
    private static long p2TimeLeftInMillis;

    private static boolean sameStartTime;
    private static boolean SoundOn;
    private static boolean vibrateOn;
    private static boolean finishSoundOn;

    private static boolean p1TimerRunning;
    private static boolean p2TimerRunning;

    private int numberOfMovesP1;
    private int numberOfMovesP2;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        p1TextBox = findViewById(R.id.textView);
        p2TextBox = findViewById(R.id.textView3);
        p1MoveBox = findViewById(R.id.textView2);
        p2MoveBox = findViewById(R.id.textView4);
//        setting = findViewById(R.id.imageButton);
//        pause = findViewById(R.id.imageButton2);
//        reset = findViewById(R.id.imageButton3);


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        startTimeInMillis = sharedPref.getLong(SettingsActivity.KEY_START_TIME_IN_MILLIS, 600000);
        sameStartTime = sharedPref.getBoolean(SettingsActivity.KEY_SAME_START_TIME, true);
        if (sameStartTime){
            p1StartTimeInMillis = startTimeInMillis;
            p2StartTimeInMillis = startTimeInMillis;
        }else{
            p1StartTimeInMillis = sharedPref.getLong(SettingsActivity.KEY_P1_START_TIME, 600000);
            p2StartTimeInMillis = sharedPref.getLong(SettingsActivity.KEY_P2_START_TIME, 600000);
        }

        numberOfMovesP1 = 0;
        numberOfMovesP2 = 0;

        p1TimerRunning = false;
        p2TimerRunning = false;

    }

    public void settingOnClick(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


    /*@TODO */
    public void restoreDefaults(){}




}