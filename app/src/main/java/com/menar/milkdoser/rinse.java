package com.menar.milkdoser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class rinse extends AppCompatActivity {
    private int currentApiVersion;
    EditText edit_t_rinse_trig,edit_n_rinse_trig,edit_t_rinse,edit_a_sweep,edit_b_sweep;
    ImageButton save_btn,back_btn,home_btn,default_btn,next_page,prev_page;
    CheckBox pump;
    Context ctx;

    //Durulama parametreleri
    int T_RINSE_TRIG,N_RINSE_TRIG,T_RINSE,ENCSET_A_SWEEP,ENCSET_B_SWEEP;
    boolean PUMP_LOCK;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
    );


    public void initviews() {
        edit_t_rinse_trig=(EditText)findViewById(R.id.t_rinse_trig);
        edit_n_rinse_trig=(EditText)findViewById(R.id.n_rinse_trig);
        edit_t_rinse=(EditText)findViewById(R.id.t_rinse);
        edit_a_sweep=(EditText)findViewById(R.id.a_sweep);
        edit_b_sweep=(EditText)findViewById(R.id.b_sweep);
        save_btn=(ImageButton) findViewById(R.id.save);
        back_btn=(ImageButton) findViewById(R.id.back);
        home_btn=(ImageButton) findViewById(R.id.home);
        default_btn=(ImageButton) findViewById(R.id.load_default);
        next_page=(ImageButton) findViewById(R.id.next_page_btn);
        prev_page=(ImageButton) findViewById(R.id.prev_page_btn);
        pump=(CheckBox) findViewById(R.id.pump_lock);
        load_params();
        pump.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PUMP_LOCK=isChecked;
            }
        });
        next_page.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                    params.setMargins(8,0,8,0);
                    view.setLayoutParams(params);
                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                    params.setMargins(0,0,0,0);
                    view.setLayoutParams(params);
                    Intent i = new Intent(getApplicationContext(),wash.class);
                    startActivity(i);
                }
                return true;
            }
        });


        prev_page.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                    params.setMargins(8,0,8,0);
                    view.setLayoutParams(params);
                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                    params.setMargins(0,0,0,0);
                    view.setLayoutParams(params);
                    Intent i = new Intent(getApplicationContext(),backdraft.class);
                    startActivity(i);
                }
                return true;
            }
        });

        save_btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                    params.setMargins(8,0,8,0);
                    view.setLayoutParams(params);
                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                    params.setMargins(0,0,0,0);
                    view.setLayoutParams(params);
                    save_params();
                    Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.ayarlanan_parametreler_kaydedildi),Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        back_btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                    params.setMargins(8,0,8,0);
                    view.setLayoutParams(params);
                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                    params.setMargins(0,0,0,0);
                    view.setLayoutParams(params);
                    Intent i = new Intent(getApplicationContext(),settings.class);
                    startActivity(i);
                }
                return true;
            }
        });

        home_btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                    params.setMargins(8,0,8,0);
                    view.setLayoutParams(params);
                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                    params.setMargins(0,0,0,0);
                    view.setLayoutParams(params);
                    Intent i = new Intent(getApplicationContext(),dosing.class);
                    startActivity(i);
                }
                return true;
            }
        });

        default_btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                    params.setMargins(8,0,8,0);
                    view.setLayoutParams(params);
                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                    params.setMargins(0,0,0,0);
                    view.setLayoutParams(params);
                    save_default_params();
                    init_params();
                    load_params();
                    Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.varsayılan_parametreler_kaydedildi),Toast.LENGTH_LONG).show();
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

        hide_system_ui();
        setContentView(R.layout.activity_rinse);
        ctx=this;
        //preventStatusBarExpansion(ctx);


        KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener()
        {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible)
            {
                if(!isVisible)
                {
                    hide_system_ui();
                }
                else
                {
                    hide_system_ui();
                }
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
        int result = 0;
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

    public void init_params()
    {
        T_RINSE_TRIG=sharedPref.getInt("t_rinse_trig_val",10);
        N_RINSE_TRIG=sharedPref.getInt("n_rinse_trig_val",45);
        T_RINSE=sharedPref.getInt("t_rinse_val",30);
        ENCSET_A_SWEEP=sharedPref.getInt("a_sweep_val",100);
        ENCSET_B_SWEEP=sharedPref.getInt("b_sweep_val",100);
        PUMP_LOCK=sharedPref.getBoolean("pump_lock",true);
    }
    public void save_default_params()
    {
        editor.putInt("t_rinse_trig_val",10);
        editor.putInt("n_rinse_trig_val",45);
        editor.putInt("t_rinse_val",30);
        editor.putInt("a_sweep_val",100);
        editor.putInt("b_sweep_val",100);
        editor.putBoolean("pump_lock", true);
        editor.commit();
    }
    public void update_params()
    {

        T_RINSE_TRIG=Integer.parseInt(String.valueOf(edit_t_rinse_trig.getText()));
        N_RINSE_TRIG=Integer.parseInt(String.valueOf(edit_n_rinse_trig.getText()));
        T_RINSE=Integer.parseInt(String.valueOf(edit_t_rinse.getText()));
        ENCSET_A_SWEEP=Integer.parseInt(String.valueOf(edit_a_sweep.getText()));
        ENCSET_B_SWEEP=Integer.parseInt(String.valueOf(edit_b_sweep.getText()));
        PUMP_LOCK=pump.isChecked();

    }
    public void save_params() {
        update_params();
        editor.putInt("t_rinse_trig_val", T_RINSE_TRIG);
        editor.putInt("n_rinse_trig_val", N_RINSE_TRIG);
        editor.putInt("t_rinse_val", T_RINSE);
        editor.putInt("a_sweep_val", ENCSET_A_SWEEP);
        editor.putInt("b_sweep_val", ENCSET_B_SWEEP);
        editor.putBoolean("pump_lock", PUMP_LOCK);
        editor.commit();
    }
    public void load_params()
    {
        pump.setChecked(PUMP_LOCK);
        edit_t_rinse_trig.setText(Integer.toString(T_RINSE_TRIG));
        edit_n_rinse_trig.setText(Integer.toString(N_RINSE_TRIG));
        edit_t_rinse.setText(Integer.toString(T_RINSE));
        edit_a_sweep.setText(Integer.toString(ENCSET_A_SWEEP));
        edit_b_sweep.setText(Integer.toString(ENCSET_B_SWEEP));
    }
}