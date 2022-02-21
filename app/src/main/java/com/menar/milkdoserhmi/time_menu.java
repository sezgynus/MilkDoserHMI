package com.menar.milkdoserhmi;

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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class time_menu extends AppCompatActivity {

    TextView dtview,tmview;
    Button time_button,date_button;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    DatePickerDialog datePickerDialog;
    Calendar c;
    SimpleDateFormat df;
    SimpleDateFormat tf;
    String formattedDate;
    String formattedTime;
    int year,month,dayOfMonth,hour,minute;
    @SuppressLint({"ClickableViewAccessibility", "SimpleDateFormat"})
    public void initviews(){
        dtview= findViewById(R.id.dateview);
        tmview= findViewById(R.id.timeview);
        time_button= findViewById(R.id.timebtn);
        date_button= findViewById(R.id.datebtn);
        tmview.setText(formattedTime);
        dtview.setText(formattedDate);
        date_button.setOnTouchListener((view, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                hour= c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);

                datePickerDialog = new DatePickerDialog(time_menu.this,
                        (datePicker, year_picker, month_picker, day_picker) -> {
                            c.set(year_picker,month_picker,day_picker,hour,minute);

                            utils.changeSystemTime(year_picker,(month_picker+1),day_picker,hour,minute);
                            df = new SimpleDateFormat("dd/MM/yyyy");
                            formattedDate=df.format(c.getTime());
                            dtview.setText(formattedDate);
                            hide_system_ui();
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
                hide_system_ui();
                Log.d("date","pressed");

            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {

            }
            return true;
        });
        time_button.setOnTouchListener((view, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                hour= c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);

                TimePickerDialog tpd = new TimePickerDialog(time_menu.this,
                        (view1, hourOfDay_picker, minute_picker) -> {
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
                        }, hour, minute, true);
                tpd.setButton(TimePickerDialog.BUTTON_POSITIVE, "Seç", tpd);
                tpd.setButton(TimePickerDialog.BUTTON_NEGATIVE, "İptal", tpd);
                tpd.show();
                hide_system_ui();
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {

            }
            return true;
        });
    }
    @SuppressLint("SimpleDateFormat")
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