package com.example.chesstimer;

import static java.lang.String.*;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "tag";
    TextView p1TextBox, p2TextBox, p1MoveBox, p2MoveBox, p1timeLeft, p1move, p1timePerMove, p2timeLeft, p2move, p2timePerMove;
    ImageButton  pause;


    // tick length in milliseconds
    private static final int TICK_LENGTH = 100;

    private CountDownTimer p1CountDownTimer;
    private CountDownTimer p2CountDownTimer;

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

    private static String p1TimeFormatted;
    private static String p2TimeFormatted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN ;
        decorView.setSystemUiVisibility(uiOptions);

        p1TextBox = findViewById(R.id.textView);
        p2TextBox = findViewById(R.id.textView3);
        p1MoveBox = findViewById(R.id.textView2);
        p2MoveBox = findViewById(R.id.textView4);
        pause = findViewById(R.id.imageButton2);


        resetBothTimer();

        Log.d(TAG, "onCreate:");
        Log.d("TAG", "onCreate: startTimeInMillis " + startTimeInMillis);
        Log.d(TAG, "onCreate: startWithDifferentTime " + startWithDifferentTime);
        Log.d(TAG, "onCreate: p1StartTimeInMillis " + p1StartTimeInMillis);
        Log.d(TAG, "onCreate: p2StartTimeInMillis " + p2StartTimeInMillis);
        Log.d(TAG, "onCreate: onTheClock " + onTheClock);



    }


    //On-Click methods:
    public void player1OnClick(View view) {
        Log.d(TAG, "player1OnClick: OnTheClock" + onTheClock);

        if (numberOfMovesP1 == 0 && numberOfMovesP2 == 0 && onTheClock == 0) {
            Log.d(TAG, "player1OnClick: First Move");
        }

        if (onTheClock == 2) return;

        p1PauseTimer();
        p2StartTimer();

        tickSound(isSoundOn);
        vibrateOn(isVibrateOn);

        pause.setVisibility(View.VISIBLE);

        Log.d(TAG, "player1OnClick: OnTheClock " + onTheClock);

    }

    public void player2OnClick(View view) {
        Log.d(TAG, "player2OnClick: onTheClock " + onTheClock);

        if (numberOfMovesP1 == 0 && numberOfMovesP2 == 0 && onTheClock == 0) {
            Log.d(TAG, "player2OnClick: First Move");
        }

        if (onTheClock == 1) return;

        p2PauseTimer();
        p1StartTimer();

        pause.setVisibility(View.VISIBLE);

        tickSound(isSoundOn);
        vibrateOn(isVibrateOn);

        Log.d(TAG, "player2OnClick: OnTheClock " + onTheClock);

    }

    public void settingOnClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void pauseOnClick(View view) {
        try {
            p1CountDownTimer.cancel();
            p2CountDownTimer.cancel();
        } catch (Exception e) {
            Log.d(TAG, "pauseOnClick: " + e);
        }
        pause.setVisibility(View.INVISIBLE);
    }

    public void resetOnClick(View view) {
        resetBothTimer();
    }



    public void showCustomDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.popup_window);


        p1timeLeft = (TextView) dialog.findViewById(R.id.p1TimeLeft);
        p1move = (TextView) dialog.findViewById(R.id.p1MoveLeft);
        p1timePerMove = (TextView) dialog.findViewById(R.id.p1TimePerMove);
        p2timeLeft = (TextView) dialog.findViewById(R.id.p2TimeLeft);
        p2move = (TextView) dialog.findViewById(R.id.p2MoveLeft);
        p2timePerMove = (TextView) dialog.findViewById(R.id.p2TimePerMove);

        p1timeLeft.setText(p1TimeFormatted);
        p2timeLeft.setText(p2TimeFormatted);

        p1move.setText(format(" %02d", numberOfMovesP1));
        p2move.setText(format(" %02d", numberOfMovesP2));


        try {

        // time taken per move
        long p1TimePerMoveInMillis = p1StartTimeInMillis / numberOfMovesP1;
        long p2TimePerMoveInMillis = p2StartTimeInMillis / numberOfMovesP2;

        int p1MinutesPerMove = (int) (p1TimePerMoveInMillis / 1000) / 60;
        int p1SecondsPerMove = (int) (p1TimePerMoveInMillis / 1000) % 60;

        int p2MinutesPerMove = (int) (p2TimePerMoveInMillis / 1000) / 60;
        int p2SecondsPerMove = (int) (p2TimePerMoveInMillis / 1000) % 60;

        p1timePerMove.setText(format("%02d:%02d", p1MinutesPerMove, p1SecondsPerMove));
        p2timePerMove.setText(format("%02d:%02d", p2MinutesPerMove, p2SecondsPerMove));

        }catch (Exception e){
            Log.d(TAG, "showCustomDialog: "+ e);
        }
        Button resetButton = dialog.findViewById(R.id.button);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    //Timer methods
    public void p1StartTimer() {
        Log.d(TAG, "p1StartTimer: ");
        Log.d(TAG, "p1StartTimer: p1TimeLeftInMillis " + p1TimeLeftInMillis);
        Log.d(TAG, "p1StartTimer: p2TimeLeftInMillis " + p2TimeLeftInMillis);


        p1CountDownTimer = new CountDownTimer(p1TimeLeftInMillis, TICK_LENGTH) {
            @Override
            public void onTick(long millisUntilFinished) {
                p1TimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "onFinish: finished p1 countdown");
                p1TimeLeftInMillis = 0;
                finalSound(finishSoundOn);
                showCustomDialog();
                resetBothTimer();

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

    }

    public void p2StartTimer() {

        Log.d(TAG, "p2StartTimer: ");
        Log.d(TAG, "p2StartTimer: p2TimeLeftInMillis " + p2TimeLeftInMillis);
        Log.d(TAG, "p2StartTimer: p1TimeLeftInMillis" + p1TimeLeftInMillis);

        p2CountDownTimer = new CountDownTimer(p2TimeLeftInMillis, TICK_LENGTH) {
            @Override
            public void onTick(long millisUntilFinished) {
                p2TimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                p2TimeLeftInMillis = 0;
                Log.d(TAG, "onFinish: finished p2 countdown");
                Log.d(TAG, "onFinish: p2TimeLeftInMillis " + p2TimeLeftInMillis);
                finalSound(finishSoundOn);
                showCustomDialog();
                resetBothTimer();
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
        } catch (Exception e) {
            Log.d(TAG, "resetOnClick: " + e);
        }
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        startTimeInMillis = 1000 * Long.parseLong(sharedPref.getString(SettingsActivity.KEY_START_TIME_IN_SECONDS, "60"));
        startWithDifferentTime = sharedPref.getBoolean(SettingsActivity.KEY_SAME_START_TIME, true);


        if (startWithDifferentTime) {
            p1StartTimeInMillis = 1000 * Long.parseLong(sharedPref.getString(SettingsActivity.KEY_P1_START_TIME, "60"));
            p2StartTimeInMillis = 1000 * Long.parseLong(sharedPref.getString(SettingsActivity.KEY_P2_START_TIME, "60"));
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

        updateCountDownText();

    }


    public void updateCountDownText() {
        int p1Minutes = (int) (p1TimeLeftInMillis / 1000) / 60;
        int p1Seconds = (int) (p1TimeLeftInMillis / 1000) % 60;

        int p2Minutes = (int) (p2TimeLeftInMillis / 1000) / 60;
        int p2Seconds = (int) (p2TimeLeftInMillis / 1000) % 60;

        p1TimeFormatted = format("%02d:%02d", p1Minutes, p1Seconds);
        p2TimeFormatted = format("%02d:%02d", p2Minutes, p2Seconds);

        p1TextBox.setText(p1TimeFormatted);
        p2TextBox.setText(p2TimeFormatted);

        p1MoveBox.setText(format("Moves: %02d", numberOfMovesP1));
        p2MoveBox.setText(format("Moves: %02d", numberOfMovesP2));


    }



    public void tickSound(Boolean isSoundOn) {
        if (!isSoundOn) return;

        MediaPlayer mediaPlayer = MediaPlayer.create(this.getBaseContext(), R.raw.tick_sound);
        mediaPlayer.start();
    }

    public void vibrateOn(Boolean isVibrateOn) {
        if (!isVibrateOn) return;

        Vibrator vibrator;
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        try {
            vibrator.cancel();
        } catch (Exception e) {
            Log.d(TAG, "vibrateOn: " + e);
        }
        vibrator.vibrate(100);
    }

    public void finalSound(Boolean finishSoundOn) {
        if (!finishSoundOn) return;
        MediaPlayer mp = MediaPlayer.create(this, R.raw.time_up_sound);
        mp.start();
    }

    /*@TODO */
    public void restoreDefaults() {

    }


}