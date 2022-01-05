package com.menar.milkdoser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;


import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class manufa extends AppCompatActivity {
    private int currentApiVersion;
    boolean channel_mode = false;

    WifiManager wManager;
    Resources resources;
    ImageButton save_btn, back_btn, home_btn;
    TextView rssi, mac;
    ListView w_list;
    Switch mono_mode_sw;
    Context ctx;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Timer timer;
    String[] devs = new String[0];
    ConstraintLayout layout;
    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
    );


    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    public void initviews() {

        back_btn = findViewById(R.id.back);
        home_btn = findViewById(R.id.home);
        mono_mode_sw = findViewById(R.id.switch1);
        save_btn = findViewById(R.id.save);
        rssi = findViewById(R.id.rssi_viewer);
        w_list = findViewById(R.id.wifi_list);
        rssi.setText("Bağlantı Kalitesi: " + wManager.getConnectionInfo().getRssi() + "dBm");
        mac = findViewById(R.id.mac_number);
        mac.setText(sharedPref.getString("wifi_name", ""));
        mono_mode_sw.setChecked(!sharedPref.getBoolean("mono_mode", false));
        mono_mode_sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("mono_mode", !isChecked);
            editor.commit();
            Log.d("Swithc", ":" + isChecked);
        });
        back_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
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
        home_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
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
                Toast.makeText(getApplicationContext(), resources.getString(R.string.ayarlanan_parametreler_kaydedildi), Toast.LENGTH_LONG).show();
            }
            return true;
        });
        w_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                AlertDialog.Builder diyalogOlusturucu =
                        new AlertDialog.Builder(manufa.this);

                diyalogOlusturucu.setMessage("Bu cihaz ile eşleştirilsinmi?")
                        .setCancelable(true)
                        .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.Q)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mac.setText(devs[position]);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                diyalogOlusturucu.create().show();

            }
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
        wManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //save_default_params();
        init_params();
        /////////////////
        Configuration conf = getResources().getConfiguration();
        conf.locale = new Locale(sharedPref.getString("running_lang","tr")); //french language locale
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        resources = new Resources(getAssets(), metrics, conf);
        ////////////////
        initviews();
        timer = new Timer();
        timer.schedule(timerTask, 200, 500);
        registerReceiver(mWifiScanReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wManager.startScan();
    }

    final TimerTask timerTask = new TimerTask() {
        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            runOnUiThread(() -> {
                WifiManager wifiMan=(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiMan.startScan();
                int newRssi = wifiMan.getConnectionInfo().getRssi();
                rssi.setText("Bağlantı Kalitesi: "+ newRssi +"dBm");
                //Log.d("RSSI Level",""+String.valueOf(newRssi));
            });
        }
    };
    private final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
            {
                List<ScanResult> scanResults = wManager.getScanResults();
                devs = new String[0];
                for (ScanResult result:
                        scanResults) {
                    if(result.SSID.contains("MilkDoser"))
                    {
                        devs = Arrays.copyOf(devs, devs.length + 1);
                        devs[devs.length-1]=result.SSID.replace("MilkDoser-","");
                    }
                }
                ArrayAdapter<String> veriAdaptoru=new ArrayAdapter<String>
                    (manufa.this, android.R.layout.simple_list_item_1, android.R.id.text1, devs);
                w_list.setAdapter(veriAdaptoru);
            }
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