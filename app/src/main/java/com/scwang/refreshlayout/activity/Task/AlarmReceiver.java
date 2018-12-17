package com.scwang.refreshlayout.activity.Task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {
    private List<AVObject> mList = new ArrayList<>();
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("action".equals(intent.getAction())) {
            AVQuery<AVObject> avQuery = new AVQuery<>("Nama");
            try {
                mList = avQuery.find();
            } catch (AVException e) {
                e.printStackTrace();
            }
            for (int i=0;i<mList.size();i++)
            {
                if (mList.get(i).getInt("Type")==2) {
                    String id = mList.get(i).getObjectId();
                    AVObject avObject = AVObject.createWithoutData("Nama", id);
                    avObject.put("status", true);
                    avObject.saveInBackground();
                }
            }
//            AVObject avObject = new AVObject("Nama");
//            avObject.put("Title","uiuiuiuuiui");
//            avObject.saveInBackground();
            return;
        }
    }
}
