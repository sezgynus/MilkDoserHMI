package com.menar.milkdoserhmi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class info extends AppCompatActivity {
    private void startKioskService() {
        startService(new Intent(this, KioskService.class));
    }
    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
    );
    Context ctx;
    ImageButton previous_btn;
    TextView date,time,counters,volumes,device_id,model;
    Timer time_tmr;
    int TOTAL_A_COUNT,TOTAL_B_COUNT,TOTAL_A_VOLUME,TOTAL_B_VOLUME;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    @SuppressLint("SetTextI18n")
    public void init_params()
    {
        TOTAL_A_COUNT=sharedPref.getInt("a_count_val",0);
        TOTAL_B_COUNT=sharedPref.getInt("b_count_val",0);
        TOTAL_A_VOLUME=sharedPref.getInt("a_volume_val",0);
        TOTAL_B_VOLUME=sharedPref.getInt("b_volume_val",0);
        int TOTAL_COUNT=TOTAL_B_COUNT+TOTAL_A_COUNT;
        int TOTAL_VOLUME=TOTAL_A_VOLUME+TOTAL_B_VOLUME;
        counters.setText(TOTAL_COUNT+" Adet");
        volumes.setText(TOTAL_VOLUME+" mL");
    }
    @SuppressLint({"HardwareIds", "ClickableViewAccessibility"})
    public void initviews() {
        previous_btn = findViewById(R.id.previous_page);
        date = findViewById(R.id.date_view);
        time = findViewById(R.id.time_view);
        counters  = findViewById(R.id.counter_view);
        volumes = findViewById(R.id.volume_view);
        device_id = findViewById(R.id.textView28);
        model = findViewById(R.id.textView5);
        device_id.setText(Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID));
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        model.setText("MILKMASTERPIECE FW: v"+info.versionName);
        previous_btn.setOnTouchListener((view, event) -> {
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
        sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        super.onCreate(null);
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
        setContentView(R.layout.activity_info);
        initviews();
        init_params();

        time_tmr = new Timer();
        time_tmr.schedule(time_timer_task,0,1000);
        ctx=this;
        //preventStatusBarExpansion(ctx);
    }
    final TimerTask time_timer_task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(() -> {
                Calendar c = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat tf = new SimpleDateFormat("HH.mm");
                String formattedDate = df.format(c.getTime());
                String formattedTime = tf.format(c.getTime());
                date.setText(formattedDate);
                time.setText(formattedTime);
            });
        }
    };
}