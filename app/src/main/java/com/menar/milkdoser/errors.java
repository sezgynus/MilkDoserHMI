package com.menar.milkdoser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class errors extends AppCompatActivity {

    TextView[] error_lines=new TextView[5];
    TextView[] time_lines=new TextView[5];
    TextView[] date_lines=new TextView[5];
    ImageButton rst_err_info;

    AlertDialog.Builder builder;
    AlertDialog dialog;

    public class error_event_table {
        public String error_channel;
        public String error_type;
        public String error_time;
        public String error_date;
    }
    error_event_table[] error_events;
    int event_count=0;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private int currentApiVersion;
    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
    );

    public void initviews(){
        error_lines[0]=(TextView)findViewById(R.id.error_error_line1);
        error_lines[1]=(TextView)findViewById(R.id.error_error_line2);
        error_lines[2]=(TextView)findViewById(R.id.error_error_line3);
        error_lines[3]=(TextView)findViewById(R.id.error_error_line4);
        error_lines[4]=(TextView)findViewById(R.id.error_error_line5);
        time_lines[0]=(TextView)findViewById(R.id.error_time_line1);
        time_lines[1]=(TextView)findViewById(R.id.error_time_line2);
        time_lines[2]=(TextView)findViewById(R.id.error_time_line3);
        time_lines[3]=(TextView)findViewById(R.id.error_time_line4);
        time_lines[4]=(TextView)findViewById(R.id.error_time_line5);
        date_lines[0]=(TextView)findViewById(R.id.error_date_line1);
        date_lines[1]=(TextView)findViewById(R.id.error_date_line2);
        date_lines[2]=(TextView)findViewById(R.id.error_date_line3);
        date_lines[3]=(TextView)findViewById(R.id.error_date_line4);
        date_lines[4]=(TextView)findViewById(R.id.error_date_line5);
        rst_err_info=(ImageButton)findViewById(R.id.reset_errors);

        builder = new AlertDialog.Builder(errors.this);
        builder.setTitle("Verileri Sıfırla?");
        builder.setMessage("Hata günlüğünü sıfırlamak istediğinize eminmisiniz?");
        builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                hide_system_ui();
            }
        });
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editor.putInt("error_event_cnt",0);
                editor.commit();
                Toast.makeText(getApplicationContext(),"Hata günlüğü sıfırlandı",Toast.LENGTH_LONG).show();
                reset_list();
                hide_system_ui();
            }
        });
        dialog=builder.setCancelable(false).create();

        rst_err_info.setOnTouchListener(new View.OnTouchListener() {
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
        setContentView(R.layout.activity_errors);
        initviews();
        get_saved_events();
        show_saved_events(0);
    }
    public void reset_list(){
        for(int i=0;i<5;i++) {
            error_lines[i].setText("");
            time_lines[i].setText("");
            date_lines[i].setText("");
        }
    }
    public void show_saved_events(int event_index){
        int line_end=5;
        if(error_events.length<5)line_end=error_events.length;
        for(int i=0;i<line_end;i++)
        {
            if(error_events[i]!=null) {
                if (error_events[error_events.length - 1 - i + event_index].error_type.equals("connection_error")) {
                    error_lines[i].setText("I/O kartı ile olan bağlantı kesildi!");
                } else if (error_events[error_events.length - 1 - i + event_index].error_type.equals("encoder_error")) {
                    error_lines[i].setText("Kanal-" + error_events[error_events.length - 1 - i + event_index].error_channel + " enkoder arızası.");
                } else if (error_events[error_events.length - 1 - i + event_index].error_type.equals("wash")) {
                    error_lines[i].setText("");
                } else {

                }
                time_lines[i].setText(error_events[error_events.length - 1 - i + event_index].error_time);
                date_lines[i].setText(error_events[error_events.length - 1 - i + event_index].error_date);
            }
        }
    }
    public void get_saved_events(){
        event_count=sharedPref.getInt("error_event_cnt",0);
        error_events = new error_event_table[event_count];
        for(int i=0;i<event_count;i++)
        {
            error_events[i]=new error_event_table();
            error_events[i].error_channel=sharedPref.getString("error_event"+(i+1)+"channel","");
            error_events[i].error_type=sharedPref.getString("error_event"+(i+1)+"type","");
            error_events[i].error_time=sharedPref.getString("error_event"+(i+1)+"time","");
            error_events[i].error_date=sharedPref.getString("error_event"+(i+1)+"date","");
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