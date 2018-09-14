package com.sesong.rest;

import android.app.Notification;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView mainText;
    Button eyeStretch;
    ImageView rightSeat, phoneNoti, blueScreen;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mainText = (TextView) findViewById(R.id.maintext);
        rightSeat = (ImageView) findViewById(R.id.rightseat);
        phoneNoti = (ImageView) findViewById(R.id.noti);
        eyeStretch = (Button) findViewById(R.id.eyesBtn);
        blueScreen = (ImageView) findViewById(R.id.bluescreen);

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
        mainText.setTextSize(12);

        rightSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        phoneNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNoti.setColorFilter(R.color.Salmon);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this);
                builder.setContentTitle("스마트폰을 사용한 지 " + i + "시간이 지났습니다.")
                        .setContentText("장시간 스마트폰 사용은 눈 건강에 해로울 수 있습니다.")
                .setDefaults(Notification.DEFAULT_SOUND)
                .setLargeIcon(R.drawable.mainicon)
                .setAutoCancel(true);

            }
        });
        eyeStretch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountDownTimer timer = new CountDownTimer(27000, 3000) {
                    int i = 0;
                    public void onTick(long millisUntilFinished) {
                        mainText.setText(stretching.get(i));
                        vibrator.vibrate(500);
                        i++;
                    }
                    public void onFinish() {
                        mainText.setTextSize(100);
                    }
                }.start();
                timer.cancel();
            }
        });
        blueScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
