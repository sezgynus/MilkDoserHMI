package com.menar.milkdoserhmi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Timer;
import java.util.TimerTask;

public class power_menu extends AppCompatActivity {

    private void startKioskService() {
        startService(new Intent(this, KioskService.class));
    }

    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
    );
    boolean power_pressed=false;
    int power_hold_counter=0;
    Timer timer;
    ImageButton power,wash,rinse,next;
    @SuppressLint("ClickableViewAccessibility")
    public void initviews() {
        power = findViewById(R.id.power_button);
        wash = findViewById(R.id.wash_button);
        rinse = findViewById(R.id.rinse_button);
        next = findViewById(R.id.next_screen);
        power.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
                power.setImageResource(R.drawable.power_button_pressed);
                power_pressed=true;
                power_hold_counter=0;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                power.setImageResource(R.drawable.power_button);
                power_pressed=false;
                power_hold_counter=0;
            }
            return true;
        });
        wash.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
                wash.setImageResource(R.drawable.wash_button_pressed);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                wash.setImageResource(R.drawable.wash_button);
                Intent i = new Intent(getApplicationContext(), dosing.class);
                i.putExtra("wash_button",true);
                startActivity(i);
            }
            return true;
        });
        rinse.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
                rinse.setImageResource(R.drawable.wash_button_pressed);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                rinse.setImageResource(R.drawable.wash_button);
                Intent i = new Intent(getApplicationContext(), dosing.class);
                i.putExtra("rinse_button",true);
                startActivity(i);
            }
            return true;
        });
        next.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 8, 0, 8);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                Intent i = new Intent(getApplicationContext(), dosing.class);
                startActivity(i);
            }
            return true;
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startKioskService();
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
        setContentView(R.layout.activity_power_menu);
        timer = new Timer();
        timer.schedule(timerTask,0,100);
        initviews();
    }
    final TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if(power_pressed)
            {
                power_hold_counter++;
                if(power_hold_counter>=4)
                {
                    power_pressed=false;
                    power_hold_counter=0;
                    Intent i = new Intent(getApplicationContext(), poweron_screen.class);
                    startActivity(i);
                    Settings.System.putInt(getApplicationContext().getContentResolver(),
                            Settings.System.SCREEN_BRIGHTNESS, 0);
                }
            }
        }
    };
}