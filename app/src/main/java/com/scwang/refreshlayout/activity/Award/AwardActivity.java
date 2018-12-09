package com.scwang.refreshlayout.activity.Award;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.scwang.refreshlayout.R;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AwardActivity extends AppCompatActivity {
    private String selectedItem;

    private RefreshLayout mRefreshLayout;
    private static boolean isFirstEnter = true;
    private ArrayList<AVObject> mList = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);
        Button button1 = (Button)findViewById(R.id.button);
        Button button2 = (Button)findViewById(R.id.button2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(AwardActivity.this, addAwardActivity.class));
            }
        });
        ListView listView = (ListView) findViewById(
                R.id.listView1);
        String[] titles;
        File parentFile = new File("/data/data/com.scwang.refreshlayout.app/filesAward");
        if (parentFile.exists()){
            File[] files = parentFile.listFiles();
            Award[] awards = new Award[files.length];
            titles = new String[files.length];
            for (int i=0;i<files.length;i++)
            {
                File file = files[i];
                FileReader fileReader = null;
                BufferedReader bufferedReader = null;
                try {
                    fileReader = new FileReader(file);
                    bufferedReader = new BufferedReader(fileReader);
                    StringBuilder sb = new StringBuilder();
                    String line = bufferedReader.readLine();
                    while (line != null) {
                        sb.append(line+" ");
                        line = bufferedReader.readLine();
                    }
                    titles[i] =sb.toString();//将待显示的文字改为Award中的标题+成就点数+次数

                } catch (IOException e) {
                }
                finally {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                        }
                    }
                    if (fileReader != null) {
                        try {
                            fileReader.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
            // 对其排序
//        for (int j=0;j<titles.length;j++)
//        {
//            awards[j] = transferAward(titles[j]);
//        }
//        Sorting.shellSort(awards);
//        for (int i=0;i<awards.length;i++)
//        {
//            titles[i] = awards[i].toString();//awards[i].getName()+"             "+"-"+awards[i].getScore()+"/次（"+awards[i].getTimes()+"次)";
//        }
        }
        else
        {
            AVQuery<AVObject> avQuery = new AVQuery<>(AVUser.getCurrentUser().getUsername());
            avQuery.orderByDescending("createdAt");
            avQuery.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e)
                {
                    if (e == null) {
                        mList.addAll(list);
                    } else {
                        e.printStackTrace();
                    }
                }
            });
            titles = new String[mList.size()];
            for (int i=0;i<mList.size();i++)
            {
                titles[i] = mList.get(i).getString("Title")+"  成就点数： "+mList.get(i).getString("Scores")+" 次数 "+mList.get(i).getString("Totaltime");
            }
        }
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1,titles);
        listView.setAdapter(arrayAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView,
                                            View view, final int position, long id) {
                        final int index = position;
                        AlertDialog.Builder dialog = new AlertDialog.Builder(AwardActivity.this);
                        dialog.setTitle("满足奖励");
                        dialog.setMessage("花费成就点数来满足奖励");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File parentFile = new File("/data/data/com.scwang.refreshlayout.app/filesAward");
                                if (parentFile.exists()){
                                    String[] titles = parentFile.list();

                                if (titles.length >index) {
                                    selectedItem = titles[index];
                                    try {
                                        deleteNote();
                                    } catch (AVException e) {
                                        e.printStackTrace();
                                    }
                                }}
                            }
                        });
                           dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {}
                           });
                           dialog.show();
                    }
                });

        mRefreshLayout = findViewById(R.id.refreshLayout);
        if (isFirstEnter) {
            isFirstEnter = false;
            mRefreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.award_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(AwardActivity.this, addAwardActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        refreshList();

    }

    private void refreshList() {

        AVQuery<AVObject> avQuery = new AVQuery<>(AVUser.getCurrentUser().getUsername());
        avQuery.orderByDescending("createdAt");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    mList.clear();
                    mList.addAll(list);
                } else {
                    e.printStackTrace();
                }
            }
        });
        ListView listView = (ListView) findViewById(
                R.id.listView1);
        String[] titles;
        File parentFile = new File("/data/data/com.scwang.refreshlayout.app/filesAward");
        if (parentFile.exists()){
            File[] files = parentFile.listFiles();
            Award[] awards = new Award[files.length];
            titles = new String[files.length];
            for (int i=0;i<files.length;i++)
            {
                File file = files[i];
                FileReader fileReader = null;
              BufferedReader bufferedReader = null;
               try {
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
                StringBuilder sb = new StringBuilder();
                String line = bufferedReader.readLine();
                while (line != null) {
                    sb.append(line+" ");
                    line = bufferedReader.readLine();
                }
               titles[i] =sb.toString();//将待显示的文字改为Award中的标题+成就点数+次数

            } catch (IOException e) {
                }
                finally {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                        }
                    }
                    if (fileReader != null) {
                        try {
                            fileReader.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
        // 对其排序
//        for (int j=0;j<titles.length;j++)
//        {
//            awards[j] = transferAward(titles[j]);
//        }
//        Sorting.shellSort(awards);
//        for (int i=0;i<awards.length;i++)
//        {
//            titles[i] = awards[i].toString();//awards[i].getName()+"             "+"-"+awards[i].getScore()+"/次（"+awards[i].getTimes()+"次)";
//        }
        }
        else
        {
            titles = new String[mList.size()];
            for (int i=0;i<mList.size();i++)
            {
                titles[i] = mList.get(i).getString("Title")+"  成就点数： "+mList.get(i).getString("Scores")+" 次数 "+mList.get(i).getString("Totaltime");
            }
        }
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1,titles);
        listView.setAdapter(arrayAdapter);
    }

    private void deleteNote() throws AVException {
        if (selectedItem != null) {

            String filename = "/data/data/com.scwang.refreshlayout.app/filesAward/" + selectedItem;
            File file = new File(filename);
            if (file.exists())
                file.delete();
//            final AVObject[] avObject = new AVObject[1];
//            final AVQuery avQuery = new AVQuery(AVUser.getCurrentUser().getUsername());
//            avQuery.whereEqualTo("Title", "xyz");
//            avQuery.findInBackground(new FindCallback<AVObject>() {
//                @Override
//                public void done(List<AVObject> list, AVException e) {
//                    avObject[0] = list.get(0);
//                }
//            });
            for (int i = 0; i < mList.size(); i++)
            {
                if ((mList.get(i).getString("Title").compareTo(selectedItem))==0)
                {

                    String objectId = mList.get(i).getObjectId();
                    String cql = " delete from "+AVUser.getCurrentUser().getUsername()+" where objectId = ? ";
                    AVQuery.doCloudQueryInBackground(cql, new CloudQueryCallback() {
                        @Override
                        public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                            if (e == null) {

                            } else {
                                e.printStackTrace();
                            }
                        }
                    }, objectId);
                }
            }

            selectedItem = null;
            refreshList();
        }
    }

//    private Award transferAward(String x)
//    {
//        StringTokenizer stringTokenizer = new StringTokenizer(x,"|");
//        Award award = new Award(stringTokenizer.nextToken(),Integer.parseInt(stringTokenizer.nextToken()),Integer.parseInt(stringTokenizer.nextToken()),0,0);
//
//        return  award;
//    }
}

