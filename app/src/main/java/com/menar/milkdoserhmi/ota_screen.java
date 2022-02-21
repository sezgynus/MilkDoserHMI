package com.menar.milkdoserhmi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSuggestion;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.widget.ImageView;

public class ota_screen extends AppCompatActivity {

    Process p;
    private int currentApiVersion;
    ImageView rotating_prgrss;
    ImageButton back_btn,home_btn,store;
    TextView myIP,ota_txt;
    Context ctx;
    WifiManager wifiMgr;
    WifiInfo wifiInfo;
    String network_pass="";
    WifiConfiguration conf;
    private List<ScanResult> results;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;
    int ip;
    String ipAddress;
    Timer check;

    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
    );

    public static void preventStatusBarExpansion(Context context) {
        WindowManager manager = ((WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE));

        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int result = 0;
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

    public void initviews(){
        back_btn=(ImageButton)findViewById(R.id.back);
        home_btn=(ImageButton)findViewById(R.id.home);
        myIP=(TextView)findViewById(R.id.text_myIP);
        store=findViewById(R.id.play_store);
        ota_txt=findViewById(R.id.oto_update_text);
        store.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                    params.setMargins(8,0,8,0);
                    view.setLayoutParams(params);
                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                    params.setMargins(0, 0, 0, 0);
                    view.setLayoutParams(params);
                    try {
                        if (isConnected()) {
                            final String appPackageName = getPackageName();
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),"Otomatik güncelleme için internete bağlanmalısınız", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(Settings.Panel.ACTION_WIFI);
                            startActivity(intent);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });
        back_btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                    params.setMargins(8,0,8,0);
                    view.setLayoutParams(params);
                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                    params.setMargins(0,0,0,0);
                    view.setLayoutParams(params);
                    //adbd_stop();
                    Intent i = new Intent(getApplicationContext(),settings.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                    overridePendingTransition(0,0);
                }
                return true;
            }
        });
        home_btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                    params.setMargins(8,0,8,0);
                    view.setLayoutParams(params);
                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                    params.setMargins(0,0,0,0);
                    view.setLayoutParams(params);
                    //adbd_stop();
                    Intent i = new Intent(getApplicationContext(),dosing.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                    overridePendingTransition(0,0);
                }
                return true;
            }
        });
        myIP.setText("Yerel Güncelleme Adresi: "+ipAddress);
    }

    private boolean connect_wifi(String ssid, String pass) {
        wifiMgr.setWifiEnabled(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Log.d("SDK","Q");


            WifiNetworkSuggestion networkSuggestion1 =
                    new WifiNetworkSuggestion.Builder()
                            .setSsid(ssid)
                            .setWpa2Passphrase(pass)
                            .setPriority(0)
                            .build();

            List<WifiNetworkSuggestion> suggestionsList = new ArrayList<>();
            suggestionsList.add(networkSuggestion1);
            wifiMgr.removeNetworkSuggestions(suggestionsList);
            int ret=wifiMgr.addNetworkSuggestions(suggestionsList);


            Log.d("RET", ""+ret);

            return true;

        }

        else {
            conf = new WifiConfiguration();
            conf.SSID = "\"" + ssid + "\"";
            conf.preSharedKey = "\"" + pass + "\"";
            int networkId = wifiMgr.addNetwork(conf);
            wifiMgr.disconnect();
            wifiMgr.enableNetwork(networkId, true);
            Boolean ret = wifiMgr.reconnect();
            Log.d("RET", String.valueOf(ret));
            return ret;
        }
    }
    public boolean isConnected() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }
    protected void onPause() {
        super.onPause();
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(null);
        currentApiVersion = android.os.Build.VERSION.SDK_INT;
        wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        wifiInfo = wifiMgr.getConnectionInfo();
        ip = wifiInfo.getIpAddress();
        ipAddress = String.format("%d.%d.%d.%d", (ip & 0xff),(ip >> 8 & 0xff),(ip >> 16 & 0xff),(ip >> 24 & 0xff));
        Log.d("İP",ipAddress);
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
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
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
        setContentView(R.layout.activity_ota_screen);
        initviews();
        check = new Timer();
        //check.schedule(checkinternet,0,500);
        try {
            if(isConnected())
            {
                store.setImageResource(R.drawable.ota_icon);
                ota_txt.setText("Otomatik güncelleme için butona tıklayın");
            }
            else
            {
                store.setImageResource(R.drawable.scanwifi);
                ota_txt.setText("Otomatik güncelleme için bir ağa bağlanın");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ctx=this;
        //preventStatusBarExpansion(ctx);
        adbd_start();
    }
    final TimerTask checkinternet = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if(isConnected())
                        {
                            store.setImageResource(R.drawable.ota_icon);
                            ota_txt.setText("Otomatik güncelleme için butona tıklayın");
                        }
                        else
                        {
                            store.setImageResource(R.drawable.scanwifi);
                            ota_txt.setText("Otomatik güncelleme için bir ağa bağlanın");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
    private void scanWifi() {
        arrayList.clear();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiMgr.startScan();
        Toast.makeText(this, "Scanning WiFi Networks ...", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(),"OTA Servisi Açıldı", Toast.LENGTH_LONG).show();
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
            Toast.makeText(getApplicationContext(),"OTA Servisi Kapatıldı", Toast.LENGTH_LONG).show();
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

    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            results = wifiMgr.getScanResults();
            unregisterReceiver(this);

            for (ScanResult scanResult : results) {
                if(!scanResult.SSID.contains("MilkDoser")) {
                    arrayList.add(scanResult.SSID);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };
}