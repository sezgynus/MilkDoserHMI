package com.menar.milkdoser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class pincode extends AppCompatActivity {

    ImageView d1,d2,d3,d4;
    ImageButton key0_btn,key1_btn,key2_btn,key3_btn,key4_btn,key5_btn,key6_btn,key7_btn,key8_btn,key9_btn,del_btn,ok_btn,home_btn,back_btn;
    int digit=0;
    int[] pin_array=new int[4];
    int[] om_password={2,4,5,3};
    int[] sm_password={2,6,1,2};
    int[] master_password={5,9,9,7};

    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
    );

    @SuppressLint("ClickableViewAccessibility")
    public void initviews(){
        d1 = findViewById(R.id.dot1);
        d2 = findViewById(R.id.dot2);
        d3 = findViewById(R.id.dot3);
        d4 = findViewById(R.id.dot4);
        key0_btn= findViewById(R.id.key0);
        key1_btn= findViewById(R.id.key1);
        key2_btn= findViewById(R.id.key2);
        key3_btn= findViewById(R.id.key3);
        key4_btn= findViewById(R.id.key4);
        key5_btn= findViewById(R.id.key5);
        key6_btn= findViewById(R.id.key6);
        key7_btn= findViewById(R.id.key7);
        key8_btn= findViewById(R.id.key8);
        key9_btn= findViewById(R.id.key9);
        del_btn= findViewById(R.id.del);
        ok_btn= findViewById(R.id.ok);
        home_btn= findViewById(R.id.home);
        back_btn= findViewById(R.id.back);
        key0_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                key0_click();
            }
            return true;
        });
        key1_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                key1_click();
            }
            return true;
        });
        key2_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                key2_click();
            }
            return true;
        });
        key3_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                key3_click();
            }
            return true;
        });
        key4_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                key4_click();
            }
            return true;
        });
        key5_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                key5_click();
            }
            return true;
        });
        key6_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                key6_click();
            }
            return true;
        });
        key7_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                key7_click();
            }
            return true;
        });
        key8_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                key8_click();
            }
            return true;
        });
        key9_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                key9_click();
            }
            return true;
        });
        home_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                home_click();
            }
            return true;
        });
        ok_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                ok_click();
            }
            return true;
        });
        del_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                del_click();
            }
            return true;
        });
        back_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(8,0,8,0);
                view.setLayoutParams(params);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                params=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(0,0,0,0);
                view.setLayoutParams(params);
                back_click();
            }
            return true;
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        setContentView(R.layout.activity_pincode);
        //Context ctx = this;
        //preventStatusBarExpansion(ctx);
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

        CustomViewGroup view = new CustomViewGroup(context);
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

    public void key0_click() {
        if(!(digit>=4))
        {
            digit++;
            pin_array[digit - 1] = 0;
        }
        write_dot();
    }

    public void key1_click() {
        if(!(digit>=4))
        {
            digit++;
            pin_array[digit - 1] = 1;
        }
        write_dot();
    }

    public void key2_click() {
        if(!(digit>=4))
        {
            digit++;
            pin_array[digit - 1] = 2;
        }
        write_dot();
    }

    public void key3_click() {
        if(!(digit>=4))
        {
            digit++;
            pin_array[digit - 1] = 3;
        }
        write_dot();
    }

    public void key4_click() {
        if(!(digit>=4))
        {
            digit++;
            pin_array[digit - 1] = 4;
        }
        write_dot();
    }

    public void key5_click() {
        if(!(digit>=4))
        {
            digit++;
            pin_array[digit - 1] = 5;
        }
        write_dot();
    }

    public void key6_click() {
        if(!(digit>=4))
        {
            digit++;
            pin_array[digit - 1] = 6;
        }
        write_dot();
    }

    public void key7_click() {
        if(!(digit>=4))
        {
            digit++;
            pin_array[digit - 1] = 7;
        }
        write_dot();
    }

    public void key8_click() {
        if(!(digit>=4))
        {
            digit++;
            pin_array[digit - 1] = 8;
        }
        write_dot();
    }

    public void key9_click() {
        if(!(digit>=4))
        {
            digit++;
            pin_array[digit - 1] = 9;
        }
        write_dot();
    }

    public void del_click() {
        digit=0;
        pin_array=new int[4];
        d1.setVisibility(View.INVISIBLE);
        d2.setVisibility(View.INVISIBLE);
        d3.setVisibility(View.INVISIBLE);
        d4.setVisibility(View.INVISIBLE);
    }

    public void ok_click() {
        if(sm_password[0]==pin_array[0]&&sm_password[1]==pin_array[1]&&sm_password[2]==pin_array[2]&&sm_password[3]==pin_array[3])
        {
            Intent i = new Intent(getApplicationContext(),settings.class);
            startActivity(i);
            Toast.makeText(getApplicationContext(),"Servis Menüsü Girişi",Toast.LENGTH_LONG).show();
        }
        else if(om_password[0]==pin_array[0]&&om_password[1]==pin_array[1]&&om_password[2]==pin_array[2]&&om_password[3]==pin_array[3])
        {
            Intent i = new Intent(getApplicationContext(),operator_menu.class);
            startActivity(i);
            Toast.makeText(getApplicationContext(),"Operasyon Müdürü Girişi",Toast.LENGTH_LONG).show();
        }
        else if(master_password[0]==pin_array[0]&&master_password[1]==pin_array[1]&&master_password[2]==pin_array[2]&&master_password[3]==pin_array[3])
        {
            Intent i = new Intent(getApplicationContext(),manufa.class);
            startActivity(i);
            Toast.makeText(getApplicationContext(),"Üretici Grişi",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Şifre yanlış! Tekrar deneyin...",Toast.LENGTH_LONG).show();
            digit=0;
            d1.setVisibility(View.INVISIBLE);
            d2.setVisibility(View.INVISIBLE);
            d3.setVisibility(View.INVISIBLE);
            d4.setVisibility(View.INVISIBLE);
            pin_array=new int[4];
        }

    }

    public void home_click() {
        Intent i = new Intent(getApplicationContext(),dosing.class);
        startActivity(i);
    }

    public void back_click() {
        Intent i = new Intent(getApplicationContext(),dosing.class);
        startActivity(i);
    }
    private void write_dot()
    {
        if(digit==1)
        {
            d1.setVisibility(View.VISIBLE);
        }
        else if(digit==2)
        {
            d1.setVisibility(View.VISIBLE);
            d2.setVisibility(View.VISIBLE);
        }
        else if(digit==3)
        {
            d1.setVisibility(View.VISIBLE);
            d2.setVisibility(View.VISIBLE);
            d3.setVisibility(View.VISIBLE);
        }
        else if(digit==4)
        {
            d1.setVisibility(View.VISIBLE);
            d2.setVisibility(View.VISIBLE);
            d3.setVisibility(View.VISIBLE);
            d4.setVisibility(View.VISIBLE);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Bilgilendirme mesajı",Toast.LENGTH_LONG).show();
        }
    }
}