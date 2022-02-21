package com.menar.milkdoserhmi;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class backdraft extends AppCompatActivity {
    private int currentApiVersion;
    EditText edit_pipe_add,edit_a_back,edit_b_back,edit_idle;
    ImageButton save_btn,back_btn,home_btn,default_btn,next_page,prev_page;
    Context ctx;

    //Geri çekme parametreleri
    int ENCSET_PIPE_ADD,ENCSET_A_BACK,ENCSET_B_BACK,T_IDLE_BACK;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
    );


    public void initviews() {
        edit_pipe_add= findViewById(R.id.pipe_add);
        edit_a_back= findViewById(R.id.a_back);
        edit_b_back= findViewById(R.id.b_back);
        edit_idle= findViewById(R.id.idle_back);
        save_btn= findViewById(R.id.save);
        back_btn= findViewById(R.id.back);
        home_btn= findViewById(R.id.home);
        default_btn= findViewById(R.id.load_default);
        next_page= findViewById(R.id.next_page_btn);
        prev_page= findViewById(R.id.prev_page_btn);
        load_params();

        next_page.setOnTouchListener((view, event) -> {
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


        prev_page.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                Intent i = new Intent(getApplicationContext(),parameters1.class);
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
                Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.ayarlanan_parametreler_kaydedildi),Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.varsayılan_parametreler_kaydedildi),Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.activity_backdraft);
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
        save_default_params();
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
        ENCSET_PIPE_ADD=sharedPref.getInt("pipe_add_value",300);
        ENCSET_A_BACK=sharedPref.getInt("a_back_value",20);
        ENCSET_B_BACK=sharedPref.getInt("b_back_value",20);
        T_IDLE_BACK=sharedPref.getInt("idle_value",250);
    }
    public void save_default_params()
    {
        editor.putInt("pipe_add_value",300);
        editor.putInt("a_back_value",20);
        editor.putInt("b_back_value",20);
        editor.putInt("idle_value",250);
        editor.commit();
    }
    public void update_params()
    {

        ENCSET_PIPE_ADD=Integer.parseInt(String.valueOf(edit_pipe_add.getText()));
        ENCSET_A_BACK=Integer.parseInt(String.valueOf(edit_a_back.getText()));
        ENCSET_B_BACK=Integer.parseInt(String.valueOf(edit_b_back.getText()));
        T_IDLE_BACK=Integer.parseInt(String.valueOf(edit_idle.getText()));

    }
    public void save_params() {
        update_params();
        editor.putInt("pipe_add_value", ENCSET_PIPE_ADD);
        editor.putInt("a_back_value", ENCSET_A_BACK);
        editor.putInt("b_back_value", ENCSET_B_BACK);
        editor.putInt("idle_value", T_IDLE_BACK);
        editor.commit();
    }
    public void load_params()
    {
        edit_pipe_add.setText(Integer.toString(ENCSET_PIPE_ADD));
        edit_a_back.setText(Integer.toString(ENCSET_A_BACK));
        edit_b_back.setText(Integer.toString(ENCSET_B_BACK));
        edit_idle.setText(Integer.toString(T_IDLE_BACK));
    }
}