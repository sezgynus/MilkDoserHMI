package com.menar.milkdoser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.DataOutputStream;
import java.io.IOException;

public class ota_screen extends AppCompatActivity {

    Process p;
    ImageButton back_btn,home_btn;
    TextView myIP;
    Context ctx;
    WifiManager wifiMgr;
    WifiInfo wifiInfo;
    int ip;
    String ipAddress;

    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
    );

    private void startKioskService() {
        startService(new Intent(this, KioskService.class));
    }

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

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    public void initviews(){
        back_btn= findViewById(R.id.back);
        home_btn= findViewById(R.id.home);
        myIP= findViewById(R.id.text_myIP);

        back_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                //adbd_stop();
                Intent i = new Intent(getApplicationContext(),settings.class);
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
                //adbd_stop();
                Intent i = new Intent(getApplicationContext(),dosing.class);
                startActivity(i);
            }
            return true;
        });
        myIP.setText("IP: "+ipAddress);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(null);
        startKioskService();
        int currentApiVersion = Build.VERSION.SDK_INT;
        wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        wifiInfo = wifiMgr.getConnectionInfo();
        ip = wifiInfo.getIpAddress();
        ipAddress = Formatter.formatIpAddress(ip);
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
        setContentView(R.layout.activity_ota_screen);
        initviews();
        ctx=this;
        //preventStatusBarExpansion(ctx);
        adbd_start();
    }
    public void adbd_start()
    {
        try {
            p=Runtime.getRuntime().exec("su");
        } catch (IOException e) {
            e.printStackTrace();
        }
        DataOutputStream dos = new DataOutputStream(p.getOutputStream());
        try {
            dos.writeBytes("setprop service.adb.tcp.port 5555\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dos.writeBytes("stop adbd\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dos.writeBytes("start adbd\n");
            Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.OTA_servisi_acildi), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void adbd_stop()
    {
        try {
            p=Runtime.getRuntime().exec("su");
        } catch (IOException e) {
            e.printStackTrace();
        }
        DataOutputStream dos = new DataOutputStream(p.getOutputStream());
        try {
            dos.writeBytes("setprop service.adb.tcp.port -1\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dos.writeBytes("stop adbd\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dos.writeBytes("start adbd\n");
            Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.OTA_servisi_kapatildi), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}