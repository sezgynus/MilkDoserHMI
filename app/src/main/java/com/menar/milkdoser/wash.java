package com.menar.milkdoser;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Calendar;

public class wash extends AppCompatActivity {
    private int currentApiVersion;
    TextView washtmview;
    EditText edit_wash_take,edit_wash_dwell,edit_wash_rinse,edit_wash_cycle;
    ImageButton save_btn,back_btn,home_btn,default_btn,next_page,prev_page;
    Button wash_time_button;
    Context ctx;
    Calendar c;
    //Yıkama parametreleri
    int WASH_TAKE,WASH_DWELL,WASH_RINSE,WASH_CYCLE;
    int hour,minute;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
    );

    @SuppressLint("ClickableViewAccessibility")
    public void initviews() {
        edit_wash_take= findViewById(R.id.t_wash_take);
        edit_wash_dwell= findViewById(R.id.t_wash_dwell);
        edit_wash_rinse= findViewById(R.id.t_wash_rinse);
        edit_wash_cycle= findViewById(R.id.n_wash_cycle);
        save_btn= findViewById(R.id.save);
        back_btn= findViewById(R.id.back);
        home_btn= findViewById(R.id.home);
        default_btn= findViewById(R.id.load_default);
        next_page= findViewById(R.id.next_page_btn);
        prev_page= findViewById(R.id.prev_page_btn);
        wash_time_button= findViewById(R.id.washtimebtn);
        washtmview= findViewById(R.id.washtimeview);
        load_params();

        wash_time_button.setOnTouchListener((view, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                c = Calendar.getInstance();

                @SuppressLint("SetTextI18n") TimePickerDialog tpd = new TimePickerDialog(wash.this,
                        (view1, hourOfDay_picker, minute_picker) -> {
                            Log.d("Time","Hour:"+hourOfDay_picker+" Minute:"+minute_picker);
                            hour=hourOfDay_picker;
                            minute=minute_picker;
                            washtmview.setText(hourOfDay_picker+":"+minute_picker);
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
        next_page.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                Intent i = new Intent(getApplicationContext(),motor_efficiency.class);
                startActivity(i);
            }
            return true;
        });


        prev_page.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                Intent i = new Intent(getApplicationContext(),rinse.class);
                startActivity(i);
            }
            return true;
        });

        save_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                save_params();
                Toast.makeText(getApplicationContext(),"Ayarlanan parametreler kaydedildi",Toast.LENGTH_LONG).show();
            }
            return true;
        });

        back_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                Intent i = new Intent(getApplicationContext(),settings.class);
                startActivity(i);
            }
            return true;
        });

        home_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                Intent i = new Intent(getApplicationContext(),dosing.class);
                startActivity(i);
            }
            return true;
        });

        default_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                save_default_params();
                init_params();
                load_params();
                Toast.makeText(getApplicationContext(),"Varsayılan parametreler kaydedildi",Toast.LENGTH_LONG).show();
            }
            return true;
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        super.onCreate(savedInstanceState);
        currentApiVersion = android.os.Build.VERSION.SDK_INT;

        hide_system_ui();
        setContentView(R.layout.activity_wash);
        ctx=this;
        //preventStatusBarExpansion(ctx);


        KeyboardUtils.addKeyboardToggleListener(this, isVisible -> {
            if(!isVisible)
            {
                hide_system_ui();
            }
            else
            {
                hide_system_ui();
            }
        });
        //save_default_params();
        init_params();
        initviews();

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
            decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
                if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                {
                    hide_system_ui();
                    decorView.setSystemUiVisibility(flags);
                }
            });
        }
    }

    public void init_params()
    {
        WASH_TAKE=sharedPref.getInt("wash_take_val",12);
        WASH_DWELL=sharedPref.getInt("wash_dwell_val",20);
        WASH_RINSE=sharedPref.getInt("wash_rinse_val",30);
        WASH_CYCLE=sharedPref.getInt("wash_cycle",3);
        hour=sharedPref.getInt("wash_hour",17);
        minute=sharedPref.getInt("wash_minute",30);
    }
    public void save_default_params()
    {
        editor.putInt("wash_take_val",12);
        editor.putInt("wash_dwell_val",20);
        editor.putInt("wash_rinse_val",30);
        editor.putInt("wash_cycle",3);
        editor.putInt("wash_hour", 17);
        editor.putInt("wash_minute", 30);
        editor.commit();
    }
    public void update_params()
    {
        WASH_TAKE=Integer.parseInt(String.valueOf(edit_wash_take.getText()));
        WASH_DWELL=Integer.parseInt(String.valueOf(edit_wash_dwell.getText()));
        WASH_RINSE=Integer.parseInt(String.valueOf(edit_wash_rinse.getText()));
        WASH_CYCLE=Integer.parseInt(String.valueOf(edit_wash_cycle.getText()));
    }
    public void save_params() {
        update_params();
        editor.putInt("wash_take_val", WASH_TAKE);
        editor.putInt("wash_dwell_val", WASH_DWELL);
        editor.putInt("wash_rinse_val", WASH_RINSE);
        editor.putInt("wash_cycle", WASH_CYCLE);
        editor.putInt("wash_hour", hour);
        editor.putInt("wash_minute", minute);
        editor.commit();
    }
    @SuppressLint("SetTextI18n")
    public void load_params()
    {
        edit_wash_take.setText(Integer.toString(WASH_TAKE));
        edit_wash_dwell.setText(Integer.toString(WASH_DWELL));
        edit_wash_rinse.setText(Integer.toString(WASH_RINSE));
        edit_wash_cycle.setText(Integer.toString(WASH_CYCLE));
        washtmview.setText(hour+":"+minute);
    }
}