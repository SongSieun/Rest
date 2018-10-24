package com.sesong.rest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class MyService extends Service {
    private static final String TAG = MyService.class.getSimpleName();
    private Thread mThread;
    private int mCount = 0;
    // MyService의 레퍼런스를 반환하는 Binder 객체
    /*private IBinder mBinder = new MyBinder();

    public class MyBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }*/

    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mThread == null) {
            // 스레드 초기화 및 시작
            mThread = new Thread("My Thread") {
                @Override
                public void run() {
                    for (int i = 0; i < 60; i++) {
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
                }
            };
            mThread.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        // stopService에 의해 호출됨
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
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
/*

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }
*/

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        /*Log.d(TAG, "onBind: ");
        return mBinder;*/
        return null;
    }
/*
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    // 바인드 된 컴포넌트에 카운팅 변수값 제공
    public int getCount() {
        return mCount;
    }*/
}
