package com.menar.milkdoser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class cleaning_journal extends AppCompatActivity {

    TextView[] cleaning_lines=new TextView[5];
    TextView[] time_lines=new TextView[5];
    TextView[] date_lines=new TextView[5];
    ImageButton rst_cleaning_info;
    public class cleaning_event_table {
        public String cleaning_channel;
        public String cleaning_type;
        public String cleaning_time;
        public String cleaning_date;
    }
    cleaning_event_table[] cleaning_events;
    int event_count=0;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
    );

    public void initviews(){
        cleaning_lines[0]= findViewById(R.id.cleaning_line1);
        cleaning_lines[1]= findViewById(R.id.cleaning_line2);
        cleaning_lines[2]= findViewById(R.id.cleaning_line3);
        cleaning_lines[3]= findViewById(R.id.cleaning_line4);
        cleaning_lines[4]= findViewById(R.id.cleaning_line5);
        time_lines[0]= findViewById(R.id.time_line1);
        time_lines[1]= findViewById(R.id.time_line2);
        time_lines[2]= findViewById(R.id.time_line3);
        time_lines[3]= findViewById(R.id.time_line4);
        time_lines[4]= findViewById(R.id.time_line5);
        date_lines[0]= findViewById(R.id.date_line1);
        date_lines[1]= findViewById(R.id.date_line2);
        date_lines[2]= findViewById(R.id.date_line3);
        date_lines[3]= findViewById(R.id.date_line4);
        date_lines[4]= findViewById(R.id.date_line5);
        rst_cleaning_info= findViewById(R.id.reset_cleaning_info);

        rst_cleaning_info.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                editor.putInt("cleaning_event_cnt",0);
                editor.commit();
                Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.hijyen_gunluğu_sifirlandi),Toast.LENGTH_LONG).show();
                reset_list();
            }
            return true;
        });
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
        setContentView(R.layout.activity_cleaning_journal);
        initviews();
        get_saved_events();
        show_saved_events(0);
    }
    public void reset_list(){
        for(int i=0;i<5;i++) {
            cleaning_lines[i].setText("");
            time_lines[i].setText("");
            date_lines[i].setText("");
        }
    }
    public void show_saved_events(int event_index){
        int line_end=5;
        if(cleaning_events.length<5)line_end=cleaning_events.length;
        for(int i=0;i<line_end;i++)
        {
            if(cleaning_events[i]!=null) {
                switch (cleaning_events[cleaning_events.length - 1 - i + event_index].cleaning_type) {
                    case "rinse_start":
                        cleaning_lines[i].setText(getApplicationContext().getString(R.string.kanal) + cleaning_events[cleaning_events.length - 1 - i + event_index].cleaning_channel + getApplicationContext().getString(R.string.icin_ara_durulama_yapildi));
                        break;
                    case "rinse_respite":
                        cleaning_lines[i].setText(getApplicationContext().getString(R.string.kanal) + cleaning_events[cleaning_events.length - 1 - i + event_index].cleaning_channel + getApplicationContext().getString(R.string.icin_ara_durulama_ertelendi));
                        break;
                    case "wash":
                        cleaning_lines[i].setText(getApplicationContext().getString(R.string.kanal) + cleaning_events[cleaning_events.length - 1 - i + event_index].cleaning_channel + getApplicationContext().getString(R.string.icin_yikama_yapildi));
                        break;
                    default:

                        break;
                }
                time_lines[i].setText(cleaning_events[cleaning_events.length - 1 - i + event_index].cleaning_time);
                date_lines[i].setText(cleaning_events[cleaning_events.length - 1 - i + event_index].cleaning_date);
            }
        }
    }
    public void get_saved_events(){
        event_count=sharedPref.getInt("cleaning_event_cnt",0);
        cleaning_events = new cleaning_event_table[event_count];
        for(int i=0;i<event_count;i++)
        {
            cleaning_events[i]=new cleaning_event_table();
            cleaning_events[i].cleaning_channel=sharedPref.getString("cleaning_event"+(i+1)+"channel","");
            cleaning_events[i].cleaning_type=sharedPref.getString("cleaning_event"+(i+1)+"type","");
            cleaning_events[i].cleaning_time=sharedPref.getString("cleaning_event"+(i+1)+"time","");
            cleaning_events[i].cleaning_date=sharedPref.getString("cleaning_event"+(i+1)+"date","");
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