package com.menar.milkdoser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class machine_info extends AppCompatActivity {

    TextView total_a_count,total_b_count,total_a_volume,total_b_volume,total_count;
    int TOTAL_A_COUNT,TOTAL_B_COUNT,TOTAL_A_VOLUME,TOTAL_B_VOLUME;
    ImageButton rst_mac_info;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
    );
    @SuppressLint("ClickableViewAccessibility")
    public void initviews() {
        total_a_count = findViewById(R.id.total_a_count);
        total_b_count = findViewById(R.id.total_b_count);
        total_a_volume = findViewById(R.id.total_a_volume);
        total_b_volume = findViewById(R.id.total_b_volume);
        total_count = findViewById(R.id.total_count);


        rst_mac_info= findViewById(R.id.reset_machine_info);
        rst_mac_info.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                editor.putInt("error_event_cnt",0);
                editor.commit();
                Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.makine_bilgileri_sifirlandi),Toast.LENGTH_LONG).show();
                save_default_params();
                init_params();
                load_params();
            }
            return true;
        });
        load_params();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        super.onCreate(savedInstanceState);
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
            decorView
                    .setOnSystemUiVisibilityChangeListener(visibility -> {
                        if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                        {
                            decorView.setSystemUiVisibility(flags);
                        }
                    });
        }
        setContentView(R.layout.activity_machine_info);
        init_params();
        initviews();
    }
    public void init_params()
    {
        TOTAL_A_COUNT=sharedPref.getInt("a_count_val",0);
        TOTAL_B_COUNT=sharedPref.getInt("b_count_val",0);
        TOTAL_A_VOLUME=sharedPref.getInt("a_volume_val",0);
        TOTAL_B_VOLUME=sharedPref.getInt("b_volume_val",0);
    }
    public void save_default_params()
    {
        editor.putInt("a_count_val",0);
        editor.putInt("b_count_val",0);
        editor.putInt("a_volume_val",0);
        editor.putInt("b_volume_val",0);
        editor.commit();
    }
    @SuppressLint("SetTextI18n")
    public void load_params()
    {
        total_a_count.setText(Integer.toString(TOTAL_A_COUNT));
        total_b_count.setText(Integer.toString(TOTAL_B_COUNT));
        total_a_volume.setText(Integer.toString(TOTAL_A_VOLUME));
        total_b_volume.setText(Integer.toString(TOTAL_B_VOLUME));
        total_count.setText(Integer.toString(TOTAL_A_COUNT+TOTAL_B_COUNT));
    }
    public void back_click(View view) {
        Intent i = new Intent(getApplicationContext(),settings.class);
        startActivity(i);
    }

    public void home_click(View view) {
        Intent i = new Intent(getApplicationContext(),dosing.class);
        startActivity(i);
    }
}