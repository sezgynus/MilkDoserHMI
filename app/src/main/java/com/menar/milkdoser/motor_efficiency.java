package com.menar.milkdoser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Timer;
import java.util.TimerTask;

public class motor_efficiency extends AppCompatActivity {

    private int currentApiVersion;
    Socket socket;
    private PrintWriter output;
    boolean wifi_connected=false;
    String SERVER_IP = "192.168.4.1";
    int SERVER_PORT = 20000;

    int l_cmd = 0, r_cmd = 0;
    Timer timer;
    Thread connect = null;
    final Handler handler = new Handler();

    public char[] query_buf = {0x02, 0x55, 0x11, 0x00, 0x00, 0x00, 0x00, 0x00, 0x57};
    public char[] speed_set = {0x02, 0x77, 0x11, 0x00, 0xbc, 0x02, 0xbc, 0x02, 0x57};
    public char[] l_buf = {0x02, 0x55, 0x06, 0x02, 0x00, 0x00, 0xD0, 0xFA, 0x29};
    public char[] r_buf = {0x02, 0x55, 0x60, 0x01, 0xD0, 0xFA, 0x00, 0x00, 0x93};
    public char[] l_start = {0x02, 0x55, 0x05, 0x02, 0x00, 0x00, 0x00, 0x00, 0x5D};
    public char[] r_start = {0x02, 0x55, 0x50, 0x01, 0x00, 0x00, 0x00, 0x00, 0xA9};
    public char[] l_stop = {0x02, 0x55, 0x0D, 0x20, 0x00, 0x00, 0x00, 0x00, 0x74};
    public char[] r_stop = {0x02, 0x55, 0xD0, 0x10, 0x00, 0x00, 0x00, 0x00, 0x47};

    int cmd_repeat_cnt = 5, r_start_cmd_cnt = 0, r_stop_cmd_cnt = 0, l_start_cmd_cnt = 0, l_stop_cmd_cnt = 0;

    SeekBar motor_a_eff, motor_b_eff;
    TextView motor_a_per, motor_b_per, max_dose_a, max_dose_b;
    EditText ml_input_a,ml_input_b;
    ImageButton save_btn, back_btn, home_btn, default_btn, next_page, prev_page;
    Button fill_a, fill_b, calibration_a, calibration_b;

