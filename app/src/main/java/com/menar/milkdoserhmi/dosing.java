package com.menar.milkdoserhmi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class dosing extends AppCompatActivity {
    ImageView cb1, cb2, connection, l_rinse, r_rinse, logo_view,waring_view;
    ImageButton manual_l, manual_r, x1l_button, x2l_button, x3l_button, x4l_button, x1r_button, x2r_button, x3r_button, x4r_button, x1l_cncl, x2l_cncl, x3l_cncl, x4l_cncl, x1r_cncl, x2r_cncl, x3r_cncl, x4r_cncl, settings_button, l_hot, l_ice, l_cold, r_hot, r_ice, r_cold, respite_l, start_l, start_l_w,respite_r, start_r, start_r_w, power, info;
    TextView l_rinse_txt, r_rinse_txt, date, time, l_att, r_att;
    ConnectivityManager connManager;
    NetworkInfo mWifi;
    WifiManager wManager;
    WifiConfiguration conf;

    String networkSSID = "";
    String networkPass = "12345678";


    Context ctx;
    Context context;
    int flow_timeout = 15, a_flow_timeout_cnt = 0, b_flow_timeout_cnt = 0; // ak???? zaman a????m??
    int dose_l_group = 1;
    int dose_r_group = 1;
    float PPML_A = (float) 3043.44, PPML_B = (float) 3043.44; //100ml ba????na pulse
    boolean no_rinse_t=false,no_rinse_n=false;
    boolean wash_button_pressed = false, rinse_button_pressed = false;
    boolean l_manuel_dosing = false, r_manuel_dosing = false;
    boolean menu_change = false;
    boolean spd_update = false;
    boolean V1 = false, V2 = false, V3 = false, V4 = false;
    boolean packet_received=false;
    String home;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    String device_id = "";
    // Kanal-A doz parametreleri
    int ENCSET_A_11, ENCSET_A_12, ENCSET_A_13, ENCSET_A_14, ENCSET_A_21, ENCSET_A_22, ENCSET_A_23, ENCSET_A_24, ENCSET_A_31, ENCSET_A_32, ENCSET_A_33, ENCSET_A_34;
    // Kanal-B doz parametreleri
    int ENCSET_B_11, ENCSET_B_12, ENCSET_B_13, ENCSET_B_14, ENCSET_B_21, ENCSET_B_22, ENCSET_B_23, ENCSET_B_24, ENCSET_B_31, ENCSET_B_32, ENCSET_B_33, ENCSET_B_34;
    //Geri ??ekme parametreleri
    int ENCSET_PIPE_ADD, ENCSET_A_BACK, ENCSET_B_BACK, T_IDLE_BACK;
    //Durulama parametreleri
    int T_RINSE_TRIG, N_RINSE_TRIG, T_RINSE, ENCSET_A_SWEEP, ENCSET_B_SWEEP;
    boolean PUMP_LOCK;
    //Y??kama parametreleri
    int WASH_TAKE, WASH_DWELL, WASH_RINSE, WASH_CYCLE;
    //PWM parametreleri
    int PWM_A = 1023, PWM_B = 1023;

    int l_wash = 0, l_detergent_cnt = 0, l_wait_cnt = 0, r_wash = 0, r_detergent_cnt = 0, r_wait_cnt = 0;

    int hour,minute;

    boolean l_detergent = false, l_wait = false, l_wash_flag = false, r_detergent = false, r_wait = false, r_wash_flag = false;

    boolean l_wash_prosedure = false,r_wash_prosedure = false;

    boolean mono_mode = true;

    boolean l_wash_complete_flag,r_wash_complete_flag;

    byte tx_buffer[]={ 0x02, 0x55, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x57 };
    public char[] speed_set = {0x02, 0x77, 0x11, 0x00, 0xbc, 0x02, 0xbc, 0x02, 0x57};
    public char[] query_buf = {0x02, 0x55, 0x11, 0x00, 0x00, 0x00, 0x00, 0x00, 0x57};
    public char[] l_buf = {0x02, 0x55, 0x06, 0x02, 0x00, 0x00, 0xD0, 0xFA, 0x29};
    public char[] r_buf = {0x02, 0x55, 0x60, 0x01, 0xD0, 0xFA, 0x00, 0x00, 0x93};
    public char[] l_valve_reset = {0x02, 0x55, 0x00, 0x20, 0x00, 0x00, 0xD0, 0xFA, 0x29};
    public char[] r_valve_reset = {0x02, 0x55, 0x00, 0x10, 0xD0, 0xFA, 0x00, 0x00, 0x93};
    public char[] l_start = {0x02, 0x55, 0x05, 0x02, 0x00, 0x00, 0x00, 0x00, 0x5D};
    public char[] r_start = {0x02, 0x55, 0x50, 0x01, 0x00, 0x00, 0x00, 0x00, 0xA9};
    public char[] l_stop = {0x02, 0x55, 0x0D, 0x20, 0x00, 0x00, 0x00, 0x00, 0x74};
    public char[] r_stop = {0x02, 0x55, 0xD0, 0x10, 0x00, 0x00, 0x00, 0x00, 0x47};
    public char[] l_rinse_start = {0x02, 0x55, 0x06, 0x14, 0x00, 0x00, 0x00, 0x00, 0x70};
    public char[] r_rinse_start = {0x02, 0x55, 0x60, 0x28, 0x00, 0x00, 0x00, 0x00, 0xCF};
    public char[] l_rinse_stop = {0x02, 0x55, 0x0D, 0x40, 0x00, 0x00, 0x00, 0x00, 0xA4};
    public char[] r_rinse_stop = {0x02, 0x55, 0xD0, 0x80, 0x00, 0x00, 0x00, 0x00, 0xA7};


    private UsbService usbService;
    private RxHandler mHandler;

    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
    );

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Toast.makeText(context, "RS485 Ready", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    //Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_NO_USB: // NO USB CONNECTED
                    Toast.makeText(context, "No RS485 interface", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    Toast.makeText(context, "RS485 interface disconnected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    break;
            }
        }
    };
    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbService.UsbBinder) arg1).getService();
            usbService.setHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };
    public boolean wifi_connected = false;
    Timer timer, rinse, wash, r_rinse_cnt, l_rinse_cnt, time_tmr, l_wash_cnt, r_wash_cnt;
    final Timer connection_timer = new Timer();
    int dose_l_set, dose_r_set, l_cmd = 0, r_cmd = 0;
    int t_rinse_cnt_l = 0, t_rinse_cnt_r = 0, n_rinse_cnt_l = 0, a_sweep_cnt = 0, b_sweep_cnt = 0, n_rinse_cnt_r = 0, t_wash_cnt = 0,r_rinse_time=0,l_rinse_time=0;
    int t_rinse_r = 0, t_rinse_l = 0;
    boolean t_rinse_cnt_l_flag = false, t_rinse_cnt_r_flag = false, a_sweep_flag = false, b_sweep_flag = false, a_sweep_timeout_flag = false, b_sweep_timeout_flag = false;
    boolean left_dosing = false, right_dosing = false, left_dosing_finish = false, right_dosing_finish = false;
    boolean r_rinse_after_wash=false,l_rinse_after_wash=false;
    int left_dosing_counter = 1000, right_dosing_counter = 1000;
    int cmd_repeat_cnt = 5, l_start_cmd_cnt = 0, l_stop_cmd_cnt = 0, l_rinse_start_cmd_cnt = 0, l_rinse_stop_cmd_cnt = 0, r_start_cmd_cnt = 0, r_stop_cmd_cnt = 0, r_rinse_start_cmd_cnt = 0, r_rinse_stop_cmd_cnt = 0;
    long encoder_l_value_last=0, encoder_r_value_last=0;
    Resources resources;

    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_SETTINGS};
    @SuppressLint("ClickableViewAccessibility")
    public void initviews() {
        cb1 = findViewById(R.id.bar);
        cb2 = findViewById(R.id.bar2);
        waring_view = findViewById(R.id.warning);
        l_rinse = findViewById(R.id.rinse_l);
        r_rinse = findViewById(R.id.rinse_r);
        connection = findViewById(R.id.connection_status);
        logo_view = findViewById(R.id.logoviewer);
        respite_l = findViewById(R.id.l_respite);
        start_l = findViewById(R.id.l_start);
        start_l_w = findViewById(R.id.l_start_wash);
        respite_r = findViewById(R.id.r_respite);
        start_r = findViewById(R.id.r_start);
        start_r_w = findViewById(R.id.r_start_wash);
        manual_l = findViewById(R.id.manuel_l);
        manual_r = findViewById(R.id.manuel_r);
        settings_button = findViewById(R.id.settings);
        x1l_button = findViewById(R.id.x1_l);
        x2l_button = findViewById(R.id.x2_l);
        x3l_button = findViewById(R.id.x3_l);
        x4l_button = findViewById(R.id.x4_l);
        x1r_button = findViewById(R.id.x1_r);
        x2r_button = findViewById(R.id.x2_r);
        x3r_button = findViewById(R.id.x3_r);
        x4r_button = findViewById(R.id.x4_r);

        x1l_cncl = findViewById(R.id.l_cancel1x);
        x2l_cncl = findViewById(R.id.l_cancel2x);
        x3l_cncl = findViewById(R.id.l_cancel3x);
        x4l_cncl = findViewById(R.id.l_cancel4x);

        x1r_cncl = findViewById(R.id.r_cancel1x);
        x2r_cncl = findViewById(R.id.r_cancel2x);
        x3r_cncl = findViewById(R.id.r_cancel3x);
        x4r_cncl = findViewById(R.id.r_cancel4x);

        l_hot = findViewById(R.id.left_hot);
        l_ice = findViewById(R.id.left_ice);
        l_cold = findViewById(R.id.left_cold);
        r_hot = findViewById(R.id.right_hot);
        r_ice = findViewById(R.id.right_ice);
        r_cold = findViewById(R.id.right_cold);
        power = findViewById(R.id.power_btn);
        info = findViewById(R.id.info_btn);
        l_rinse_txt = findViewById(R.id.left_rinse_text);
        r_rinse_txt = findViewById(R.id.right_rinse_text);
        date = findViewById(R.id.date1);
        time = findViewById(R.id.time1);

        l_att = findViewById(R.id.textView41);
        r_att = findViewById(R.id.textView42);

        params = (ConstraintLayout.LayoutParams) l_hot.getLayoutParams();
        params.setMargins(8, 0, 8, 0);
        l_hot.setLayoutParams(params);

        params = (ConstraintLayout.LayoutParams) r_hot.getLayoutParams();
        params.setMargins(8, 0, 8, 0);
        r_hot.setLayoutParams(params);

        //Bitmap bmp = BitmapFactory.decodeFile(home + "/logo/logo.bmp");
        //logo_view.setImageBitmap(bmp);
        settings_button.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
                menu_change = true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                wifi_connected = false;
                Intent i = new Intent(getApplicationContext(), pincode.class);
                startActivity(i);
                finish();
            }
            return true;
        });
        respite_l.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                hide_rinse_screen("left");
                save_cleanig_events("A", "rinse_respite");
                t_rinse_cnt_l = T_RINSE_TRIG - 3;
                editor.putInt("force_rinse_l_cnt",(sharedPref.getInt("force_rinse_l_cnt",0)+1));
                editor.commit();
                if(sharedPref.getInt("force_rinse_l_cnt",0)>2){
                    editor.putBoolean("force_rinse_l",true);
                    editor.commit();
                }
                Log.d("ForceL","Flag:"+sharedPref.getBoolean("force_rinse_l",false)+" CNT:"+sharedPref.getInt("force_rinse_l_cnt",0));
            }
            return true;
        });
        start_l.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                if (PUMP_LOCK) l_rinse_start[2] = 0x06;
                else l_rinse_start[2] = 0x00;
                if (l_wash_prosedure) {
                    l_cmd = 5;
                    l_wash = 0;
                    l_detergent = true;
                    l_wash_flag = true;
                    start_l.setVisibility(View.INVISIBLE);
                    respite_l.setVisibility(View.INVISIBLE);
                    settings_button.setVisibility(View.INVISIBLE);
                    l_rinse_txt.setVisibility(View.VISIBLE);
                    l_rinse_txt.setText(resources.getString(R.string.yikama_islemi) + "1/" + WASH_CYCLE);
                    Log.d("L_wash", "WASH_TAKE=" + WASH_TAKE + " WASH_DWELL=" + WASH_DWELL + " WASH_CYCLE=" + WASH_CYCLE + " WASH_RINSE=" + WASH_RINSE);
                }
                else {
                    if(l_rinse_after_wash) {
                        l_rinse_start[2] = 0x05;
                        l_rinse_start[3] = 0x02;
                        l_rinse_time=WASH_RINSE;
                        l_att.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        if(PUMP_LOCK)l_rinse_start[2]=0x05;
                        else l_rinse_start[2]=0x00;
                        l_rinse_start[3] = 0x14;
                        l_rinse_time=T_RINSE;
                    }
                    l_cmd = 7;
                    start_l.setVisibility(View.INVISIBLE);
                    respite_l.setVisibility(View.INVISIBLE);
                    settings_button.setVisibility(View.INVISIBLE);
                    l_rinse_txt.setVisibility(View.VISIBLE);
                    t_rinse_cnt_l_flag = true;
                    n_rinse_cnt_l = 0;
                    save_cleanig_events("A", "rinse_start");
                    Log.d("Rinse", "left: start touch");
                }
            }
            return true;
        });
        start_l_w.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                if (PUMP_LOCK) l_rinse_start[2] = 0x06;
                else l_rinse_start[2] = 0x00;
                if (l_wash_prosedure) {
                    l_cmd = 5;
                    l_wash = 0;
                    l_detergent = true;
                    l_wash_flag = true;
                    start_l_w.setVisibility(View.INVISIBLE);
                    respite_l.setVisibility(View.INVISIBLE);
                    settings_button.setVisibility(View.INVISIBLE);
                    l_rinse_txt.setVisibility(View.VISIBLE);
                    l_rinse_txt.setText(resources.getString(R.string.yikama_islemi) + "1/" + WASH_CYCLE);
                    l_att.setVisibility(View.INVISIBLE);
                    Log.d("L_wash", "WASH_TAKE=" + WASH_TAKE + " WASH_DWELL=" + WASH_DWELL + " WASH_CYCLE=" + WASH_CYCLE + " WASH_RINSE=" + WASH_RINSE);
                }
                else {
                    if(l_rinse_after_wash) {
                        l_rinse_start[2] = 0x05;
                        l_rinse_start[3] = 0x02;
                        l_rinse_time=WASH_RINSE;
                        l_att.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        if(PUMP_LOCK)l_rinse_start[2]=0x05;
                        else l_rinse_start[2]=0x00;
                        l_rinse_start[3] = 0x14;
                        l_rinse_time=T_RINSE;
                        editor.putInt("force_rinse_l_cnt",0);
                        editor.putBoolean("force_rinse_l",false);
                        editor.commit();
                    }
                    l_cmd = 7;
                    start_l_w.setVisibility(View.INVISIBLE);
                    respite_l.setVisibility(View.INVISIBLE);
                    settings_button.setVisibility(View.INVISIBLE);
                    l_rinse_txt.setVisibility(View.VISIBLE);
                    t_rinse_cnt_l_flag = true;
                    n_rinse_cnt_l = 0;
                    save_cleanig_events("A", "rinse_start");
                    Log.d("Rinse", "left: start touch");
                }
            }
            return true;
        });
        respite_r.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                hide_rinse_screen("right");
                save_cleanig_events("B", "rinse_respite");
                t_rinse_cnt_r = T_RINSE_TRIG - 3;
                editor.putInt("force_rinse_r_cnt",(sharedPref.getInt("force_rinse_r_cnt",0)+1));
                editor.commit();
                if(sharedPref.getInt("force_rinse_r_cnt",0)>2){
                    editor.putBoolean("force_rinse_r",true);
                    editor.commit();
                }
                Log.d("ForceR","Flag:"+sharedPref.getBoolean("force_rinse_r",false)+" CNT:"+sharedPref.getInt("force_rinse_r_cnt",0));
            }
            return true;
        });
        start_r.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                if (PUMP_LOCK) r_rinse_start[2] = 0x60;
                else r_rinse_start[2] = 0x00;
                if (r_wash_prosedure) {
                    r_cmd = 5;
                    r_wash = 0;
                    r_detergent = true;
                    r_wash_flag = true;
                    start_r.setVisibility(View.INVISIBLE);
                    respite_r.setVisibility(View.INVISIBLE);
                    settings_button.setVisibility(View.INVISIBLE);
                    r_rinse_txt.setVisibility(View.VISIBLE);
                    r_rinse_txt.setText(resources.getString(R.string.yikama_islemi) + "1/" + WASH_CYCLE);
                    Log.d("R_wash", "WASH_TAKE=" + WASH_TAKE + " WASH_DWELL=" + WASH_DWELL + " WASH_CYCLE=" + WASH_CYCLE + " WASH_RINSE=" + WASH_RINSE);
                }
                else {
                    if(r_rinse_after_wash) {
                        r_rinse_start[2] = 0x50;
                        r_rinse_start[3] = 0x01;
                        r_rinse_time=WASH_RINSE;
                        r_att.setVisibility(View.INVISIBLE);
                        Log.d("Wash","rinse");
                    }
                    else
                    {
                        if(PUMP_LOCK)r_rinse_start[2]=0x50;
                        else r_rinse_start[2]=0x00;
                        r_rinse_start[3] = 0x28;
                        r_rinse_time=T_RINSE;
                    }
                    r_cmd = 7;
                    start_r.setVisibility(View.INVISIBLE);
                    respite_r.setVisibility(View.INVISIBLE);
                    settings_button.setVisibility(View.INVISIBLE);
                    r_rinse_txt.setVisibility(View.VISIBLE);
                    t_rinse_cnt_r_flag = true;
                    n_rinse_cnt_r = 0;
                    save_cleanig_events("B", "rinse_start");
                    Log.d("Rinse", "right: start touch");
                }
            }
            return true;
        });
        start_r_w.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                if (PUMP_LOCK) r_rinse_start[2] = 0x60;
                else r_rinse_start[2] = 0x00;
                if (r_wash_prosedure) {
                    r_cmd = 5;
                    r_wash = 0;
                    r_detergent = true;
                    r_wash_flag = true;
                    start_r_w.setVisibility(View.INVISIBLE);
                    respite_r.setVisibility(View.INVISIBLE);
                    settings_button.setVisibility(View.INVISIBLE);
                    r_rinse_txt.setVisibility(View.VISIBLE);
                    r_rinse_txt.setText(resources.getString(R.string.yikama_islemi) + "1/" + WASH_CYCLE);
                    r_att.setVisibility(View.INVISIBLE);
                    Log.d("R_wash", "WASH_TAKE=" + WASH_TAKE + " WASH_DWELL=" + WASH_DWELL + " WASH_CYCLE=" + WASH_CYCLE + " WASH_RINSE=" + WASH_RINSE);
                }
                else {
                    if(r_rinse_after_wash) {
                        r_rinse_start[2] = 0x50;
                        r_rinse_start[3] = 0x01;
                        r_rinse_time=WASH_RINSE;
                        r_att.setVisibility(View.INVISIBLE);
                        Log.d("Wash","rinse");
                    }
                    else
                    {
                        if(PUMP_LOCK)r_rinse_start[2]=0x50;
                        else r_rinse_start[2]=0x00;
                        r_rinse_start[3] = 0x28;
                        editor.putInt("force_rinse_r_cnt",0);
                        editor.putBoolean("force_rinse_r",false);
                        editor.commit();
                        r_rinse_time=T_RINSE;
                    }
                    r_cmd = 7;
                    start_r_w.setVisibility(View.INVISIBLE);
                    respite_r.setVisibility(View.INVISIBLE);
                    settings_button.setVisibility(View.INVISIBLE);
                    r_rinse_txt.setVisibility(View.VISIBLE);
                    t_rinse_cnt_r_flag = true;
                    n_rinse_cnt_r = 0;
                    save_cleanig_events("B", "rinse_start");
                    Log.d("Rinse", "right: start touch");
                }
            }
            return true;
        });
        manual_r.setOnTouchListener((view, event) -> {
            if (!right_dosing) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                    params.setMargins(8, 0, 8, 0);
                    view.setLayoutParams(params);
                    r_manuel_dosing = true;
                    r_cmd = 5;
                    Log.d("Manuel", "R pressed");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                    params.setMargins(0, 0, 0, 0);
                    view.setLayoutParams(params);
                    r_manuel_dosing = false;
                    r_cmd = 6;
                    Log.d("Manuel", "R released");
                }
            }
            return true;
        });
        manual_l.setOnTouchListener((view, event) -> {
            if (!left_dosing) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                    params.setMargins(8, 0, 8, 0);
                    view.setLayoutParams(params);
                    l_manuel_dosing = true;
                    l_cmd = 5;
                    Log.d("Manuel", "L pressed");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                    params.setMargins(0, 0, 0, 0);
                    view.setLayoutParams(params);
                    l_manuel_dosing = false;
                    l_cmd = 6;
                    Log.d("Manuel", "L released");
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    Log.d("Manuel", "L cancelled");
                }
            }
            return true;
        });
        l_hot.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) l_ice.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                l_ice.setLayoutParams(params);
                params = (ConstraintLayout.LayoutParams) l_cold.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                l_cold.setLayoutParams(params);
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                x1l_button.setImageResource(R.drawable.hotcup1x);
                x2l_button.setImageResource(R.drawable.hotcup2x);
                x3l_button.setImageResource(R.drawable.hotcup3x);
                x4l_button.setImageResource(R.drawable.hotcup4x);
                view.setLayoutParams(params);
                dose_l_group = 1;
            }
            else if (event.getAction() == MotionEvent.ACTION_UP)
            {

            }
            return true;
        });
        l_ice.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) l_hot.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                l_hot.setLayoutParams(params);
                params = (ConstraintLayout.LayoutParams) l_cold.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                l_cold.setLayoutParams(params);
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
                x1l_button.setImageResource(R.drawable.icecup1x);
                x2l_button.setImageResource(R.drawable.icecup2x);
                x3l_button.setImageResource(R.drawable.icecup3x);
                x4l_button.setImageResource(R.drawable.icecup4x);
                dose_l_group = 2;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
            }
            return true;
        });
        l_cold.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) l_hot.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                l_hot.setLayoutParams(params);
                params = (ConstraintLayout.LayoutParams) l_ice.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                l_ice.setLayoutParams(params);
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
                x1l_button.setImageResource(R.drawable.coldcup1x);
                x2l_button.setImageResource(R.drawable.coldcup2x);
                x3l_button.setImageResource(R.drawable.coldcup3x);
                x4l_button.setImageResource(R.drawable.coldcup4x);
                dose_l_group = 3;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
            }
            return true;
        });
        r_hot.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) r_ice.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                r_ice.setLayoutParams(params);
                params = (ConstraintLayout.LayoutParams) r_cold.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                r_cold.setLayoutParams(params);
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
                x1r_button.setImageResource(R.drawable.hotcup1x);
                x2r_button.setImageResource(R.drawable.hotcup2x);
                x3r_button.setImageResource(R.drawable.hotcup3x);
                x4r_button.setImageResource(R.drawable.hotcup4x);
                dose_r_group = 1;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
            }
            return true;
        });
        r_ice.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) r_hot.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                r_hot.setLayoutParams(params);
                params = (ConstraintLayout.LayoutParams) r_cold.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                r_cold.setLayoutParams(params);
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
                x1r_button.setImageResource(R.drawable.icecup1x);
                x2r_button.setImageResource(R.drawable.icecup2x);
                x3r_button.setImageResource(R.drawable.icecup3x);
                x4r_button.setImageResource(R.drawable.icecup4x);
                dose_r_group = 2;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
            }
            return true;
        });
        r_cold.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) r_hot.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                r_hot.setLayoutParams(params);
                params = (ConstraintLayout.LayoutParams) r_ice.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                r_ice.setLayoutParams(params);
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
                x1r_button.setImageResource(R.drawable.coldcup1x);
                x2r_button.setImageResource(R.drawable.coldcup2x);
                x3r_button.setImageResource(R.drawable.coldcup3x);
                x4r_button.setImageResource(R.drawable.coldcup4x);
                dose_r_group = 3;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
            }
            return true;
        });
        x1l_button.setOnTouchListener((view, event) -> {
            if (!left_dosing) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                    params.setMargins(8, 0, 8, 0);
                    view.setLayoutParams(params);
                    x1l_button.setImageResource(R.drawable.cup_pressed1x);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    l_cmd = 1;
                    x1l_cncl.setVisibility(View.VISIBLE);
                    if (dose_l_group == 1) dose_l_set = ENCSET_A_11;
                    else if (dose_l_group == 2) dose_l_set = ENCSET_A_21;
                    else if (dose_l_group == 3) dose_l_set = ENCSET_A_31;
                    else dose_l_set = 65535;
                }
            } else {
                Log.d("Dose button: ", "locked=" + left_dosing);
            }
            return true;
        });
        x2l_button.setOnTouchListener((view, event) -> {
            if (!left_dosing) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                    params.setMargins(8, 0, 8, 0);
                    view.setLayoutParams(params);
                    x2l_button.setImageResource(R.drawable.cup_pressed2x);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    l_cmd = 2;
                    x2l_cncl.setVisibility(View.VISIBLE);
                    if (dose_l_group == 1) dose_l_set = ENCSET_A_12;
                    else if (dose_l_group == 2) dose_l_set = ENCSET_A_22;
                    else if (dose_l_group == 3) dose_l_set = ENCSET_A_32;
                    else dose_l_set = 65535;
                }
            }
            return true;
        });
        x3l_button.setOnTouchListener((view, event) -> {
            if (!left_dosing) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                    params.setMargins(8, 0, 8, 0);
                    view.setLayoutParams(params);
                    x3l_button.setImageResource(R.drawable.cup_pressed3x);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    l_cmd = 3;
                    x3l_cncl.setVisibility(View.VISIBLE);
                    if (dose_l_group == 1) dose_l_set = ENCSET_A_13;
                    else if (dose_l_group == 2) dose_l_set = ENCSET_A_23;
                    else if (dose_l_group == 3) dose_l_set = ENCSET_A_33;
                    else dose_l_set = 65535;
                }
            }
            return true;
        });
        x4l_button.setOnTouchListener((view, event) -> {
            if (!left_dosing) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                    params.setMargins(8, 0, 8, 0);
                    view.setLayoutParams(params);
                    x4l_button.setImageResource(R.drawable.cup_pressed4x);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    l_cmd = 4;
                    x4l_cncl.setVisibility(View.VISIBLE);
                    if (dose_l_group == 1) dose_l_set = ENCSET_A_14;
                    else if (dose_l_group == 2) dose_l_set = ENCSET_A_24;
                    else if (dose_l_group == 3) dose_l_set = ENCSET_A_34;
                    else dose_l_set = 65535;
                }
            }
            return true;
        });
        x1r_button.setOnTouchListener((view, event) -> {
            if (!right_dosing) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                    params.setMargins(8, 0, 8, 0);
                    view.setLayoutParams(params);
                    x1r_button.setImageResource(R.drawable.cup_pressed1x);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    r_cmd = 1;
                    x1r_cncl.setVisibility(View.VISIBLE);
                    if (dose_r_group == 1) dose_r_set = ENCSET_B_11;
                    else if (dose_r_group == 2) dose_r_set = ENCSET_B_21;
                    else if (dose_r_group == 3) dose_r_set = ENCSET_B_31;
                    else dose_r_set = 65535;
                }
            }
            return true;
        });
        x2r_button.setOnTouchListener((view, event) -> {
            if (!right_dosing) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                    params.setMargins(8, 0, 8, 0);
                    view.setLayoutParams(params);
                    x2r_button.setImageResource(R.drawable.cup_pressed2x);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    r_cmd = 2;
                    x2r_cncl.setVisibility(View.VISIBLE);
                    if (dose_r_group == 1) dose_r_set = ENCSET_B_12;
                    else if (dose_r_group == 2) dose_r_set = ENCSET_B_22;
                    else if (dose_r_group == 3) dose_r_set = ENCSET_B_32;
                    else dose_r_set = 65535;
                }
            }
            return true;
        });
        x3r_button.setOnTouchListener((view, event) -> {
            if (!right_dosing) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                    params.setMargins(8, 0, 8, 0);
                    view.setLayoutParams(params);
                    x3r_button.setImageResource(R.drawable.cup_pressed3x);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    r_cmd = 3;
                    x3r_cncl.setVisibility(View.VISIBLE);
                    if (dose_r_group == 1) dose_r_set = ENCSET_B_13;
                    else if (dose_r_group == 2) dose_r_set = ENCSET_B_23;
                    else if (dose_r_group == 3) dose_r_set = ENCSET_B_33;
                    else dose_r_set = 65535;
                }
            }
            return true;
        });
        x4r_button.setOnTouchListener((view, event) -> {
            if (!right_dosing) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                    params.setMargins(8, 0, 8, 0);
                    view.setLayoutParams(params);
                    x4r_button.setImageResource(R.drawable.cup_pressed4x);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    r_cmd = 4;
                    x4r_cncl.setVisibility(View.VISIBLE);
                    if (dose_r_group == 1) dose_r_set = ENCSET_B_14;
                    else if (dose_r_group == 2) dose_r_set = ENCSET_B_24;
                    else if (dose_r_group == 3) dose_r_set = ENCSET_B_34;
                    else dose_r_set = 65535;
                }
            }
            return true;
        });
        x1l_cncl.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                left_dosing = false;
                left_dosing_finish = false;
                Log.d("x1l_cncl: ", "left_dosing=" + left_dosing);
                l_cmd = 6;
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                reset_l_button();
            }
            return true;
        });
        x2l_cncl.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                left_dosing = false;
                l_cmd = 6;
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                reset_l_button();
            }
            return true;
        });
        x3l_cncl.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                l_cmd = 6;
                left_dosing = false;
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                reset_l_button();
            }
            return true;
        });
        x4l_cncl.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                l_cmd = 6;
                left_dosing = false;
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                reset_l_button();
            }
            return true;
        });
        x1r_cncl.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                r_cmd = 6;
                right_dosing = false;
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                reset_r_button();
            }
            return true;
        });
        x2r_cncl.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                r_cmd = 6;
                right_dosing = false;
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                reset_r_button();
            }
            return true;
        });
        x3r_cncl.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                r_cmd = 6;
                right_dosing = false;
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                reset_r_button();
            }
            return true;
        });
        x4r_cncl.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                r_cmd = 6;
                right_dosing = false;
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                reset_r_button();
            }
            return true;
        });
        power.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
                menu_change = true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                Intent i = new Intent(getApplicationContext(), power_menu.class);
                startActivity(i);
                finish();
            }
            return true;
        });
        info.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                menu_change = true;
                Intent i = new Intent(getApplicationContext(), info.class);
                startActivity(i);
                finish();
            }
            return true;
        });
        x1l_cncl.setVisibility(View.INVISIBLE);
        x2l_cncl.setVisibility(View.INVISIBLE);
        x3l_cncl.setVisibility(View.INVISIBLE);
        x4l_cncl.setVisibility(View.INVISIBLE);
        x1r_cncl.setVisibility(View.INVISIBLE);
        x2r_cncl.setVisibility(View.INVISIBLE);
        x3r_cncl.setVisibility(View.INVISIBLE);
        x4r_cncl.setVisibility(View.INVISIBLE);
    }
    public void checkPermission(String[] permission, int requestCode){
        //if (ContextCompat.checkSelfPermission(dosing.this, permission) == PackageManager.PERMISSION_DENIED) {
        ActivityCompat.requestPermissions(dosing.this, permission , requestCode);
    }
    public class DefaultExceptionHandler implements Thread.UncaughtExceptionHandler {

        private Thread.UncaughtExceptionHandler defaultUEH;
        Activity activity;

        public DefaultExceptionHandler(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {

            Intent intent = new Intent(activity, dosing.class);

            intent.putExtra("crash",true);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);

            @SuppressLint("WrongConstant") PendingIntent pendingIntent = PendingIntent.getActivity(
                    activity.getBaseContext(), 0, intent, intent.getFlags());

            //Following code will restart your application after 0.5 seconds
            AlarmManager mgr = (AlarmManager) activity.getBaseContext()
                    .getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500,
                    pendingIntent);

            //This will finish your activity manually
            activity.finish();

            //This will stop your application and take out from it.
            System.exit(2);

        }

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        checkPermission(REQUIRED_SDK_PERMISSIONS,1);
        home = Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.menar.milkdoser";
        File folder = new File(home);
        File logo = new File(home + "/logo");
        File parameters = new File(home + "/parameters");
        boolean success = true;
        Log.d("Location", "Home:" + home);
        Log.d("Location", "Logo:" + logo);
        Log.d("Location", "Parameters:" + parameters);
        if (!folder.exists()) {
            //Toast.makeText(MainActivity.this, "Directory Does Not Exist, Create It", Toast.LENGTH_SHORT).show();
            success = folder.mkdir();
        }
        if (!logo.exists() && success) {
            //Toast.makeText(MainActivity.this, "Directory Does Not Exist, Create It", Toast.LENGTH_SHORT).show();
            success = logo.mkdir();
        }
        if (!parameters.exists() && success) {
            //Toast.makeText(MainActivity.this, "Directory Does Not Exist, Create It", Toast.LENGTH_SHORT).show();
            success = parameters.mkdir();
        }
        if (success) {
            Toast.makeText(dosing.this, "Directory Created", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(dosing.this, "Failed - Error", Toast.LENGTH_SHORT).show();
        }
        copyFile();
        int currentApiVersion = Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(flags);
                }
            });
        }
        /////////////////
        sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        Configuration conf = getResources().getConfiguration();
        conf.locale = new Locale(sharedPref.getString("running_lang","tr")); //french language locale
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        resources = new Resources(getAssets(), metrics, conf);
        ////////////////
        init_params();
        if (mono_mode) setContentView(R.layout.activity_dosing_mono);
        else setContentView(R.layout.activity_dosing);
        mHandler = new RxHandler(this);
        initviews();
        menu_change = false;
        device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (!Objects.equals(device_id, sharedPref.getString("saved_device_id", ""))) {
            Log.d("Android", "Android ID : Kay??tl?? de??il >> " + device_id);
        }
        if (!sharedPref.getBoolean("power_flag", false)) {
            save_using_events("A", "power_on");
            editor.putBoolean("power_flag", true);
        }
        wash_button_pressed = getIntent().getBooleanExtra("wash_button", false);
        rinse_button_pressed = getIntent().getBooleanExtra("rinse_button", false);
        if(l_wash_complete_flag)
        {
            l_wash_prosedure = true;
            show_wash_screen("left");
        }
        if(r_wash_complete_flag)
        {
            if(!mono_mode) {
                r_wash_prosedure = true;
                show_wash_screen("right");
            }
        }
        Log.d("Wash button pressed", ": " + wash_button_pressed);
        if (rinse_button_pressed) {

            Log.d("Rinse:", "ok");
            t_rinse_cnt_l = T_RINSE_TRIG;
            t_rinse_cnt_r = T_RINSE_TRIG;
            l_rinse.setImageResource(R.drawable.rinse);
            r_rinse.setImageResource(R.drawable.rinse);
        }
        if (wash_button_pressed) {
            Log.d("Wash:", "ok");
            l_wash_prosedure = true;
            show_wash_screen("left");
            if(!mono_mode) {
                r_wash_prosedure = true;
                show_wash_screen("right");
            }
        }
        r_cupbar_setlevel(0);
        l_cupbar_setlevel(0);
        timer = new Timer();
        rinse = new Timer();
        wash = new Timer();
        time_tmr = new Timer();
        l_wash_cnt = new Timer();
        l_wash_cnt.schedule(l_wash_counter, 0, 1000);
        r_wash_cnt = new Timer();
        r_wash_cnt.schedule(r_wash_counter, 0, 1000);
        r_rinse_cnt = new Timer();
        r_rinse_cnt.schedule(r_rinse_counter, 0, 1000);
        l_rinse_cnt = new Timer();
        l_rinse_cnt.schedule(l_rinse_counter, 0, 1000);
        timer.schedule(timerTask, 0, 50);
        rinse.schedule(rinse_timer_task, 200, 60000);
        wash.schedule(wash_timer_task, 200, 60000);
        time_tmr.schedule(time_timer_task, 0, 750);
        connection_timer.schedule(connection_timer_task, 0, 500);
        ctx = this;
        context = getApplicationContext();
        spd_update = true;
        //preventStatusBarExpansion(context);
        //Toast.makeText(getApplicationContext(),"dosing activity",Toast.LENGTH_LONG).show();
    }
    private void copyFile() {
        try {
            File sd = new File(home+"/parameters");
            File data = Environment.getDataDirectory();
            Log.d("Location","Data:"+data);
            if (sd.canWrite()) {
                String backupDBPath = "parameters.xml";
                File currentDB = new File(data+"/data/com.menar.milkdoser/shared_prefs/sharedPref.xml");
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void save_using_events(String cleanig_event_channel, String cleanig_event_type) {
        int using_event_count;
        using_event_count=sharedPref.getInt("using_event_cnt",0);
        using_event_count++;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
        String formattedDate = df.format(c.getTime());
        String formattedTime = tf.format(c.getTime());
        editor.putInt("using_event_cnt",using_event_count);
        editor.putString("using_event"+using_event_count+"channel", cleanig_event_channel);
        editor.putString("using_event"+using_event_count+"type", cleanig_event_type);
        editor.putString("using_event"+using_event_count+"time", formattedTime);
        editor.putString("using_event"+using_event_count+"date", formattedDate);
        editor.commit();
    }
    public void save_cleanig_events(String cleanig_event_channel, String cleanig_event_type) {
     int cleaning_event_count;
        cleaning_event_count=sharedPref.getInt("cleaning_event_cnt",0);
        cleaning_event_count++;
        if(cleaning_event_count>5)
        {
            for(int i=1;i<=500;i++)
            {
                editor.putString("cleaning_event"+(i-1)+"channel", sharedPref.getString("cleaning_event"+(i)+"channel",""));
                editor.putString("cleaning_event"+(i-1)+"type", sharedPref.getString("cleaning_event"+(i)+"type",""));
                editor.putString("cleaning_event"+(i-1)+"time", sharedPref.getString("cleaning_event"+(i)+"time",""));
                editor.putString("cleaning_event"+(i-1)+"date", sharedPref.getString("cleaning_event"+(i)+"date",""));
            }
            cleaning_event_count=500;
        }
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
        String formattedDate = df.format(c.getTime());
        String formattedTime = tf.format(c.getTime());
        editor.putInt("cleaning_event_cnt",cleaning_event_count);
        editor.putString("cleaning_event"+cleaning_event_count%6+"channel", cleanig_event_channel);
        editor.putString("cleaning_event"+cleaning_event_count%6+"type", cleanig_event_type);
        editor.putString("cleaning_event"+cleaning_event_count%6+"time", formattedTime);
        editor.putString("cleaning_event"+cleaning_event_count%6+"date", formattedDate);
        editor.commit();
    }
    public void save_error_events(String error_event_channel, String error_event_type) {
        int error_event_count;
        error_event_count=sharedPref.getInt("error_event_cnt",0);
        error_event_count++;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
        String formattedDate = df.format(c.getTime());
        String formattedTime = tf.format(c.getTime());
        editor.putInt("error_event_cnt",error_event_count);
        editor.putString("error_event"+error_event_count+"channel", error_event_channel);
        editor.putString("error_event"+error_event_count+"type", error_event_type);
        editor.putString("error_event"+error_event_count+"time", formattedTime);
        editor.putString("error_event"+error_event_count+"date", formattedDate);
        editor.commit();
    }
    public void reset_l_button() {
        x1l_cncl.setVisibility(View.INVISIBLE);
        x2l_cncl.setVisibility(View.INVISIBLE);
        x3l_cncl.setVisibility(View.INVISIBLE);
        x4l_cncl.setVisibility(View.INVISIBLE);
        params = (ConstraintLayout.LayoutParams) x1l_button.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        x1l_button.setLayoutParams(params);
        params = (ConstraintLayout.LayoutParams) x2l_button.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        x2l_button.setLayoutParams(params);
        params = (ConstraintLayout.LayoutParams) x3l_button.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        x3l_button.setLayoutParams(params);
        params = (ConstraintLayout.LayoutParams) x4l_button.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        x4l_button.setLayoutParams(params);
        l_cupbar_setlevel(0);
        if(dose_l_group==1)
        {
            x1l_button.setImageResource(R.drawable.hotcup1x);
            x2l_button.setImageResource(R.drawable.hotcup2x);
            x3l_button.setImageResource(R.drawable.hotcup3x);
            x4l_button.setImageResource(R.drawable.hotcup4x);
        }
        else if(dose_l_group==2)
        {
            x1l_button.setImageResource(R.drawable.icecup1x);
            x2l_button.setImageResource(R.drawable.icecup2x);
            x3l_button.setImageResource(R.drawable.icecup3x);
            x4l_button.setImageResource(R.drawable.icecup4x);
        }
        else if(dose_l_group==3)
        {
            x1l_button.setImageResource(R.drawable.coldcup1x);
            x2l_button.setImageResource(R.drawable.coldcup2x);
            x3l_button.setImageResource(R.drawable.coldcup3x);
            x4l_button.setImageResource(R.drawable.coldcup4x);
        }
        left_dosing=false;
    }
    public void reset_r_button() {
        x1r_cncl.setVisibility(View.INVISIBLE);
        x2r_cncl.setVisibility(View.INVISIBLE);
        x3r_cncl.setVisibility(View.INVISIBLE);
        x4r_cncl.setVisibility(View.INVISIBLE);
        params = (ConstraintLayout.LayoutParams) x1r_button.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        x1r_button.setLayoutParams(params);
        params = (ConstraintLayout.LayoutParams) x2r_button.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        x2r_button.setLayoutParams(params);
        params = (ConstraintLayout.LayoutParams) x3r_button.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        x3r_button.setLayoutParams(params);
        params = (ConstraintLayout.LayoutParams) x4r_button.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        x4r_button.setLayoutParams(params);
        r_cupbar_setlevel(0);
        if(dose_r_group==1)
        {
            x1r_button.setImageResource(R.drawable.hotcup1x);
            x2r_button.setImageResource(R.drawable.hotcup2x);
            x3r_button.setImageResource(R.drawable.hotcup3x);
            x4r_button.setImageResource(R.drawable.hotcup4x);
        }
        else if(dose_r_group==2)
        {
            x1r_button.setImageResource(R.drawable.icecup1x);
            x2r_button.setImageResource(R.drawable.icecup2x);
            x3r_button.setImageResource(R.drawable.icecup3x);
            x4r_button.setImageResource(R.drawable.icecup4x);
        }
        else if(dose_r_group==3)
        {
            x1r_button.setImageResource(R.drawable.coldcup1x);
            x2r_button.setImageResource(R.drawable.coldcup2x);
            x3r_button.setImageResource(R.drawable.coldcup3x);
            x4r_button.setImageResource(R.drawable.coldcup4x);
        }
        right_dosing=false;
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

    public static class customViewGroup extends ViewGroup {

        public customViewGroup(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            Log.v("customViewGroup", "**********Intercepted");
            return true;
        }
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
    final TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            send_data(query_buf);
        }
    };
    final TimerTask connection_timer_task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(() -> {
                if(packet_received==false)connection.setImageResource(R.drawable.disconnected);
                if(packet_received==true)connection.setImageResource(R.drawable.connected);
                packet_received=false;
                if(connection.getVisibility()==View.VISIBLE)
                {
                    connection.setVisibility(View.INVISIBLE);
                }
                else if(connection.getVisibility()==View.INVISIBLE)
                {
                    connection.setVisibility(View.VISIBLE);
                }
            });
        }
    };
    final TimerTask l_wash_counter = new TimerTask() {
        @Override
        public void run() {
            if(l_wash_flag)
            {
                if(l_detergent)
                {
                    Log.d("L_wash","wash_take="+l_detergent_cnt);
                    l_detergent_cnt++;
                    if(l_detergent_cnt>=WASH_TAKE)
                    {
                        l_cmd=6;
                        l_detergent=false;
                        l_detergent_cnt=0;
                        l_wait=true;
                        l_wash++;
                        Log.d("L_wash","wash_cycle="+l_wash);
                    }

                }
                if(l_wait)
                {
                    l_wait_cnt++;
                    Log.d("L_wash","wash_dwell="+l_wait_cnt);
                    if(l_wait_cnt>=WASH_DWELL)
                    {
                        if(l_wash>=WASH_CYCLE)
                        {
                            Log.d("L_wash","Y??kama bitti");
                            l_wash_flag=false;
                            l_wash_prosedure=false;
                            l_rinse_after_wash=true;

                            runOnUiThread(() -> {
                                l_att.setText("Temizleme haznesini y??kay??n Temizleme haznesine \n10lt su doldurun Durulamay?? ba??lat??n");
                                hide_wash_screen("left");
                                show_rinse_screen("left");
                            });
                        }

                        else
                        {
                            l_cmd=5;
                            l_wait=false;
                            l_wait_cnt=0;
                            l_detergent=true;
                            runOnUiThread(() -> l_rinse_txt.setText(resources.getString(R.string.yikama_islemi)+(l_wash+1)+"/"+WASH_CYCLE));
                        }

                    }

                }
            }
        }
    };
    final TimerTask r_wash_counter = new TimerTask() {
        @Override
        public void run() {
            if(r_wash_flag)
            {
                if(r_detergent)
                {
                    Log.d("R_wash","wash_take="+r_detergent_cnt);
                    r_detergent_cnt++;
                    if(r_detergent_cnt>=WASH_TAKE)
                    {
                        r_cmd=6;
                        r_detergent=false;
                        r_detergent_cnt=0;
                        r_wait=true;
                        r_wash++;
                        Log.d("R_wash","wash_cycle="+r_wash);

                    }

                }
                if(r_wait)
                {
                    r_wait_cnt++;
                    Log.d("R_wash","wash_dwell="+r_wait_cnt);
                    if(r_wait_cnt>=WASH_DWELL)
                    {


                        if(r_wash>=WASH_CYCLE)
                        {
                            Log.d("R_wash","Y??kama bitti");
                            r_wash_flag=false;
                            r_wash_prosedure=false;
                            r_rinse_after_wash=true;
                            runOnUiThread(() -> {
                                r_att.setText("Temizleme haznesini y??kay??n Temizleme haznesine \n10lt su doldurun Durulamay?? ba??lat??n");
                                hide_wash_screen("right");
                                show_rinse_screen("right");
                            });
                        }
                        else {
                            r_cmd = 5;
                            r_wait = false;
                            r_wait_cnt = 0;
                            r_detergent = true;
                            runOnUiThread(() -> r_rinse_txt.setText(resources.getString(R.string.yikama_islemi) + (r_wash + 1) + "/" + WASH_CYCLE));
                        }

                    }

                }
            }

        }
    };
    final TimerTask l_rinse_counter = new TimerTask() {
        @Override
        public void run() {
            if (t_rinse_cnt_l_flag) {
                t_rinse_l++;
                if (t_rinse_l > l_rinse_time) {
                    l_cmd = 8;
                    t_rinse_cnt_l_flag=false;
                    t_rinse_l = 0;
                    t_rinse_cnt_l=0;
                    runOnUiThread(() -> {
                        hide_rinse_screen("left");
                        l_rinse.setImageResource(R.drawable.rinse);
                        if(!t_rinse_cnt_r_flag)settings_button.setVisibility(View.VISIBLE);
                    });
                    a_sweep_timeout_flag=true;
                    if(l_rinse_after_wash)editor.putBoolean("l_wash_flag",false);editor.commit();
                    l_rinse_after_wash=false;
                }
                runOnUiThread(() -> l_rinse_txt.setText(resources.getString(R.string.durulamanin_bitmesine)+ (l_rinse_time-t_rinse_l) + resources.getString(R.string.saniye_kaldi)));
            }
            else if(a_sweep_timeout_flag)
            {
                a_sweep_cnt++;
                if(a_sweep_cnt>1)
                {
                    a_sweep_flag=true;
                    a_sweep_timeout_flag=false;
                    dose_l_set=ENCSET_A_SWEEP;
                    l_cmd=9;
                }
            }
        }
    };
    final TimerTask r_rinse_counter = new TimerTask() {
        @Override
        public void run() {
            if (t_rinse_cnt_r_flag) {
                t_rinse_r++;
                if (t_rinse_r > r_rinse_time) {
                    r_cmd = 8;
                    t_rinse_cnt_r_flag=false;
                    t_rinse_r = 0;
                    t_rinse_cnt_r=0;
                    runOnUiThread(() -> {
                        hide_rinse_screen("right");
                        r_rinse.setImageResource(R.drawable.rinse);
                        if(!t_rinse_cnt_l_flag)settings_button.setVisibility(View.VISIBLE);
                    });
                    if(r_rinse_after_wash)editor.putBoolean("r_wash_flag",false);editor.commit();
                    b_sweep_timeout_flag=true;
                    r_rinse_after_wash=false;
                }
                runOnUiThread(() -> r_rinse_txt.setText(resources.getString(R.string.durulamanin_bitmesine)+ (r_rinse_time-t_rinse_r) + resources.getString(R.string.saniye_kaldi)));
            }
            else if(b_sweep_timeout_flag)
            {
                b_sweep_cnt++;
                if(b_sweep_cnt>1)
                {
                    b_sweep_flag=true;
                    b_sweep_timeout_flag=false;
                    dose_r_set=ENCSET_B_SWEEP;
                    r_cmd=9;
                }
            }
        }
    };
    final TimerTask wash_timer_task = new TimerTask() {
        @Override
        public void run() {
            t_wash_cnt++;
        }
    };
    final TimerTask rinse_timer_task = new TimerTask() {
        @Override
        public void run() {
            if(!t_rinse_cnt_l_flag)t_rinse_cnt_l++;
            if(!t_rinse_cnt_r_flag)t_rinse_cnt_r++;
            if((t_rinse_cnt_l>T_RINSE_TRIG)&&(!t_rinse_cnt_l_flag))
            {
                if(!no_rinse_t) {
                    runOnUiThread(() -> show_rinse_screen("left"));
                }
            }
            if((t_rinse_cnt_r>T_RINSE_TRIG)&&(!t_rinse_cnt_r_flag)&&(!mono_mode))
            {
                if(!no_rinse_t) {
                    runOnUiThread(() -> show_rinse_screen("right"));
                }
            }
        }
    };
    final TimerTask time_timer_task = new TimerTask() {
        @Override
        public void run() {
                runOnUiThread(() -> {
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
                    String formattedDate = df.format(c.getTime());
                    String formattedTime = tf.format(c.getTime());
                    if((hour==c.get(Calendar.HOUR_OF_DAY))&&(minute==c.get(Calendar.MINUTE)&&(1==c.get(Calendar.SECOND))))
                    {
                        editor.putBoolean("l_wash_flag",true);
                        editor.putBoolean("r_wash_flag",true);
                        editor.commit();
                        l_wash_prosedure = true;
                        r_wash_prosedure = true;
                        show_wash_screen("left");
                        if(!mono_mode) {
                            show_wash_screen("right");
                        }

                    }
                    date.setText(formattedDate);
                    time.setText(formattedTime);
                });
        }
    };
    public void show_wash_screen(String side) {
        hide_dose_items(side);
        show_wash_items(side);
    }
    public void hide_wash_screen(String side) {
        show_dose_items(side);
        hide_wash_items(side);
    }
    public void show_rinse_screen(String side) {
        hide_dose_items(side);
        show_rinse_items(side);
    }
    public void hide_rinse_screen(String side){
        show_dose_items(side);
        hide_rinse_items(side);
    }
    //
    // Hide-Show Wash items
    //
    public void hide_wash_items(String side) {
        if (side == "left") {
            l_rinse.setVisibility(View.INVISIBLE);
            start_l_w.setVisibility(View.INVISIBLE);
            respite_l.setVisibility(View.INVISIBLE);
            l_rinse_txt.setVisibility(View.INVISIBLE);
        }
        else if(side=="right"){
            r_rinse.setVisibility(View.INVISIBLE);
            start_r_w.setVisibility(View.INVISIBLE);
            respite_r.setVisibility(View.INVISIBLE);
            r_rinse_txt.setVisibility(View.INVISIBLE);
        }
        settings_button.setVisibility(View.VISIBLE);
    }
    public void show_wash_items(String side) {
        if (side == "left") {
            l_rinse.setImageResource(R.drawable.wash);
            l_rinse.setVisibility(View.VISIBLE);
            start_l_w.setVisibility(View.VISIBLE);
            l_att.setText("Temizlik haznesine 2lt 50-60 derece sicak su koyun,\n temizleme sol??syonu ilave edin y??kamay?? ba??lat??n");
            l_att.setVisibility(View.VISIBLE);
            //respite_l.setVisibility(View.VISIBLE);
        }
        else if(side=="right"){
            r_rinse.setImageResource(R.drawable.wash);
            r_rinse.setVisibility(View.VISIBLE);
            start_r_w.setVisibility(View.VISIBLE);
            r_att.setText("Temizlik haznesine 2lt 50-60 derece sicak su koyun,\n temizleme sol??syonu ilave edin y??kamay?? ba??lat??n");
            r_att.setVisibility(View.VISIBLE);
            //respite_r.setVisibility(View.VISIBLE);
        }
    }
    //
    // Hide-Show rinse items
    //
    public void hide_rinse_items(String side) {
        if (side == "left") {
            l_rinse.setVisibility(View.INVISIBLE);
            start_l.setVisibility(View.INVISIBLE);
            respite_l.setVisibility(View.INVISIBLE);
            l_rinse_txt.setVisibility(View.INVISIBLE);
            start_l_w.setVisibility(View.INVISIBLE);
        }
        else if(side=="right"){
            r_rinse.setVisibility(View.INVISIBLE);
            start_r.setVisibility(View.INVISIBLE);
            respite_r.setVisibility(View.INVISIBLE);
            r_rinse_txt.setVisibility(View.INVISIBLE);
            start_r_w.setVisibility(View.INVISIBLE);
        }
    }
    public void show_rinse_items(String side) {
        if (side == "left") {
            if(l_rinse_after_wash){
                l_att.setVisibility(View.VISIBLE);
                start_l_w.setVisibility(View.VISIBLE);
            }
            else {
                if (sharedPref.getBoolean("force_rinse_l", false))
                    start_l_w.setVisibility(View.VISIBLE);
                else {
                    start_l.setVisibility(View.VISIBLE);
                    respite_l.setVisibility(View.VISIBLE);
                }
            }
            l_rinse.setImageResource(R.drawable.rinse);
            l_rinse.setVisibility(View.VISIBLE);
        }
        else if(side=="right"){
            if(!mono_mode) {
                if(r_rinse_after_wash){
                    r_att.setVisibility(View.VISIBLE);
                    start_r_w.setVisibility(View.VISIBLE);
                }
                else {
                    if (sharedPref.getBoolean("force_rinse_r", false))
                        start_r_w.setVisibility(View.VISIBLE);
                    else {
                        start_r.setVisibility(View.VISIBLE);
                        respite_r.setVisibility(View.VISIBLE);
                    }
                }
                r_rinse.setImageResource(R.drawable.rinse);
                r_rinse.setVisibility(View.VISIBLE);
            }
        }
    }
    //
    // Hide-Show dose items
    //
    public void hide_dose_items(String side) {
        if (side == "left") {
            x1l_button.setVisibility(View.INVISIBLE);
            x2l_button.setVisibility(View.INVISIBLE);
            x3l_button.setVisibility(View.INVISIBLE);
            x4l_button.setVisibility(View.INVISIBLE);
            l_hot.setVisibility(View.INVISIBLE);
            l_ice.setVisibility(View.INVISIBLE);
            l_cold.setVisibility(View.INVISIBLE);
            cb1.setVisibility(View.INVISIBLE);
            manual_l.setVisibility(View.INVISIBLE);
        }
        else if(side=="right"){
            x1r_button.setVisibility(View.INVISIBLE);
            x2r_button.setVisibility(View.INVISIBLE);
            x3r_button.setVisibility(View.INVISIBLE);
            x4r_button.setVisibility(View.INVISIBLE);
            r_hot.setVisibility(View.INVISIBLE);
            r_ice.setVisibility(View.INVISIBLE);
            r_cold.setVisibility(View.INVISIBLE);
            cb2.setVisibility(View.INVISIBLE);
            manual_r.setVisibility(View.INVISIBLE);
        }
    }
    public void show_dose_items(String side) {
        if (side == "left") {
            x1l_button.setVisibility(View.VISIBLE);
            x2l_button.setVisibility(View.VISIBLE);
            x3l_button.setVisibility(View.VISIBLE);
            x4l_button.setVisibility(View.VISIBLE);
            l_hot.setVisibility(View.VISIBLE);
            l_ice.setVisibility(View.VISIBLE);
            l_cold.setVisibility(View.VISIBLE);
            cb1.setVisibility(View.VISIBLE);
            manual_l.setVisibility(View.VISIBLE);
        }
        else if(side=="right"){
            x1r_button.setVisibility(View.VISIBLE);
            x2r_button.setVisibility(View.VISIBLE);
            x3r_button.setVisibility(View.VISIBLE);
            x4r_button.setVisibility(View.VISIBLE);
            r_hot.setVisibility(View.VISIBLE);
            r_ice.setVisibility(View.VISIBLE);
            r_cold.setVisibility(View.VISIBLE);
            cb2.setVisibility(View.VISIBLE);
            manual_r.setVisibility(View.VISIBLE);
        }
    }
    long map(long x, long in_min, long in_max, long out_min, long out_max) {
        if(((in_max - in_min) + out_min)!=0) {
            return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
        }
        else return 0;
    }
    public void increase_counter(String channel) {
        if(channel=="left")
        {
            editor.putInt("a_count_val", (sharedPref.getInt("a_count_val",0)) + 1);
        }
        else if(channel=="right")
        {
            editor.putInt("b_count_val", (sharedPref.getInt("b_count_val",0)) + 1);
        }
        editor.commit();
    }
    public void increase_volume(String channel, long volume) {
        if(channel=="left")
        {
            editor.putInt("a_volume_val", (int) ((sharedPref.getInt("a_volume_val",0)) + volume));
        }
        else if(channel=="right")
        {
            editor.putInt("b_volume_val", (int) ((sharedPref.getInt("b_volume_val",0)) + volume));
        }
        editor.commit();
    }
    public void init_params() {
        ENCSET_A_11 = sharedPref.getInt("1a1_value", 100);
        ENCSET_A_12 = sharedPref.getInt("1a2_value", 200);
        ENCSET_A_13 = sharedPref.getInt("1a3_value", 300);
        ENCSET_A_14 = sharedPref.getInt("1a4_value", 400);
        ENCSET_A_21 = sharedPref.getInt("2a1_value", 125);
        ENCSET_A_22 = sharedPref.getInt("2a2_value", 225);
        ENCSET_A_23 = sharedPref.getInt("2a3_value", 325);
        ENCSET_A_24 = sharedPref.getInt("2a4_value", 425);
        ENCSET_A_31 = sharedPref.getInt("3a1_value", 150);
        ENCSET_A_32 = sharedPref.getInt("3a2_value", 250);
        ENCSET_A_33 = sharedPref.getInt("3a3_value", 350);
        ENCSET_A_34 = sharedPref.getInt("3a4_value", 450);

        ENCSET_B_11 = sharedPref.getInt("1b1_value", 100);
        ENCSET_B_12 = sharedPref.getInt("1b2_value", 200);
        ENCSET_B_13 = sharedPref.getInt("1b3_value", 300);
        ENCSET_B_14 = sharedPref.getInt("1b4_value", 400);
        ENCSET_B_21 = sharedPref.getInt("2b1_value", 125);
        ENCSET_B_22 = sharedPref.getInt("2b2_value", 225);
        ENCSET_B_23 = sharedPref.getInt("2b3_value", 325);
        ENCSET_B_24 = sharedPref.getInt("2b4_value", 425);
        ENCSET_B_31 = sharedPref.getInt("3b1_value", 150);
        ENCSET_B_32 = sharedPref.getInt("3b2_value", 250);
        ENCSET_B_33 = sharedPref.getInt("3b3_value", 350);
        ENCSET_B_34 = sharedPref.getInt("3b4_value", 450);

        ENCSET_PIPE_ADD = sharedPref.getInt("pipe_add_value", 2000);
        ENCSET_A_BACK = sharedPref.getInt("a_back_value", 50);
        ENCSET_B_BACK = sharedPref.getInt("b_back_value", 50);
        T_IDLE_BACK = sharedPref.getInt("idle_value", 250);

        T_RINSE_TRIG = sharedPref.getInt("t_rinse_trig_val", 60);
        N_RINSE_TRIG = sharedPref.getInt("n_rinse_trig_val", 45);
        if(T_RINSE_TRIG==0)no_rinse_t=true;
        if(N_RINSE_TRIG==0)no_rinse_n=true;
        T_RINSE = sharedPref.getInt("t_rinse_val", 6);
        ENCSET_A_SWEEP = sharedPref.getInt("a_sweep_val", 200);
        ENCSET_B_SWEEP = sharedPref.getInt("b_sweep_val", 200);

        WASH_TAKE = sharedPref.getInt("wash_take_val", 12);
        WASH_DWELL = sharedPref.getInt("wash_dwell_val", 20);
        WASH_RINSE = sharedPref.getInt("wash_rinse_val", 120);
        WASH_CYCLE = sharedPref.getInt("wash_cycle", 3);

        PWM_A = sharedPref.getInt("channel_a_pwm", 1023);
        PWM_B = sharedPref.getInt("channel_b_pwm", 1023);

        PPML_A = sharedPref.getFloat("ppml_a", (float) 3043.44);
        PPML_B = sharedPref.getFloat("ppml_b", (float) 3043.44);

        mono_mode = sharedPref.getBoolean("mono_mode", false);
        PUMP_LOCK=sharedPref.getBoolean("pump_lock",true);
        networkSSID=sharedPref.getString("wifi_name","");

        l_wash_complete_flag= sharedPref.getBoolean("l_wash_flag", false);
        r_wash_complete_flag= sharedPref.getBoolean("r_wash_flag", false);

        hour=sharedPref.getInt("wash_hour",17);
        minute=sharedPref.getInt("wash_minute",30);
    }
    public void back_click(View view) {
        super.onBackPressed();
    }
    public void settings_click(View view) {
        Intent i = new Intent(getApplicationContext(),pincode.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }
    public void l_cupbar_setlevel(int level) {
        cb1.getBackground().setLevel(100*level);
    }
    public void r_cupbar_setlevel(int level) {
        cb2.getBackground().setLevel(100*level);
    }
    public void send_data(char out_buf[]){
        char[] output_message;
        output_message = out_buf;

        if (usbService != null) {
            if (l_cmd != 0) {
                if (l_cmd > 0 && l_cmd < 5) {
                    if (utils.ml_to_pulse(dose_l_set,PPML_A,true) != 65535) {
                        l_buf[7] = (char) ((utils.ml_to_pulse(dose_l_set,PPML_A,true) & 0xFF00) >> 8);
                        l_buf[6] = (char) (utils.ml_to_pulse(dose_l_set,PPML_A,true) & 0xFF);
                        Log.d("Dose_Set > ","L="+ utils.ml_to_pulse(dose_l_set,PPML_A,true));
                        output_message = l_buf;
                        increase_counter("left");
                        increase_volume("left", dose_l_set);
                        t_rinse_cnt_l = 0;
                        left_dosing_counter = 0;
                        left_dosing=true;
                        Log.d("Left_cmd: ","l_buf");
                    } else {
                        runOnUiThread(dosing.this::reset_l_button);
                    }
                    l_cmd = 0;
                } else if (l_cmd == 5) {
                    output_message = l_start;
                    l_start_cmd_cnt++;
                    if(l_start_cmd_cnt>cmd_repeat_cnt) {
                        l_cmd = 0;
                        l_start_cmd_cnt=0;
                    }
                    t_rinse_cnt_l = 0;
                    Log.d("Left_cmd: ","l_start");
                } else if (l_cmd == 6) {
                    output_message = l_stop;
                    left_dosing_finish=true;
                    left_dosing=false;
                    Log.d("Left_cmd: ","l_stop");
                    l_stop_cmd_cnt++;
                    if(l_stop_cmd_cnt>cmd_repeat_cnt) {
                        l_cmd = 0;
                        l_stop_cmd_cnt=0;
                    }
                } else if (l_cmd == 7) {
                    output_message = l_rinse_start;
                    l_rinse_start_cmd_cnt++;
                    if(l_rinse_start_cmd_cnt>cmd_repeat_cnt) {
                        l_cmd = 0;
                        l_rinse_start_cmd_cnt=0;
                    }
                    Log.d("Left_cmd: ","l_rinse_start");
                } else if (l_cmd == 8) {
                    output_message = l_rinse_stop;
                    l_rinse_stop_cmd_cnt++;
                    if(l_rinse_stop_cmd_cnt>cmd_repeat_cnt) {
                        l_cmd = 0;
                        l_rinse_stop_cmd_cnt=0;
                    }
                    Log.d("Left_cmd: ","l_rinse_stop");
                } else if (l_cmd == 9) {
                    l_buf[7] = (char) ((utils.ml_to_pulse(dose_l_set,PPML_A,true) & 0xFF00) >> 8);
                    l_buf[6] = (char) (utils.ml_to_pulse(dose_l_set,PPML_A,true) & 0xFF);
                    output_message = l_buf;
                    l_cmd = 0;
                    Log.d("Left_cmd: ","l_buf");
                }else if (l_cmd == 10) {
                    output_message = l_valve_reset;
                    l_cmd = 0;
                    Log.d("Left_cmd: ","l_valve_rst");
                }else {
                    output_message = query_buf;
                    l_cmd = 0;
                }
            }
            else if (r_cmd != 0) {
                if (r_cmd > 0 && r_cmd < 5) {

                    if (utils.ml_to_pulse(dose_r_set,PPML_B,true) != 65535) {
                        r_buf[5] = (char) ((utils.ml_to_pulse(dose_r_set,PPML_B,true) & 0xFF00) >> 8);
                        r_buf[4] = (char) (utils.ml_to_pulse(dose_r_set,PPML_B,true) & 0xFF);
                        Log.d("Dose_Set > ","R="+ utils.ml_to_pulse(dose_r_set,PPML_B,true));
                        output_message = r_buf;
                        increase_counter("right");
                        increase_volume("right", dose_r_set);
                        t_rinse_cnt_r = 0;
                        right_dosing_counter = 0;
                        right_dosing=true;
                        Log.d("Right_cmd: ","r_buf");
                        V1=true;
                    } else {
                        runOnUiThread(dosing.this::reset_r_button);
                    }
                    r_cmd = 0;
                } else if (r_cmd == 5) {
                    output_message = r_start;
                    r_start_cmd_cnt++;
                    if(r_start_cmd_cnt>cmd_repeat_cnt) {
                        r_cmd = 0;
                        r_start_cmd_cnt=0;
                    }
                    t_rinse_cnt_r = 0;
                    Log.d("Right_cmd: ","r_start");
                } else if (r_cmd == 6) {
                    output_message = r_stop;
                    right_dosing_finish=true;
                    right_dosing=false;
                    r_stop_cmd_cnt++;
                    if(r_stop_cmd_cnt>cmd_repeat_cnt) {
                        r_cmd = 0;
                        r_stop_cmd_cnt=0;
                    }
                    Log.d("Right_cmd: ","r_stop");
                } else if (r_cmd == 7) {
                    output_message = r_rinse_start;
                    r_rinse_start_cmd_cnt++;
                    if(r_rinse_start_cmd_cnt>cmd_repeat_cnt) {
                        r_cmd = 0;
                        r_rinse_start_cmd_cnt=0;
                    }
                    Log.d("Right_cmd: ","r_rinse_start");
                } else if (r_cmd == 8) {
                    output_message = r_rinse_stop;
                    r_rinse_stop_cmd_cnt++;
                    if(r_rinse_stop_cmd_cnt>cmd_repeat_cnt) {
                        r_cmd = 0;
                        r_rinse_stop_cmd_cnt=0;
                    }
                    Log.d("Right_cmd: ", "r_rinse_stop");
                } else if (r_cmd == 9) {
                    r_buf[5] = (char) ((utils.ml_to_pulse(dose_r_set,PPML_B,true) & 0xFF00) >> 8);
                    r_buf[4] = (char) (utils.ml_to_pulse(dose_r_set,PPML_B,true) & 0xFF);
                    output_message = r_buf;
                    r_cmd = 0;
                    Log.d("Right_cmd: ","r_buf");
                }else if (r_cmd == 10) {
                    output_message = r_valve_reset;
                    r_cmd = 0;
                    Log.d("Right_cmd: ","r_valve_rst");
                } else {
                    output_message = query_buf;
                    r_cmd = 0;
                    Log.d("Right_cmd: ","r_buf");
                }
            }
            else if(spd_update) {
                speed_set[5] = (char) ((PWM_B & 0xFF00) >> 8);
                speed_set[4] = (char) (PWM_B & 0xFF);

                speed_set[7] = (char) ((PWM_A & 0xFF00) >> 8);
                speed_set[6] = (char) (PWM_A & 0xFF);

                output_message = speed_set;
                spd_update=false;
            }
            int checksum = 0;
            for (int i = 0; i < 8; i++) {
                checksum += output_message[i];
            }
            output_message[8] = (char) (checksum & 0xFF);
            for(int i =0 ; i< output_message.length;i++)            {
                tx_buffer[i]= (byte)(output_message[i]);
            }
            if((output_message!=query_buf) || true) {
                usbService.write(tx_buffer);
            }
        } else {
            Log.d("write error", "write error");
        }

    }
    public void parse_data(char in_buf[]) {
        final String message = new String(in_buf);

        int sum = 0;
        for (int i = 0; i < 7; i++) {
            sum += in_buf[i];
        }
        Log.d("Received", "" + Integer.toHexString(in_buf[0]) + "-" + Integer.toHexString(in_buf[1]) + "-" + Integer.toHexString(in_buf[2]) + "-" + Integer.toHexString(in_buf[3]) + "-" + Integer.toHexString(in_buf[4]) + "-" + Integer.toHexString(in_buf[5]) + "-" + Integer.toHexString(in_buf[6]) + "-" + Integer.toHexString(in_buf[7]));
        sum = sum & 0xFF;
        if (in_buf[7] == sum) {
            runOnUiThread(() -> {
                int io_stat=in_buf[1];
                int MA_F=io_stat&0x01;
                int MA_R=io_stat&0x02;
                int MB_F=io_stat&0x04;
                int MB_R=io_stat&0x08;
                int encoder_l_value_H=in_buf[6];
                int encoder_l_value_L=in_buf[5];
                long encoder_l_value=0;
                encoder_l_value=encoder_l_value_H<<8;
                encoder_l_value=encoder_l_value + encoder_l_value_L;
                if(left_dosing){
                    if(MB_F==0){
                        left_dosing_finish=true;
                    }
                    else {
                        encoder_l_value = map(encoder_l_value, utils.ml_to_pulse(dose_l_set,PPML_A,true), 65535, 0, 100);
                        l_cupbar_setlevel((int) encoder_l_value);
                    }
                }
                if(left_dosing_finish){
                    left_dosing_finish=false;
                    left_dosing_counter++;
                    if(left_dosing_counter==5){
                        left_dosing_finish=true;
                        left_dosing_counter=0;
                        l_cmd=10;
                    }
                }
                if(left_dosing&&(encoder_l_value==encoder_l_value_last))
                {
                    Log.d("encoderror","left");
                    a_flow_timeout_cnt++;
                    if(a_flow_timeout_cnt>flow_timeout)
                    {
                        if(!l_manuel_dosing)l_cmd=6;
                        save_error_events("A","encoder_error");
                        left_dosing_finish=true;
                        a_flow_timeout_cnt=0;
                    }
                }
                else{
                    encoder_l_value_last=encoder_l_value;
                    a_flow_timeout_cnt=0;

                }
                if((left_dosing&&left_dosing_finish)&&!l_manuel_dosing){
                    if(a_sweep_flag)a_sweep_flag=false;
                    reset_l_button();
                    left_dosing_finish=false;
                    n_rinse_cnt_l++;
                    if(n_rinse_cnt_l>=N_RINSE_TRIG)
                    {
                        if(!no_rinse_n) {
                            show_rinse_screen("left");
                        }
                    }
                }
                int encoder_r_value_H=in_buf[4];
                int encoder_r_value_L=in_buf[3];
                long encoder_r_value=0;
                encoder_r_value=encoder_r_value_H<<8;
                encoder_r_value=encoder_r_value + encoder_r_value_L;
                if(right_dosing){
                    if(MA_F==0){
                        right_dosing_finish=true;
                    }
                    else {
                        encoder_r_value = map(encoder_r_value, utils.ml_to_pulse(dose_r_set,PPML_B,true), 65535, 0, 100);
                        //Log.d("Progressbar left ",": "+encoder_l_value);
                        r_cupbar_setlevel((int) encoder_r_value);
                    }
                }
                if(right_dosing_finish){
                    right_dosing_finish=false;
                    right_dosing_counter++;
                    if(right_dosing_counter==5){
                        right_dosing_finish=true;
                        right_dosing_counter=0;
                        r_cmd=10;
                    }
                }
                if(right_dosing&&(encoder_r_value==encoder_r_value_last))
                {
                    b_flow_timeout_cnt++;
                    if(b_flow_timeout_cnt>flow_timeout)
                    {
                        r_cmd=6;
                        save_error_events("B","encoder_error");
                        right_dosing_finish=true;
                        b_flow_timeout_cnt=0;
                    }
                }
                else{
                    encoder_r_value_last=encoder_r_value;
                    b_flow_timeout_cnt=0;
                }
                if(right_dosing&&right_dosing_finish){
                    Log.d("Views","R reset");
                    if(b_sweep_flag)b_sweep_flag=false;
                    reset_r_button();
                    right_dosing_finish=false;
                    n_rinse_cnt_l++;
                    if(n_rinse_cnt_r>=N_RINSE_TRIG)
                    {
                        if(!no_rinse_n) {
                            show_rinse_screen("right");
                        }
                    }
                }
            });
            //Log.d("checksum ok!", " >> " + Integer.toHexString(sum));
        }
        else {
            //Log.d("checksum fail!", " Correct checksum >> " + Integer.toHexString(sum) + " - Incoming checksum >> " + Integer.toHexString(in_buf[7]));
        }
        sum = 0;
    }
    public void onResume() {
        super.onResume();
        setFilters();
        startService(UsbService.class, usbConnection, null);
        Log.d("Activity","Resumed");
    }
    @Override
    public void onPause() {
        super.onPause();
        //unregisterReceiver(mUsbReceiver);
        //unbindService(usbConnection);
        Log.d("Activity","Paused");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer.purge();
        timer=null;
        Log.d("Activity","Destroyed");
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
    }
    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);
    }
    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        }
        Intent bindingIntent = new Intent(this, service);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
    private class RxHandler extends Handler {
        private final WeakReference<dosing> mActivity;

        public RxHandler(dosing activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbService.MESSAGE_FROM_SERIAL_PORT:
                    String data = (String) msg.obj;
                    Log.d("input",data);
                    break;
                case UsbService.CTS_CHANGE:
                    Toast.makeText(mActivity.get(), "CTS_CHANGE",Toast.LENGTH_LONG).show();
                    break;
                case UsbService.DSR_CHANGE:
                    Toast.makeText(mActivity.get(), "DSR_CHANGE",Toast.LENGTH_LONG).show();
                    break;
                case UsbService.SYNC_READ:
                    byte b[] = (byte[]) msg.obj;
                    char buffer[] = new char[b.length];
                    for(int i =0 ; i< b.length;i++)
                    {
                        buffer[i]= (char)(b[i] & 0xff);
                    }/*
                    runOnUiThread(() -> {
                        if(connection.getVisibility()==View.VISIBLE)
                        {
                            connection.setVisibility(View.INVISIBLE);
                        }
                        else if(connection.getVisibility()==View.INVISIBLE)
                        {
                            connection.setVisibility(View.VISIBLE);
                        }
                    });*/
                    packet_received=true;
                    parse_data(buffer);
                    //Log.d("Received", ":  " + Integer.toHexString(buffer[0]) + "-" + Integer.toHexString(buffer[1]) + "-" + Integer.toHexString(buffer[2]) + "-" + Integer.toHexString(buffer[3]) + "-" + Integer.toHexString(buffer[4]) + "-" + Integer.toHexString(buffer[5]) + "-" + Integer.toHexString(buffer[6]) + "-" + Integer.toHexString(buffer[7]));
                    break;
            }
        }
    }
}