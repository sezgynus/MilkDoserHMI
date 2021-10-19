package com.menar.milkdoser;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;

public final class utils {
    private utils(){

    }
    public static int pulse_to_ml(int pulse, float ppml, boolean conjugate)
    {
        int millis;
        if(conjugate)pulse=65535-pulse;
        millis=Math.round((pulse*100)/ppml);
        Log.d("Pulse > Mililitre","Mililitre: " + millis);
        Log.d("Pulse > Mililitre","Pulse : " + pulse);
        return millis;
    }
    public static int ml_to_pulse(int millis,float ppml, boolean conjugate)
    {
        int pulse;
        pulse=Math.round((millis*ppml)/100);
        Log.d("Mililitre > Pulse","Mililitre: " + millis);
        Log.d("Mililitre > Pulse","Pulse : " + pulse);
        if(conjugate)pulse=65535-pulse;
        return pulse;
    }
    public static void changeSystemTime(int year,int month,int day,int hour,int minute){
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            String syear,smonth,sday,shour,sminute;
            String command ;
            syear=Integer.toString(year);
            smonth=Integer.toString(month);
            sday=Integer.toString(day);
            shour=Integer.toString(hour);
            sminute=Integer.toString(minute);
            //if(syear.length()>2)syear="0"+syear;
            if(smonth.length()<2)smonth="0"+smonth;
            if(sday.length()<2)sday="0"+sday;
            if(shour.length()<2)shour="0"+shour;
            if(sminute.length()<2)sminute="0"+sminute;

            command = "date "+smonth+sday+shour+sminute+syear+".00\n";
            Log.d("command",command);
            os.writeBytes(command);
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
