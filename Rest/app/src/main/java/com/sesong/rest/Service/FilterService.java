package com.sesong.rest.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class FilterService extends Service implements SensorEventListener {
    private View view;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private SensorManager sensorManager;
    private Sensor sensor;
    public int mSensorCount = 0;
    private int sensorVaule;
    private final Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        // 센서 서비스를 얻어오고 조도센서와 연결
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        // 센서를 센서 매니저에 등록
        sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_UI);

        view = new FilterView(this);
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
        );
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(view, params);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    // stopService에 의해 호출됨
    @Override
    public void onDestroy() {
        super.onDestroy();
        ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(view);
        view = null;
        // 등록된 센서를 unregister
        sensorManager.unregisterListener(this);
    }

    public FilterService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            mSensorCount++;
            String str;

            // 조도 센서의 값은 event.values[0]에 있음
            str = "Brightness Sensor Value : " + event.values[0] + "lux";
            sensorVaule = (int) event.values[0];
            Log.d("Sensor_Data ", str);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private class FilterView extends View {
        private Paint paint;

        public FilterView(Context context) {
            super(context);
            paint = new Paint();
            paint.setTextSize(100);
            paint.setARGB(200, 10, 10, 10);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (sensorVaule < 10) {
                canvas.drawARGB(100, 255, 212, 0);
            }
            handler.postDelayed(runnable, 1000);
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
                handler.postDelayed(this, 15000);
            }
        };
    }
}
