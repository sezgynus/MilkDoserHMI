package com.menar.milkdoser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Timer;
import java.util.TimerTask;

public class poweron_screen extends AppCompatActivity {

    ImageButton poweron_btn,sleep_screen;
    TextView welcome_view,reception_view,reception_view2;
    CircleProgressBar circleProgressBar;
    boolean poweron_pressed=false,reception_show=false,wakeup=false,unlock=false;
    Timer hold_timer,reception_timer,resleep_timer;
    int hold_counter=0,reception_counter=0,resleep_counter=0;

    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
    );

    @SuppressLint("ClickableViewAccessibility")
    public void initviews() {
        poweron_btn = findViewById(R.id.poweron_button);
        welcome_view = findViewById(R.id.welcome);
        reception_view = findViewById(R.id.reception_text);
        reception_view2 = findViewById(R.id.reception_text2);
        sleep_screen = findViewById(R.id.sleep_screen);
        circleProgressBar = findViewById(R.id.custom_progressBar);
        circleProgressBar.setColor(Color.WHITE);
        circleProgressBar.setMax(12);
        circleProgressBar.setMin(0);
        sleep_screen.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Settings.System.putInt(getApplicationContext().getContentResolver(),Settings.System.SCREEN_BRIGHTNESS, 255);
                sleep_screen.setVisibility(View.INVISIBLE);
                wakeup=true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
            }
            return true;
        });
        poweron_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                poweron_pressed=true;
                view.setLayoutParams(params);
                resleep_counter=0;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                poweron_pressed=false;
                resleep_counter=0;
                circleProgressBar.setProgressWithAnimation(0);
            }
            return true;
        });
        reception_view.setVisibility(View.INVISIBLE);
        reception_view2.setVisibility(View.INVISIBLE);
        poweron_btn.setVisibility(View.VISIBLE);
        welcome_view.setVisibility(View.VISIBLE);
        sleep_screen.setVisibility(View.VISIBLE);
        circleProgressBar.setVisibility(View.VISIBLE);
        Settings.System.putInt(getApplicationContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //startKioskService();
        int currentApiVersion = Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {

            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
                if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                {
                    decorView.setSystemUiVisibility(flags);
                }
            });
        }
        setContentView(R.layout.activity_poweron_screen);
        initviews();
        hold_timer=new Timer();
        reception_timer=new Timer();
        resleep_timer=new Timer();
        hold_timer.schedule(hold_timer_task,0,100);
        reception_timer.schedule(reception_timer_task,0,100);
        resleep_timer.schedule(resleep_timer_task,0,100);
    }
    final TimerTask resleep_timer_task = new TimerTask() {
        @Override
        public void run() {
            if(wakeup) {
                resleep_counter++;
                if (resleep_counter >= 30)
                {
                    wakeup=false;
                    runOnUiThread(() -> {
                        circleProgressBar.setColor(Color.WHITE);
                        reception_view.setVisibility(View.INVISIBLE);
                        reception_view2.setVisibility(View.INVISIBLE);
                        poweron_btn.setVisibility(View.VISIBLE);
                        welcome_view.setVisibility(View.VISIBLE);
                        sleep_screen.setVisibility(View.VISIBLE);
                        if(!unlock) {
                            Settings.System.putInt(getApplicationContext().getContentResolver(),
                                    Settings.System.SCREEN_BRIGHTNESS, 0);
                        }
                    });
                }
            }
            else {
                resleep_counter=0;
            }
        }
    };
    final TimerTask hold_timer_task = new TimerTask() {
        @Override
        public void run() {
            if(poweron_pressed) {
                hold_counter++;
                runOnUiThread(() -> circleProgressBar.setProgressWithAnimation(hold_counter));
                if (hold_counter >= 12)
                {
                    runOnUiThread(() -> {
                        circleProgressBar.setColor(Color.rgb(2,157,235));
                        poweron_btn.setVisibility(View.INVISIBLE);
                        welcome_view.setVisibility(View.INVISIBLE);
                        reception_view.setVisibility(View.VISIBLE);
                        reception_view2.setVisibility(View.VISIBLE);
                        circleProgressBar.setVisibility(View.INVISIBLE);
                        reception_show=true;
                        poweron_pressed=false;
                        unlock=true;
                    });
                }
            }
            else {
                hold_counter=0;
            }
        }
    };
    final TimerTask reception_timer_task = new TimerTask() {
        @Override
        public void run() {
            if(reception_show) {
                reception_counter++;
                runOnUiThread(() -> circleProgressBar.setProgress(12));
                if (reception_counter >= 30)
                {
                    runOnUiThread(() -> {
                        Intent i = new Intent(getApplicationContext(),power_menu.class);
                        reception_show=false;
                        startActivity(i);
                    });
                }
            }
            else {
                reception_counter=0;
            }
        }
    };
}