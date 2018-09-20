package com.sesong.phoneusenoti;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Random;

public class MyService extends Service {
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    int setNum = 0;

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        MyService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MyService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 60; i++)
                {
                    try {
                        Thread.sleep(500);
                        setNum = i;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** method for clients */
    public int changeTick(){
        return setNum;
    }
}
