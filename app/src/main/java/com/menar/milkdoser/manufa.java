package com.menar.milkdoser;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Timer;
import java.util.TimerTask;

public class manufa extends AppCompatActivity {
    private int currentApiVersion;
    boolean channel_mode=false;
    ImageButton save_btn,back_btn,home_btn;
    EditText mac;
    TextView rssi;
    Switch mono_mode_sw;
    Context ctx;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    WifiManager wManager;
    Timer timer;
    ConstraintLayout layout;
    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
    );


    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    public void initviews() {

        back_btn= findViewById(R.id.back);
        home_btn= findViewById(R.id.home);
        mono_mode_sw= findViewById(R.id.switch1);
        save_btn = findViewById(R.id.save);
        rssi = findViewById(R.id.rssi_viewer);
        rssi.setText("Bağlantı Kalitesi: "+ wManager.getConnectionInfo().getRssi() +"dBm");
        mac = findViewById(R.id.mac_number);
        mac.setText(sharedPref.getString("wifi_name",""));
        mono_mode_sw.setChecked(!sharedPref.getBoolean("mono_mode",false));
        mono_mode_sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("mono_mode",!isChecked);
            editor.commit();
            Log.d("Swithc",":"+isChecked);
        });

        back_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                Intent i = new Intent(getApplicationContext(),dosing.class);
                startActivity(i);
            }
            return true;
        });

        home_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                Intent i = new Intent(getApplicationContext(),dosing.class);
                startActivity(i);
            }
            return true;
        });

        save_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                editor.putString("wifi_name", (String.valueOf(mac.getText())).toUpperCase());
                editor.commit();
                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.ayarlanan_parametreler_kaydedildi), Toast.LENGTH_LONG).show();
            }
            return true;
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        super.onCreate(savedInstanceState);
        currentApiVersion = android.os.Build.VERSION.SDK_INT;

        hide_system_ui();
        setContentView(R.layout.activity_manufa);
        ctx=this;
        //preventStatusBarExpansion(ctx);
        wManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        KeyboardUtils.addKeyboardToggleListener(this, isVisible -> {
            if(!isVisible)
            {
                hide_system_ui();
            }
            else
            {
                hide_system_ui();
            }
        });
        //save_default_params();
        init_params();
        initviews();
        timer = new Timer();
        timer.schedule(timerTask, 200, 500);

    }

    final TimerTask timerTask = new TimerTask() {
        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            runOnUiThread(() -> {
                WifiManager wifiMan=(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiMan.startScan();
                int newRssi = wifiMan.getConnectionInfo().getRssi();
                rssi.setText("Bağlantı Kalitesi: "+String.valueOf(newRssi)+"dBm");
                //Log.d("RSSI Level",""+String.valueOf(newRssi));
            });
        }
    };
    private BroadcastReceiver myRssiChangeReceiver
            = new BroadcastReceiver(){
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            WifiManager wifiMan=(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiMan.startScan();
            int newRssi = wifiMan.getConnectionInfo().getRssi();

            rssi.setText("Bağlantı Kalitesi: "+ newRssi +"dBm");
            //Log.d("RSSI Level",""+String.valueOf(newRssi));
        }
    };
    public static void preventStatusBarExpansion(Context context) {
        WindowManager manager = ((WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE));

        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int result;
        if (resId > 0) {
            result = context.getResources().getDimensionPixelSize(resId);
        } else {
            // Use Fallback size:
            result = 60; // 60px Fallback
        }

        localLayoutParams.height = result;
        localLayoutParams.format = PixelFormat.TRANSPARENT;

        pincode.CustomViewGroup view = new pincode.CustomViewGroup(context);
        manager.addView(view, localLayoutParams);
    }
    public static class CustomViewGroup extends ViewGroup {
        public CustomViewGroup(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {

            return true;
        }
    }
    public void hide_system_ui()
    {
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
                    hide_system_ui();
                    decorView.setSystemUiVisibility(flags);
                }
            });
        }
    }

    public void init_params()
    {
        channel_mode=sharedPref.getBoolean("mono_mode",false);
    }
}