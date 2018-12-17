package com.scwang.refreshlayout.activity.Task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.scwang.refreshlayout.activity.Task.LongRunningService;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context,LongRunningService.class);
        context.startService(i);
    }
}
