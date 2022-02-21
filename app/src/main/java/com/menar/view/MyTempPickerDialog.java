package com.menar.view;

import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.menar.milkdoserhmi.R;
import com.menar.view.TempPicker.OnTempChangedListener;


public class MyTempPickerDialog extends AlertDialog implements OnClickListener,
        OnTempChangedListener {


    public interface OnTempSetListener {
        void onTempSet(TempPicker view, int hourOfDay, int minute, int seconds);
    }

    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";
    private static final String SECONDS = "seconds";
    private static final String IS_24_HOUR = "is24hour";
    
    private final TempPicker mTempPicker;
    private final OnTempSetListener mCallback;
    private final Calendar mCalendar;
    private final java.text.DateFormat mDateFormat;
        
    int mInitialHourOfDay;
    int mInitialMinute;
    int mInitialSeconds;    
    boolean mIs24HourView;

    public MyTempPickerDialog(Context context,
            OnTempSetListener callBack,
            int hourOfDay, int minute, int seconds, boolean is24HourView) {
    	
        this(context, 0,
                callBack, hourOfDay, minute, seconds, is24HourView);
    }

    public MyTempPickerDialog(Context context,
            int theme,
            OnTempSetListener callBack,
            int hourOfDay, int minute, int seconds, boolean is24HourView) {
        super(context, theme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCallback = callBack;
        mInitialHourOfDay = hourOfDay;
        mInitialMinute = minute;
        mInitialSeconds = seconds;
        if (mInitialSeconds<0)mInitialSeconds=0;
        if (mInitialSeconds>3)mInitialSeconds=3;
        mIs24HourView = is24HourView;

        mDateFormat = DateFormat.getTimeFormat(context);
        mCalendar = Calendar.getInstance();
        updateTitle(mInitialHourOfDay, mInitialMinute, mInitialSeconds);
        
        setButton(context.getText(R.string.time_set), this);
        setButton2(context.getText(R.string.cancel), (OnClickListener) null);
        
        LayoutInflater inflater = 
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.temp_picker_dialog, null);
        setView(view);
        mTempPicker = (TempPicker) view.findViewById(R.id.tempPicker);

        // initialize state
        mTempPicker.setCurrentHour(mInitialHourOfDay);
        mTempPicker.setCurrentMinute(mInitialMinute);
        mTempPicker.setCurrentSecond(mInitialSeconds);
        mTempPicker.setIs24HourView(mIs24HourView);
        mTempPicker.setOnTempChangedListener(this);
    }
    
    public void onClick(DialogInterface dialog, int which) {
        if (mCallback != null) {
            mTempPicker.clearFocus();
            mCallback.onTempSet(mTempPicker, mTempPicker.getCurrentHour(),
                    mTempPicker.getCurrentMinute(), mTempPicker.getCurrentSeconds());
        }
    }

    public void onTempChanged(TempPicker view, int hourOfDay, int minute, int seconds) {
        updateTitle(hourOfDay, minute, seconds);
    }
    
    public void updateTemp(int hourOfDay, int minutOfHour, int seconds) {
        mTempPicker.setCurrentHour(hourOfDay);
        mTempPicker.setCurrentMinute(minutOfHour);
        mTempPicker.setCurrentSecond(seconds);
    }
    
    private void updateTitle(int hour, int minute, int seconds) {
        setTitle( "Hedef sıcaklık: "+minute + "," + (seconds*25)+"°C");
    }
    
    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(HOUR, mTempPicker.getCurrentHour());
        state.putInt(MINUTE, mTempPicker.getCurrentMinute());
        state.putInt(SECONDS, mTempPicker.getCurrentSeconds());
        state.putBoolean(IS_24_HOUR, mTempPicker.is24HourView());
        return state;
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int hour = savedInstanceState.getInt(HOUR);
        int minute = savedInstanceState.getInt(MINUTE);
        int seconds = savedInstanceState.getInt(SECONDS);
        mTempPicker.setCurrentHour(hour);
        mTempPicker.setCurrentMinute(minute);
        mTempPicker.setCurrentSecond(seconds);
        mTempPicker.setIs24HourView(savedInstanceState.getBoolean(IS_24_HOUR));
        mTempPicker.setOnTempChangedListener(this);
        updateTitle(hour, minute, seconds);
    }
    
   
}
