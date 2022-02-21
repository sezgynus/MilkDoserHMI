package com.menar.milkdoserhmi;

import android.annotation.SuppressLint;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class lang_screen extends AppCompatActivity {
    Button[][] lang_buttons=new Button[3][3];

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    String selected_language="tr";

    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
    );

    @SuppressLint("ClickableViewAccessibility")
    public void initviews(){
        lang_buttons[0][0]= findViewById(R.id.lang1);
        lang_buttons[0][1]= findViewById(R.id.lang2);
        lang_buttons[0][2]= findViewById(R.id.lang3);
        lang_buttons[1][0]= findViewById(R.id.lang4);
        lang_buttons[1][1]= findViewById(R.id.lang5);
        lang_buttons[1][2]= findViewById(R.id.lang6);
        lang_buttons[2][0]= findViewById(R.id.lang7);
        lang_buttons[2][1]= findViewById(R.id.lang8);
        lang_buttons[2][2]= findViewById(R.id.lang9);
        ImageButton apply_btn= findViewById(R.id.apply);
        apply_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                editor.putString("running_lang",selected_language);
                editor.commit();
            }
            return true;
        });
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                lang_buttons[i][j].setOnTouchListener((view, event) -> {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        rst_selection();
                        Button b=(Button)view;
                        selectlang(b.getText().toString());
                        view.setBackgroundResource(R.drawable.langbtn_selected);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    }
                    return true;
                });
            }
        }
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
        setContentView(R.layout.lang_screen);
        initviews();
        getselectedlang();
        //Context ctx = this;
        //preventStatusBarExpansion(ctx);

    }
    public void getselectedlang()
    {
        String language;
        selected_language=sharedPref.getString("running_lang","tr");
        switch (selected_language) {
            case "tr":
                language = "Türkçe";
                break;
            case "en":
                language = "English";
                break;
            case "de":
                language = "deutsch";
                break;
            case "it":
                language = "italiano";
                break;
            case "es":
                language = "español";
                break;
            case "nl":
                language = "Nederlands";
                break;
            case "fr":
                language = "français";
                break;
            case "pl":
                language = "polski";
                break;
            case "sl":
                language = "slovensko";
                break;
            default:
                language = "Türkçe";
                break;
        }
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                if(language.equals(lang_buttons[i][j].getText().toString()))lang_buttons[i][j].setBackgroundResource(R.drawable.langbtn_selected);
            }
        }

    }
    public void selectlang(String language)
    {
        switch (language) {
            case "Türkçe":
                selected_language = "tr";
                break;
            case "English":
                selected_language = "en";
                break;
            case "deutsch":
                selected_language = "de";
                break;
            case "italiano":
                selected_language = "it";
                break;
            case "español":
                selected_language = "es";
                break;
            case "Nederlands":
                selected_language = "nl";
                break;
            case "français":
                selected_language = "fr";
                break;
            case "polski":
                selected_language = "pl";
                break;
            case "slovensko":
                selected_language = "sl";
                break;
            default:
                selected_language = "tr";
                break;
        }
        Log.d("Selected language= ",selected_language);
    }

    public void rst_selection()
    {
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                lang_buttons[i][j].setBackgroundResource(R.drawable.langbtn);
            }
        }
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

    public void home_click(View view) {
        Intent i = new Intent(getApplicationContext(),dosing.class);
        startActivity(i);
    }

    public void back_click(View view) {
        Intent i = new Intent(getApplicationContext(),settings.class);
        startActivity(i);
    }
}