    //PWM parametreleri
    int PWM_A, PWM_B;
    int minimum_pwm = 700;
    float PPML_A,PPML_B;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
    );


    @SuppressLint("ClickableViewAccessibility")
    public void initviews() {
        calibration_a = findViewById(R.id.a_calibration);
        calibration_b = findViewById(R.id.b_calibration);
        fill_a = findViewById(R.id.a_fill);
        fill_b = findViewById(R.id.b_fill);
        save_btn = findViewById(R.id.save);
        back_btn = findViewById(R.id.back);
        home_btn = findViewById(R.id.home);
        default_btn = findViewById(R.id.load_default);
        next_page = findViewById(R.id.next_page_btn);
        prev_page = findViewById(R.id.prev_page_btn);
        motor_a_eff = findViewById(R.id.motorA);
        motor_a_per = findViewById(R.id.motorA_per);
        motor_b_eff = findViewById(R.id.motorB);
        motor_b_per = findViewById(R.id.motorB_per);
        max_dose_a = findViewById(R.id.max_dosing_a);
        max_dose_b = findViewById(R.id.max_dosing_b);
        ml_input_a = findViewById(R.id.ml_a);
        ml_input_b = findViewById(R.id.ml_b);

        ml_input_a.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(ml_input_a.getText().length()>0)
                {
                    PPML_A=calculate_calibration_factor(3000,Float.parseFloat(String.valueOf(ml_input_a.getText())));
                    max_dose_a.setText(utils.pulse_to_ml(65535, PPML_A, false) +"mL");
                }
            }
        });
        ml_input_b.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (ml_input_b.getText().length() > 0) {
                    PPML_B = calculate_calibration_factor(3000, Float.parseFloat(String.valueOf(ml_input_b.getText())));
                    max_dose_b.setText(utils.pulse_to_ml(65535, PPML_B, false) +"mL");
                }
            }
        });
        calibration_a.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                l_cmd = 1;
            }
            return true;
        });
        calibration_b.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                r_cmd = 1;
            }
            return true;
        });
        fill_a.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                l_cmd = 2;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                l_cmd = 3;
            }
            return true;
        });
        fill_b.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                r_cmd = 2;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                r_cmd = 3;
            }
            return true;
        });
        motor_a_eff.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                PWM_A = progress + minimum_pwm;
                motor_a_per.setText("%" + map((PWM_A - minimum_pwm), 0, 323, 0, 100));
            }
        });
        motor_b_eff.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                PWM_B = progress + minimum_pwm;
                motor_b_per.setText("%" + map((PWM_B - minimum_pwm), 0, 323, 0, 100));
            }
        });
        next_page.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                Intent i = new Intent(getApplicationContext(), parameters1.class);
                startActivity(i);
            }
            return true;
        });


        prev_page.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                Intent i = new Intent(getApplicationContext(), wash.class);
                startActivity(i);
            }
            return true;
        });

        save_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                save_params();
                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.ayarlanan_parametreler_kaydedildi), Toast.LENGTH_LONG).show();
            }
            return true;
        });

        back_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                Intent i = new Intent(getApplicationContext(), settings.class);
                startActivity(i);
            }
            return true;
        });

        home_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                Intent i = new Intent(getApplicationContext(), dosing.class);
                startActivity(i);
            }
            return true;
        });

        default_btn.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(8, 0, 8, 0);
                view.setLayoutParams(params);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);
                save_default_params();
                init_params();
                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.varsayılan_parametreler_kaydedildi), Toast.LENGTH_LONG).show();
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

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {

            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(visibility -> {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            decorView.setSystemUiVisibility(flags);
                        }
                    });
        }
        setContentView(R.layout.activity_motor_efficiency);
        KeyboardUtils.addKeyboardToggleListener(this, isVisible -> {
            if (!isVisible) {
                hide_system_ui();
            } else {
                hide_system_ui();
            }
        });
        initviews();
        init_params();
        timer = new Timer();
        timer.schedule(timerTask,200,100);
    }

    public void back_click(View view) {
        Intent i = new Intent(getApplicationContext(), settings.class);
        startActivity(i);
    }

    public void home_click(View view) {
        Intent i = new Intent(getApplicationContext(), dosing.class);
        startActivity(i);
    }

    long map(long x, long in_min, long in_max, long out_min, long out_max) {
        if (((in_max - in_min) + out_min) != 0) {
            return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
        } else return 0;
    }

    public void init_params() {
        PWM_A = sharedPref.getInt("channel_a_pwm", 1023);
        PWM_B = sharedPref.getInt("channel_b_pwm", 1023);
        PPML_A = sharedPref.getFloat("ppml_a", (float) 3043.44);
        PPML_B = sharedPref.getFloat("ppml_a", (float) 3043.44);

        Log.d("PWMA", ": " + PWM_A);
        Log.d("PWMB", ": " + PWM_B);
        load_params();
    }

    public void save_default_params() {
        editor.putInt("channel_a_pwm", 1023);
        editor.putInt("channel_b_pwm", 1023);
        editor.putFloat("ppml_a", (float) 3043.44);
        editor.putFloat("ppml_b", (float) 3043.44);
        editor.commit();
    }

    public void save_params() {
        editor.putInt("channel_a_pwm", PWM_A);
        editor.putInt("channel_b_pwm", PWM_B);
        editor.putFloat("ppml_a", PPML_A);
        editor.putFloat("ppml_b", PPML_B);
        editor.commit();
    }

    public void hide_system_ui() {
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {

            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    hide_system_ui();
                    decorView.setSystemUiVisibility(flags);
                }
            });
        }
    }
    public float calculate_calibration_factor(int pulse, float ml) {
        return (pulse*100)/ml;
    }
    @SuppressLint("SetTextI18n")
    public void load_params() {
        Log.d("PWMA", ": " + PWM_A);
        Log.d("PWMB", ": " + PWM_B);
        motor_a_eff.setProgress(PWM_A - minimum_pwm);
        motor_a_per.setText("%" + map((PWM_A - minimum_pwm), 0, 323, 0, 100));
        motor_b_eff.setProgress(PWM_B - minimum_pwm);
        motor_b_per.setText("%" + map((PWM_B - minimum_pwm), 0, 323, 0, 100));
    }

    class connect implements Runnable {
        public void run() {
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
                output = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream(),
                                Charset.forName("ISO-8859-1").newEncoder())),
                        true);
                //Log.d("test","success");
                wifi_connected=true;
            } catch (IOException e) {
                //Log.d("test","error");
                wifi_connected=false;
                //e.printStackTrace();
            }
        }
    }
    final TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if(wifi_connected) {
                handler.post(() -> new Thread(new querry(query_buf)).start());
            }
            else {
                connect = new Thread(new motor_efficiency.connect());
                connect.start();
            }
        }
    };
    class querry implements Runnable {
        private char[] output_message;

        querry(char[] message) {
            this.output_message = message;
        }

        @Override
        public void run() {
            if (output != null) {
                if (l_cmd != 0) {
                    if (l_cmd > 0 && l_cmd < 5) {
                        if (l_cmd == 1) {
                            l_buf[7] = 0xF4;
                            l_buf[6] = 0x47;
                            output_message = l_buf;
                            Log.d("Left_cmd: ", "l_buf");
                            l_cmd = 0;
                        } else if (l_cmd == 2) {
                            output_message = l_start;
                            l_start_cmd_cnt++;
                            if (l_start_cmd_cnt > cmd_repeat_cnt) {
                                l_cmd = 0;
                                l_start_cmd_cnt = 0;
                            }
                            Log.d("Left_cmd: ", "l_start");
                        } else if (l_cmd == 3) {
                            output_message = l_stop;
                            l_stop_cmd_cnt++;
                            if (l_stop_cmd_cnt > cmd_repeat_cnt) {
                                l_cmd = 0;
                                l_stop_cmd_cnt = 0;
                            }
                            Log.d("Left_cmd: ", "l_stop");
                        } else {
                            output_message = query_buf;
                            l_cmd = 0;
                        }
                    }
                }
                else if (r_cmd != 0) {
                    if (r_cmd > 0 && r_cmd < 5) {
                        if (r_cmd == 1) {
                            r_buf[5] = 0xF4;
                            r_buf[4] = 0x47;
                            output_message = r_buf;
                            Log.d("Right_cmd: ", "r_buf");
                            r_cmd = 0;
                        } else if (r_cmd == 2) {
                            output_message = r_start;
                            r_start_cmd_cnt++;
                            if (r_start_cmd_cnt > cmd_repeat_cnt) {
                                r_cmd = 0;
                                r_start_cmd_cnt = 0;
                            }
                            Log.d("Right_cmd: ", "r_start");
                        } else if (r_cmd == 3) {
                            output_message = r_stop;
                            r_stop_cmd_cnt++;
                            if (r_stop_cmd_cnt > cmd_repeat_cnt) {
                                r_cmd = 0;
                                r_stop_cmd_cnt = 0;
                            }
                            Log.d("Right_cmd: ", "r_stop");
                        } else {
                            output_message = query_buf;
                            r_cmd = 0;
                        }
                    }
                }/*
                else if(spd_update) {
                    speed_set[5] = (char) ((PWM_B & 0xFF00) >> 8);
                    speed_set[4] = (char) (PWM_B & 0xFF);

                    speed_set[7] = (char) ((PWM_A & 0xFF00) >> 8);
                    speed_set[6] = (char) (PWM_A & 0xFF);

                    output_message = speed_set;
                    spd_update=false;
                }*/
                int checksum = 0;
                for (int i = 0; i < 8; i++) {
                    checksum += output_message[i];
                }
                output_message[8] = (char) (checksum & 0xFF);
                if ((output_message != query_buf) || true) {
                    output.write(output_message);
                    output.flush();
                }
            } else {
                Log.d("write error", "write error");
            }
        }
    }
}
