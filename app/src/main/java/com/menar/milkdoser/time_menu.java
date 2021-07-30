package com.menar.milkdoser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class time_menu extends AppCompatActivity {

    TextView dtview,tmview;
    Button time_button,date_button;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private int currentApiVersion;
    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
    );
    DatePickerDialog datePickerDialog;
    Calendar c;
    SimpleDateFormat df;
    SimpleDateFormat tf;
    String formattedDate;
    String formattedTime;
    int year,month,dayOfMonth,hour,minute;
    public void initviews(){
        dtview=(TextView)findViewById(R.id.dateview);
        tmview=(TextView)findViewById(R.id.timeview);
        time_button=(Button)findViewById(R.id.timebtn);
        date_button=(Button)findViewById(R.id.datebtn);
        tmview.setText(formattedTime);
        dtview.setText(formattedDate);
        date_button.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View view, MotionEvent event) {

                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    c = Calendar.getInstance();
                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                    hour= c.get(Calendar.HOUR_OF_DAY);
                    minute = c.get(Calendar.MINUTE);

                    datePickerDialog = new DatePickerDialog(time_menu.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year_picker, int month_picker, int day_picker) {
                                    c.set(year_picker,month_picker,day_picker,hour,minute);

                                    utils.changeSystemTime(year_picker,(month_picker+1),day_picker,hour,minute);
                                    df = new SimpleDateFormat("dd/MM/yyyy");
                                    formattedDate=df.format(c.getTime());
                                    dtview.setText(formattedDate);
                                    hide_system_ui();
                                }
                            }, year, month, dayOfMonth);
                    datePickerDialog.show();
                    hide_system_ui();
                    Log.d("date","pressed");

                }
                else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {

                }
                return true;
            }
        });
        time_button.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {

                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    c = Calendar.getInstance();
                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                    hour= c.get(Calendar.HOUR_OF_DAY);
                    minute = c.get(Calendar.MINUTE);

                    TimePickerDialog tpd = new TimePickerDialog(time_menu.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay_picker, int minute_picker) {
                                    Log.d("Time","Hour:"+hourOfDay_picker+" Minute:"+minute_picker);
                                    AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                    c.set(year,month,dayOfMonth,hourOfDay_picker,minute_picker);
                                    utils.changeSystemTime(year,(month+1),dayOfMonth,hourOfDay_picker,minute_picker);
                                    tf = new SimpleDateFormat("HH:mm");
                                    formattedTime = tf.format(c.getTime());
                                    long timeStamp = c.getTimeInMillis();
                                    //am.setTime(timeStamp);
                                    tmview.setText(formattedTime);
                                    hide_system_ui();
                                }
                            }, hour, minute, true);
                    tpd.setButton(TimePickerDialog.BUTTON_POSITIVE, "Seç", tpd);
                    tpd.setButton(TimePickerDialog.BUTTON_NEGATIVE, "İptal", tpd);
                    tpd.show();
                    hide_system_ui();
                }
                else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {

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
        hide_system_ui();
        setContentView(R.layout.activity_time_menu);
        c = Calendar.getInstance();
        df = new SimpleDateFormat("dd/MM/yyyy");
        tf = new SimpleDateFormat("HH:mm");
        formattedDate = df.format(c.getTime());
        formattedTime = tf.format(c.getTime());
        initviews();
    }
    public void hide_system_ui()
    {
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