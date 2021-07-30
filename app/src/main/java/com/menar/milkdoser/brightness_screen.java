package com.menar.milkdoser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class brightness_screen extends AppCompatActivity {

    private int currentApiVersion;
    SeekBar br;
    TextView bri_per;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentApiVersion = android.os.Build.VERSION.SDK_INT;

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
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                    {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility)
                        {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                            {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }
        setContentView(R.layout.brightness_screen);
        br=(SeekBar)findViewById(R.id.seekBar);
        bri_per=(TextView)findViewById(R.id.percentage);
        br.setProgress(Settings.System.getInt(getApplicationContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0));
        bri_per.setText("%"+String.valueOf(map(Settings.System.getInt(getApplicationContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0),0,255,0,100)));
        br.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub
                Settings.System.putInt(getApplicationContext().getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS, progress);
                bri_per.setText("%"+String.valueOf(map(Settings.System.getInt(getApplicationContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0),0,255,0,100)));
            }
        });
    }

    public void back_click(View view) {
        Intent i = new Intent(getApplicationContext(),settings.class);
        startActivity(i);
    }

    public void home_click(View view) {
        Intent i = new Intent(getApplicationContext(),dosing.class);
        startActivity(i);
    }
    long map(long x, long in_min, long in_max, long out_min, long out_max) {
        if(((in_max - in_min) + out_min)!=0) {
            return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
        }
        else return 0;
    }
}