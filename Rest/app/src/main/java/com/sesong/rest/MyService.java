package com.sesong.rest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyService extends Service {
    private static final String TAG = MyService.class.getSimpleName();
    private Thread mThread;
    private int mCount = 0;
    // MyService의 레퍼런스를 반환하는 Binder 객체
    private IBinder mBinder = new MyBinder();

    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 포그라운드 서비스 시작
        startForegroundService();
        if (mThread == null) {
            // 스레드 초기화 및 시작
            mThread = new Thread("My Thread") {
                @Override
                public void run() {
                    for (int i = 0; i < 60; i++) {
                        try {
                            mCount++;
                            // 1초 마다 쉬기
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            // 스레드에 인터럽트가 걸리면
                            // 오래 걸리는 처리 종료
                            break;
                        }
                        // 1초마다 로그 남기기
                        Log.d("MyService", "서비스 동작 중" + mCount);
                    }
                }
            };
            mThread.start();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    public class MyBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }

    // 바인드된 컴포넌트에 카운팅 변숫값 제공
    public int getCount() {
        return mCount;
    }

    private void startForegroundService() {
        // default 채널 ID로 알림 생성
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.drawable.mainicon);
        builder.setContentTitle("휴식");
        builder.setContentText("바른자세, 사용알림 기능 작동중");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent);
        // 오레오에서는 알림 채널을 매니저에 생성해야 한다
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }
        // 포그라운드로 시작
        startForeground(1, builder.build());
    }
}
