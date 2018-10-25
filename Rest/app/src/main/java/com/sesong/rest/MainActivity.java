package com.sesong.rest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView mainText;
    private Button eyeStretch;
    private ImageView rightSeat, phoneNoti, blueScreen;
    private Handler handler = new Handler();
    private Boolean servicePowerFlag = false;
    private Boolean mainTextFlag = false;
    private Vibrator vibrator;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mainText = (TextView) findViewById(R.id.maintext);
        rightSeat = (ImageView) findViewById(R.id.rightseat);
        phoneNoti = (ImageView) findViewById(R.id.noti);
        eyeStretch = (Button) findViewById(R.id.eyesBtn);
        blueScreen = (ImageView) findViewById(R.id.bluescreen);

        rightSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        phoneNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeNoti(v);
            }
        });
        eyeStretch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainTextFlag = true;
                eyeStretchFun();
            }
        });
        blueScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void eyeStretchFun() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<String> stretching = new ArrayList<>();
                stretching.add("초점을 맞추지 않은 채로 가볍게 위를 본다");
                stretching.add("눈을 감는다");
                stretching.add("눈을 최대한 부릅뜬다");
                stretching.add("양쪽 시선을 우측으로 고정한다");
                stretching.add("양쪽 시선을 좌측으로 고정한다");
                stretching.add("양쪽 시선을 위쪽으로 고정한다");
                stretching.add("양쪽 시선을 아래쪽으로 고정한다");
                stretching.add("눈 주위를 누르며\n\n안쪽에서 바깥쪽으로 문질러준다");
                stretching.add("눈꺼풀 위를 가볍게 누른다");
                stretching.add(":)");

                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int sentence = i;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mainText.setTextSize(12);
                            mainText.setText(stretching.get(sentence));
                            vibrator.vibrate(200);
                        }
                    });
                }
                mainTextFlag = false;
            }
        }).start();
    }

    public void setTimeNoti(final View view) {
        servicePowerFlag = !servicePowerFlag;
        if (servicePowerFlag == true) {
            Intent intent = new Intent(this, MyService.class);
            startService(intent);
        } else {
            Intent intent = new Intent(this, MyService.class);
            stopService(intent);
        }
    }

    public void showCountText(int mCount) {
        if (!mainTextFlag) {
            mainText.setTextSize(100);
            mainText.setText(String.valueOf(mCount));
        }
        progressBar.setProgress(mCount);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("CountingValue"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int message = intent.getIntExtra("Counting", 0);
            Log.d("message ", String.valueOf(message));
            showCountText(message);
        }
    };

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }
}