package com.scwang.refreshlayout.activity.Task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;

import java.util.ArrayList;
import java.util.List;

public class WeekAlarmReceiver extends BroadcastReceiver {
    private List<AVObject> mList = new ArrayList<>();
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("action".equals(intent.getAction())) {
            AVQuery<AVObject> avQuery = new AVQuery<>(AVUser.getCurrentUser().getUsername());
            try {
                mList = avQuery.find();
            } catch (AVException e) {
                e.printStackTrace();
            }
            for (int i=0;i<mList.size();i++)
            {
                if (mList.get(i).getInt("Type")==3) {
                    String id = mList.get(i).getObjectId();
                    AVObject avObject = AVObject.createWithoutData(AVUser.getCurrentUser().getUsername(), id);
                    avObject.put("times",0);
                    avObject.put("status", true);
                    avObject.saveInBackground();
                }
            }
            return;
        }
    }
}
