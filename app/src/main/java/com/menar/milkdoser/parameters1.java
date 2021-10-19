package com.menar.milkdoser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class parameters1 extends AppCompatActivity {
    private int currentApiVersion;
    EditText a1_input,a2_input,a3_input,a4_input,b1_input,b2_input,b3_input,b4_input;
    ImageButton save_btn,back_btn,home_btn,default_btn,next_page,prev_page;
    Context ctx;
    int selected_group=1;

    // Kanal-A doz parametreleri
    int ENCSET_A_11,ENCSET_A_12,ENCSET_A_13,ENCSET_A_14,ENCSET_A_21,ENCSET_A_22,ENCSET_A_23,ENCSET_A_24,ENCSET_A_31,ENCSET_A_32,ENCSET_A_33,ENCSET_A_34;
    // Kanal-b doz parametreleri
    int ENCSET_B_11,ENCSET_B_12,ENCSET_B_13,ENCSET_B_14,ENCSET_B_21,ENCSET_B_22,ENCSET_B_23,ENCSET_B_24,ENCSET_B_31,ENCSET_B_32,ENCSET_B_33,ENCSET_B_34;


    float PPML = (float) 3043.44;//100ml başına pulse

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    ConstraintLayout layout;
    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
    );


    @SuppressLint("ClickableViewAccessibility")
    public void initviews() {
        a1_input= findViewById(R.id.a1);
        a2_input= findViewById(R.id.a2);
        a3_input= findViewById(R.id.a3);
        a4_input= findViewById(R.id.a4);
        b1_input= findViewById(R.id.b1);
        b2_input= findViewById(R.id.b2);
        b3_input= findViewById(R.id.b3);
        b4_input= findViewById(R.id.b4);
        save_btn= findViewById(R.id.save);
        back_btn= findViewById(R.id.back);
        home_btn= findViewById(R.id.home);
        default_btn= findViewById(R.id.load_default);
        next_page= findViewById(R.id.next_page_btn);
        prev_page= findViewById(R.id.prev_page_btn);
        load_dose_group_values(1);

        a1_input.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(a1_input.getText().length() != 0) {
                    if (utils.ml_to_pulse(Integer.parseInt(String.valueOf(a1_input.getText())),PPML,false) > 65535) {

                        a1_input.setText(Integer.toString(utils.pulse_to_ml(65535,PPML,false)));
                    }
                }
            }
        });
        a2_input.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(a2_input.getText().length() != 0) {
                    if (utils.ml_to_pulse(Integer.parseInt(String.valueOf(a2_input.getText())),PPML,false) > 65535) {
                        a2_input.setText(Integer.toString(utils.pulse_to_ml(65535,PPML,false)));
                    }
                }
            }
        });
        a3_input.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(a3_input.getText().length() != 0) {
                    if (utils.ml_to_pulse(Integer.parseInt(String.valueOf(a3_input.getText())),PPML,false) > 65535) {
                        a3_input.setText(Integer.toString(utils.pulse_to_ml(65535,PPML,false)));
                    }
                }
            }
        });
        a4_input.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(a4_input.getText().length() != 0) {
                    if (utils.ml_to_pulse(Integer.parseInt(String.valueOf(a4_input.getText())),PPML,false) > 65535) {
                        a4_input.setText(Integer.toString(utils.pulse_to_ml(65535,PPML,false)));
                    }
                }
            }
        });
        b1_input.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(b1_input.getText().length() != 0) {
                    if (utils.ml_to_pulse(Integer.parseInt(String.valueOf(b1_input.getText())),PPML,false) > 65535) {
                        b1_input.setText(Integer.toString(utils.pulse_to_ml(65535,PPML,false)));
                    }
                }
            }
        });
        b2_input.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(b2_input.getText().length() != 0) {
                    if (utils.ml_to_pulse(Integer.parseInt(String.valueOf(b2_input.getText())),PPML,false) > 65535) {
                        b2_input.setText(Integer.toString(utils.pulse_to_ml(65535,PPML,false)));
                    }
                }
            }
        });
        b3_input.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(b3_input.getText().length() != 0) {
                    if (utils.ml_to_pulse(Integer.parseInt(String.valueOf(b3_input.getText())),PPML,false) > 65535) {
                        b3_input.setText(Integer.toString(utils.pulse_to_ml(65535,PPML,false)));
                    }
                }
            }
        });
        b4_input.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(b4_input.getText().length() != 0) {
                    if (utils.ml_to_pulse(Integer.parseInt(String.valueOf(b4_input.getText())),PPML,false) > 65535) {
                        b4_input.setText(Integer.toString(utils.pulse_to_ml(65535,PPML,false)));
                    }
                }
            }
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
                Intent i = new Intent(getApplicationContext(),backdraft.class);
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
                Intent i = new Intent(getApplicationContext(),motor_efficiency.class);
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
                save_dose_group_values(1);
                save_dose_group_values(2);
                save_dose_group_values(3);
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
                load_dose_group_values(selected_group);
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
        setContentView(R.layout.activity_parameters1);
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
        layout= findViewById(R.id.parameters_layout);

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
        ENCSET_A_11=sharedPref.getInt("1a1_value",100);
        ENCSET_A_12=sharedPref.getInt("1a2_value",200);
        ENCSET_A_13=sharedPref.getInt("1a3_value",300);
        ENCSET_A_14=sharedPref.getInt("1a4_value",400);
        ENCSET_A_21=sharedPref.getInt("2a1_value",125);
        ENCSET_A_22=sharedPref.getInt("2a2_value",225);
        ENCSET_A_23=sharedPref.getInt("2a3_value",325);
        ENCSET_A_24=sharedPref.getInt("2a4_value",425);
        ENCSET_A_31=sharedPref.getInt("3a1_value",150);
        ENCSET_A_32=sharedPref.getInt("3a2_value",250);
        ENCSET_A_33=sharedPref.getInt("3a3_value",350);
        ENCSET_A_34=sharedPref.getInt("3a4_value",450);

        ENCSET_B_11=sharedPref.getInt("1b1_value",100);
        ENCSET_B_12=sharedPref.getInt("1b2_value",200);
        ENCSET_B_13=sharedPref.getInt("1b3_value",300);
        ENCSET_B_14=sharedPref.getInt("1b4_value",400);
        ENCSET_B_21=sharedPref.getInt("2b1_value",125);
        ENCSET_B_22=sharedPref.getInt("2b2_value",225);
        ENCSET_B_23=sharedPref.getInt("2b3_value",325);
        ENCSET_B_24=sharedPref.getInt("2b4_value",425);
        ENCSET_B_31=sharedPref.getInt("3b1_value",150);
        ENCSET_B_32=sharedPref.getInt("3b2_value",250);
        ENCSET_B_33=sharedPref.getInt("3b3_value",350);
        ENCSET_B_34=sharedPref.getInt("3b4_value",450);

        PPML = sharedPref.getFloat("ppml", (float) 3043.44);
    }
    public void save_default_params()
    {
        editor.putInt("1a1_value",100);
        editor.putInt("1a2_value",200);
        editor.putInt("1a3_value",300);
        editor.putInt("1a4_value",400);
        editor.putInt("1b1_value",100);
        editor.putInt("1b2_value",200);
        editor.putInt("1b3_value",300);
        editor.putInt("1b4_value",400);
        editor.putInt("2a1_value",125);
        editor.putInt("2a2_value",225);
        editor.putInt("2a3_value",325);
        editor.putInt("2a4_value",425);
        editor.putInt("2b1_value",125);
        editor.putInt("2b2_value",225);
        editor.putInt("2b3_value",325);
        editor.putInt("2b4_value",425);
        editor.putInt("3a1_value",150);
        editor.putInt("3a2_value",250);
        editor.putInt("3a3_value",350);
        editor.putInt("3a4_value",450);
        editor.putInt("3b1_value",150);
        editor.putInt("3b2_value",250);
        editor.putInt("3b3_value",350);
        editor.putInt("3b4_value",450);
        editor.commit();
    }
    public void update_params()
    {
        if(selected_group==1)
        {
            ENCSET_A_11=Integer.parseInt(String.valueOf(a1_input.getText()));
            ENCSET_A_12=Integer.parseInt(String.valueOf(a2_input.getText()));
            ENCSET_A_13=Integer.parseInt(String.valueOf(a3_input.getText()));
            ENCSET_A_14=Integer.parseInt(String.valueOf(a4_input.getText()));

            ENCSET_B_11=Integer.parseInt(String.valueOf(b1_input.getText()));
            ENCSET_B_12=Integer.parseInt(String.valueOf(b2_input.getText()));
            ENCSET_B_13=Integer.parseInt(String.valueOf(b3_input.getText()));
            ENCSET_B_14=Integer.parseInt(String.valueOf(b4_input.getText()));
        }
        else if(selected_group==2)
        {
            ENCSET_A_21=Integer.parseInt(String.valueOf(a1_input.getText()));
            ENCSET_A_22=Integer.parseInt(String.valueOf(a2_input.getText()));
            ENCSET_A_23=Integer.parseInt(String.valueOf(a3_input.getText()));
            ENCSET_A_24=Integer.parseInt(String.valueOf(a4_input.getText()));

            ENCSET_B_21=Integer.parseInt(String.valueOf(b1_input.getText()));
            ENCSET_B_22=Integer.parseInt(String.valueOf(b2_input.getText()));
            ENCSET_B_23=Integer.parseInt(String.valueOf(b3_input.getText()));
            ENCSET_B_24=Integer.parseInt(String.valueOf(b4_input.getText()));
        }
        else if(selected_group==3)
        {
            ENCSET_A_31=Integer.parseInt(String.valueOf(a1_input.getText()));
            ENCSET_A_32=Integer.parseInt(String.valueOf(a2_input.getText()));
            ENCSET_A_33=Integer.parseInt(String.valueOf(a3_input.getText()));
            ENCSET_A_34=Integer.parseInt(String.valueOf(a4_input.getText()));

            ENCSET_B_31=Integer.parseInt(String.valueOf(b1_input.getText()));
            ENCSET_B_32=Integer.parseInt(String.valueOf(b2_input.getText()));
            ENCSET_B_33=Integer.parseInt(String.valueOf(b3_input.getText()));
            ENCSET_B_34=Integer.parseInt(String.valueOf(b4_input.getText()));
        }
    }
    public void save_dose_group_values(int dose_group)
    {
        update_params();
        if(dose_group==1)
        {
            editor.putInt("1a1_value",ENCSET_A_11);
            editor.putInt("1a2_value",ENCSET_A_12);
            editor.putInt("1a3_value",ENCSET_A_13);
            editor.putInt("1a4_value",ENCSET_A_14);
            editor.putInt("1b1_value",ENCSET_B_11);
            editor.putInt("1b2_value",ENCSET_B_12);
            editor.putInt("1b3_value",ENCSET_B_13);
            editor.putInt("1b4_value",ENCSET_B_14);
            editor.commit();
        }
        else if(dose_group==2)
        {
            editor.putInt("2a1_value",ENCSET_A_21);
            editor.putInt("2a2_value",ENCSET_A_22);
            editor.putInt("2a3_value",ENCSET_A_23);
            editor.putInt("2a4_value",ENCSET_A_24);
            editor.putInt("2b1_value",ENCSET_B_21);
            editor.putInt("2b2_value",ENCSET_B_22);
            editor.putInt("2b3_value",ENCSET_B_23);
            editor.putInt("2b4_value",ENCSET_B_24);
            editor.commit();
        }
        else if(dose_group==3)
        {
            editor.putInt("3a1_value",ENCSET_A_31);
            editor.putInt("3a2_value",ENCSET_A_32);
            editor.putInt("3a3_value",ENCSET_A_33);
            editor.putInt("3a4_value",ENCSET_A_34);
            editor.putInt("3b1_value",ENCSET_B_31);
            editor.putInt("3b2_value",ENCSET_B_32);
            editor.putInt("3b3_value",ENCSET_B_33);
            editor.putInt("3b4_value",ENCSET_B_34);
            editor.commit();
        }
        else
        {

        }
    }
    @SuppressLint("SetTextI18n")
    public void load_dose_group_values(int dose_group)
    {
        if(dose_group==1)
        {
            a1_input.setText(Integer.toString(ENCSET_A_11));
            a2_input.setText(Integer.toString(ENCSET_A_12));
            a3_input.setText(Integer.toString(ENCSET_A_13));
            a4_input.setText(Integer.toString(ENCSET_A_14));

            b1_input.setText(Integer.toString(ENCSET_B_11));
            b2_input.setText(Integer.toString(ENCSET_B_12));
            b3_input.setText(Integer.toString(ENCSET_B_13));
            b4_input.setText(Integer.toString(ENCSET_B_14));
        }
        else if(dose_group==2)
        {
            a1_input.setText(Integer.toString(ENCSET_A_21));
            a2_input.setText(Integer.toString(ENCSET_A_22));
            a3_input.setText(Integer.toString(ENCSET_A_23));
            a4_input.setText(Integer.toString(ENCSET_A_24));

            b1_input.setText(Integer.toString(ENCSET_B_21));
            b2_input.setText(Integer.toString(ENCSET_B_22));
            b3_input.setText(Integer.toString(ENCSET_B_23));
            b4_input.setText(Integer.toString(ENCSET_B_24));
        }
        else if(dose_group==3)
        {
            a1_input.setText(Integer.toString(ENCSET_A_31));
            a2_input.setText(Integer.toString(ENCSET_A_32));
            a3_input.setText(Integer.toString(ENCSET_A_33));
            a4_input.setText(Integer.toString(ENCSET_A_34));

            b1_input.setText(Integer.toString(ENCSET_B_31));
            b2_input.setText(Integer.toString(ENCSET_B_32));
            b3_input.setText(Integer.toString(ENCSET_B_33));
            b4_input.setText(Integer.toString(ENCSET_B_34));
        }
        else
        {
            a1_input.setText(Integer.toString(0));
            a2_input.setText(Integer.toString(0));
            a3_input.setText(Integer.toString(0));
            a4_input.setText(Integer.toString(0));

            b1_input.setText(Integer.toString(0));
            b2_input.setText(Integer.toString(0));
            b3_input.setText(Integer.toString(0));
            b4_input.setText(Integer.toString(0));
        }
    }
    public void dg1_click(View view)
    {
        selected_group=1;
        load_dose_group_values(selected_group);
        layout.setBackgroundResource(R.drawable.dose_group1_bg1024);
    }
    public void dg2_click(View view)
    {
        selected_group=2;
        load_dose_group_values(selected_group);
        layout.setBackgroundResource(R.drawable.dose_group2_bg1024);
    }
    public void dg3_click(View view)
    {
        selected_group=3;
        load_dose_group_values(selected_group);
        layout.setBackgroundResource(R.drawable.dose_group3_bg1024);
    }
}