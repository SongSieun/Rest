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
        mainTextFlag = true;
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
            }
        }).start();
        mainTextFlag = false;
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

    public void showCountText(String mCount) {
        if (!mainTextFlag){
            mainText.setText(mCount);
        }
        progressBar.setProgress(Integer.parseInt(mCount));
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
            String message = intent.getStringExtra("Counting");
            showCountText(message);
        }
    };

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    private void show() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        // 필수 항목
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("스마트폰 사용시간 경고");
        builder.setContentText("과도한 스마트폰의 사용은 눈 건강에 해로울 수 있습니다.");
        // 액션 정의
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 클릭 이벤트 설정
        builder.setContentIntent(pendingIntent);
        // 큰 아이콘 설정
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.mainicon);
        builder.setLargeIcon(largeIcon);
        // 색상 변경
        builder.setColor(Color.RED);
        // 기본 알림음 사운드 설정
        Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(ringtoneUri);
        // 진동 설정: 대기시간, 진동시간, 대기시간, 진동시간 ... 반복패턴
        long[] vibrate = {0, 100, 200, 300};
        builder.setVibrate(vibrate);
        builder.setAutoCancel(true);
        // 알림 매니저
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // 오레오에서는 알림 채널을 매니저에 생성해야 한다
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }
        // 알림 통지
        manager.notify(1, builder.build());
    }
/*

    @Override
    protected void onStart() {
        super.onStart();
        // 서비스에 바인딩
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 서비스와 연결 해제
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }
*/

    /**
     * bindService()를 통해 서비스와 연결될 때의 콜백 정의
     *//*
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // MyBinder와 연결될 것이며 IBinder 타입으로 넘어오는 것을 캐스팅하여 사용
            MyService.MyBinder binder = (MyService.MyBinder) service;
            myService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 예기치 않은 종료
        }
    };*/
}