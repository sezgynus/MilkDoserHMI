package com.menar.milkdoser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class using_info extends AppCompatActivity {

    TextView[] using_lines=new TextView[5];
    TextView[] time_lines=new TextView[5];
    TextView[] date_lines=new TextView[5];
    ImageButton rst_using_info;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    public class using_event_table {
        public String using_channel;
        public String using_type;
        public String using_time;
        public String using_date;
    }
    using_event_table[] using_events;
    int event_count=0;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private int currentApiVersion;
    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
    );

    public void initviews(){
        using_lines[0]=(TextView)findViewById(R.id.using_line1);
        using_lines[1]=(TextView)findViewById(R.id.using_line2);
        using_lines[2]=(TextView)findViewById(R.id.using_line3);
        using_lines[3]=(TextView)findViewById(R.id.using_line4);
        using_lines[4]=(TextView)findViewById(R.id.using_line5);
        time_lines[0]=(TextView)findViewById(R.id.using_time_line1);
        time_lines[1]=(TextView)findViewById(R.id.using_time_line2);
        time_lines[2]=(TextView)findViewById(R.id.using_time_line3);
        time_lines[3]=(TextView)findViewById(R.id.using_time_line4);
        time_lines[4]=(TextView)findViewById(R.id.using_time_line5);
        date_lines[0]=(TextView)findViewById(R.id.using_date_line1);
        date_lines[1]=(TextView)findViewById(R.id.using_date_line2);
        date_lines[2]=(TextView)findViewById(R.id.using_date_line3);
        date_lines[3]=(TextView)findViewById(R.id.using_date_line4);
        date_lines[4]=(TextView)findViewById(R.id.using_date_line5);

        rst_using_info=(ImageButton)findViewById(R.id.reset_using_info);

        builder = new AlertDialog.Builder(using_info.this);
        builder.setTitle("Verileri Sıfırla?");
        builder.setMessage("Makine kullanım verilerini sıfırlamak istediğinize eminmisiniz?");
        builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                hide_system_ui();
            }
        });
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editor.putInt("using_event_cnt",0);
                editor.commit();
                Toast.makeText(getApplicationContext(),"Makine kullanım bilgileri sıfırlandı",Toast.LENGTH_LONG).show();
                reset_list();
                hide_system_ui();
            }
        });
        dialog=builder.setCancelable(false).create();

        rst_using_info.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                    params.setMargins(8,0,8,0);
                    view.setLayoutParams(params);
                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                    params.setMargins(0,0,0,0);
                    view.setLayoutParams(params);
                    dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                    dialog.show();
                    dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                }
                return true;
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

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
        setContentView(R.layout.activity_using_info);
        initviews();
        get_saved_events();
        show_saved_events(0);
    }
    public void reset_list(){
        for(int i=0;i<5;i++) {
            using_lines[i].setText("");
            time_lines[i].setText("");
            date_lines[i].setText("");
        }
    }
    public void show_saved_events(int event_index){
        int line_end=5;
        if(using_events.length<5)line_end=using_events.length;
        for(int i=0;i<line_end;i++)
        {
            if(using_events[i]!=null) {
                if (using_events[using_events.length - 1 - i + event_index].using_type.equals("power_on")) {
                    using_lines[i].setText("Cihaz açıldı");
                } else if (using_events[using_events.length - 1 - i + event_index].using_type.equals("power_off")) {
                    using_lines[i].setText("Cihaz kapatıldı");
                } else if (using_events[using_events.length - 1 - i + event_index].using_type.equals("wash")) {
                    using_lines[i].setText("");
                } else {

                }
                time_lines[i].setText(using_events[using_events.length - 1 - i + event_index].using_time);
                date_lines[i].setText(using_events[using_events.length - 1 - i + event_index].using_date);
            }
        }
    }
    public void get_saved_events(){
        event_count=sharedPref.getInt("using_event_cnt",0);
        using_events = new using_event_table[event_count];
        for(int i=0;i<event_count;i++)
        {
            using_events[i]=new using_event_table();
            using_events[i].using_channel=sharedPref.getString("using_event"+(i+1)+"channel","");
            using_events[i].using_type=sharedPref.getString("using_event"+(i+1)+"type","");
            using_events[i].using_time=sharedPref.getString("using_event"+(i+1)+"time","");
            using_events[i].using_date=sharedPref.getString("using_event"+(i+1)+"date","");
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
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
            {

                @Override
                public void onSystemUiVisibilityChange(int visibility)
                {
                    if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                    {
                        hide_system_ui();
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }
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