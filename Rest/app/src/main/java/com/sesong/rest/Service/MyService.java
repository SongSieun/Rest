package com.sesong.rest.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.sesong.rest.ui.MainActivity;
import com.sesong.rest.R;

public class MyService extends Service {
    private static final String TAG = MyService.class.getSimpleName();
    private Thread mThread;
    private int mCount = 0;

    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mThread == null) {
            // 스레드 초기화 및 시작
            mThread = new Thread("My Thread") {
                @Override
                public void run() {
                    for (int i = 0; i < 360; i++) {
                        try {
                            mCount++;
                            // 1초 마다 쉬기
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            // 스레드에 인터럽트가 걸리면
                            // 오래 걸리는 처리 종료
                            break;
                        }
                        // 1초마다 로그 남기기
                        Log.d("MyService", "서비스 동작 중" + mCount);
                        sendMessage();
                    }
                    show();
                }
            };
            mThread.start();
        }
        return START_STICKY;
    }

    // stopService에 의해 호출됨
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        // 스레드를 정지시킴
        if (mThread != null) {
            mThread.interrupt();
            mThread = null;
        }
    }

    //브로드 캐스트 보내기
    private void sendMessage() {
        Intent intent = new Intent("CountingValue");
        intent.putExtra("Counting", mCount);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
}
