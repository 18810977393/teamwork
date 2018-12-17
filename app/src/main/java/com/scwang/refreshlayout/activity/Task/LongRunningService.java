package com.scwang.refreshlayout.activity.Task;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.avos.avoscloud.AVObject;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class LongRunningService extends Service {
    private static final String TAG = "LongRunningService";
    private static final int PENDING_REQUEST=0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run: executed at "+new Date().toString());
                AVObject avObject = new AVObject("Nama");
                avObject.put("Title","Namananahzy");
                avObject.saveInBackground();
            }
        }).start();

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        //获取当前毫秒值
        long systemTime = System.currentTimeMillis();

        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        mCalendar.set(Calendar.HOUR_OF_DAY, 19);

        mCalendar.set(Calendar.MINUTE,45);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);

        long selectTime = mCalendar.getTimeInMillis();

        if(systemTime > selectTime) {
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }


        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        Intent intent2 = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent2, 0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,mCalendar.getTimeInMillis(),pi);
        return super.onStartCommand(intent, flags, startId);
    }
}
