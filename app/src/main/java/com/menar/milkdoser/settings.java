package com.menar.milkdoser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class settings extends AppCompatActivity {
    ImageButton btn1,btn2,btn3,next,previous;
    TextView txt1,txt2,txt3;
    ImageView dot_1,dot_2,dot_3,dot_4;
    Context ctx;
    Resources resources;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    int current_page=0;
    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
    );
    @SuppressLint("ClickableViewAccessibility")
    public void initviews(){
        btn1= findViewById(R.id.button1);
        btn2= findViewById(R.id.button2);
        btn3= findViewById(R.id.button3);
        txt1= findViewById(R.id.textView1);
        txt2= findViewById(R.id.textView2);
        txt3= findViewById(R.id.textView3);
        dot_1= findViewById(R.id.dot1);
        dot_2= findViewById(R.id.dot2);
        dot_3= findViewById(R.id.dot3);
        dot_4= findViewById(R.id.dot4);
        previous= findViewById(R.id.previous_page);
        next= findViewById(R.id.next_page);

        btn1.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
                if(current_page==0)
                {
                    btn1.setImageResource(R.drawable.brightness_selected);
                }
                else if(current_page==1)
                {
                    btn1.setImageResource(R.drawable.parameters_selected);
                }
                else if(current_page==2)
                {
                    btn1.setImageResource(R.drawable.contact_selected);
                }
                else if(current_page==3)
                {
                    btn1.setImageResource(R.drawable.error_selected);
                }
                else
                {

                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                if(current_page==0)
                {
                    btn1.setImageResource(R.drawable.brightness);
                    Intent i = new Intent(getApplicationContext(),brightness_screen.class);
                    startActivity(i);
                }
                else if(current_page==1)
                {
                    btn1.setImageResource(R.drawable.parameters);
                    Intent i = new Intent(getApplicationContext(),parameters1.class);
                    startActivity(i);
                }
                else if(current_page==2)
                {
                    btn1.setImageResource(R.drawable.contact);
                    Intent i = new Intent(getApplicationContext(),conctact_viewer.class);
                    startActivity(i);
                }
                else if(current_page==3)
                {
                    btn1.setImageResource(R.drawable.error);
                    Intent i = new Intent(getApplicationContext(),errors.class);
                    startActivity(i);
                }
                else
                {

                }
            }
            return true;
        });
        btn2.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
                if(current_page==0)
                {
                    btn2.setImageResource(R.drawable.lang_selected);
                }
                else if(current_page==1)
                {
                    btn2.setImageResource(R.drawable.dataview_selected);
                }
                else if(current_page==2)
                {
                    btn2.setImageResource(R.drawable.machine_info_selected);
                }
                else if(current_page==3)
                {
                    btn2.setImageResource(R.drawable.demo_selected);
                }
                else
                {

                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                if(current_page==0)
                {
                    btn2.setImageResource(R.drawable.lang);
                    Intent i = new Intent(getApplicationContext(),lang_screen.class);
                    startActivity(i);
                }
                else if(current_page==1)
                {
                    btn2.setImageResource(R.drawable.dataview);
                    Intent i = new Intent(getApplicationContext(),using_info.class);
                    startActivity(i);
                }
                else if(current_page==2) {
                    btn2.setImageResource(R.drawable.machine_info);
                    Intent i = new Intent(getApplicationContext(), machine_info.class);
                    startActivity(i);
                }
                else if(current_page==3)
                {
                    btn2.setImageResource(R.drawable.demo);
                }
                else
                {

                }
            }
            return true;
        });
        btn3.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
                if(current_page==0)
                {
                    btn3.setImageResource(R.drawable.time_selected);
                }
                else if(current_page==1)
                {
                    btn3.setImageResource(R.drawable.cleaning_journal_selected);
                }
                else if(current_page==2)
                {
                    btn3.setImageResource(R.drawable.ota_selected_bg);
                }
                else if(current_page==3)
                {

                }
                else
                {

                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                if(current_page==0)
                {
                    btn3.setImageResource(R.drawable.time);
                    Intent i = new Intent(getApplicationContext(), time_menu.class);
                    startActivity(i);
                }
                else if(current_page==1)
                {
                    btn3.setImageResource(R.drawable.cleaning_journal);
                    Intent i = new Intent(getApplicationContext(), cleaning_journal.class);
                    startActivity(i);
                }
                else if(current_page==2)
                {
                    btn3.setImageResource(R.drawable.ota);
                    Intent i = new Intent(getApplicationContext(),ota_screen.class);
                    startActivity(i);
                }
                else if(current_page==3)
                {

                }
                else
                {

                }
            }
            return true;
        });
        previous.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                current_page--;
                if(current_page<0)current_page=0;
                setpageviews();
            }
            return true;
        });
        next.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                current_page++;
                if(current_page>3)current_page=3;
                setpageviews();
            }
            return true;
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
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
        setContentView(R.layout.activity_settings);
        /////////////////
        Configuration conf = getResources().getConfiguration();
        conf.locale = new Locale(sharedPref.getString("running_lang","tr")); //french language locale
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        resources = new Resources(getAssets(), metrics, conf);
        ////////////////
        initviews();
        setpageviews();
        ctx=this;
        //preventStatusBarExpansion(ctx);
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



    public static class CustomViewGroup extends ViewGroup {
        public CustomViewGroup(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            // Intercepted touch!
            return true;
        }
    }

    @SuppressLint("SetTextI18n")
    public void setpageviews()
    {
        if(current_page==0)
        {
            btn1.setVisibility(View.VISIBLE);
            txt1.setVisibility(View.VISIBLE);
            btn1.setImageResource(R.drawable.brightness);
            txt1.setText(resources.getString(R.string.ekran_parlakl));
            btn2.setVisibility(View.VISIBLE);
            txt2.setVisibility(View.VISIBLE);
            btn2.setImageResource(R.drawable.lang);
            txt2.setText(resources.getString(R.string.dil));
            btn3.setVisibility(View.VISIBLE);
            txt3.setVisibility(View.VISIBLE);
            btn3.setImageResource(R.drawable.time);
            txt3.setText(resources.getString(R.string.tarih_saat));
            previous.setVisibility(View.INVISIBLE);
            dot_1.setImageResource(R.drawable.dot_selected);
            dot_2.setImageResource(R.drawable.dot);
            dot_3.setImageResource(R.drawable.dot);
            dot_4.setImageResource(R.drawable.dot);
        }
        else if(current_page==1)
        {
            btn1.setVisibility(View.VISIBLE);
            txt1.setVisibility(View.VISIBLE);
            btn1.setImageResource(R.drawable.parameters);
            txt1.setText(resources.getString(R.string.parametereler));
            btn2.setVisibility(View.VISIBLE);
            txt2.setVisibility(View.VISIBLE);
            btn2.setImageResource(R.drawable.dataview);
            txt2.setText(resources.getString(R.string.makine_kullan_m_verileri));
            btn3.setVisibility(View.VISIBLE);
            txt3.setVisibility(View.VISIBLE);
            btn3.setImageResource(R.drawable.cleaning_journal);
            txt3.setText("Hijyen Günlüğü");
            previous.setVisibility(View.VISIBLE);
            dot_1.setImageResource(R.drawable.dot);
            dot_2.setImageResource(R.drawable.dot_selected);
            dot_3.setImageResource(R.drawable.dot);
            dot_4.setImageResource(R.drawable.dot);

        }
        else if(current_page==2)
        {
            btn1.setVisibility(View.VISIBLE);
            txt1.setVisibility(View.VISIBLE);
            btn1.setImageResource(R.drawable.contact);
            txt1.setText(resources.getString(R.string.adresler));
            btn2.setVisibility(View.VISIBLE);
            txt2.setVisibility(View.VISIBLE);
            btn2.setImageResource(R.drawable.machine_info);
            txt2.setText(resources.getString(R.string.makine_bilgileri));
            btn3.setVisibility(View.VISIBLE);
            txt3.setVisibility(View.VISIBLE);
            btn3.setImageResource(R.drawable.ota);
            txt3.setText("Güncelleme");
            next.setVisibility(View.VISIBLE);
            dot_1.setImageResource(R.drawable.dot);
            dot_2.setImageResource(R.drawable.dot);
            dot_3.setImageResource(R.drawable.dot_selected);
            dot_4.setImageResource(R.drawable.dot);

        }
        else if(current_page==3)
        {
            btn1.setVisibility(View.VISIBLE);
            txt1.setVisibility(View.VISIBLE);
            btn1.setImageResource(R.drawable.error);
            txt1.setText(resources.getString(R.string.hatalar));
            btn2.setVisibility(View.VISIBLE);
            txt2.setVisibility(View.VISIBLE);
            btn2.setImageResource(R.drawable.demo);
            txt2.setText("Demo Modu");
            btn3.setVisibility(View.INVISIBLE);
            txt3.setVisibility(View.INVISIBLE);
            btn3.setImageResource(R.drawable.time);
            txt3.setText(resources.getString(R.string.tarih_saat));
            next.setVisibility(View.INVISIBLE);
            dot_1.setImageResource(R.drawable.dot);
            dot_2.setImageResource(R.drawable.dot);
            dot_3.setImageResource(R.drawable.dot);
            dot_4.setImageResource(R.drawable.dot_selected);
        }
        else
        {

        }
    }
    public void back_click(View view) {
        Intent i = new Intent(getApplicationContext(),dosing.class);
        startActivity(i);
    }

    public void home_click(View view) {
        Intent i = new Intent(getApplicationContext(),dosing.class);
        startActivity(i);
    }
}