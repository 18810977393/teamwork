package com.scwang.refreshlayout.activity.Task;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.FindCallback;
import com.scwang.refreshlayout.R;
import com.scwang.refreshlayout.util.StatusBarUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class WeekTaskActivity extends AppCompatActivity {
    private DayTaskRecyclerAdapter mRecyclerAdapter;
    private static boolean isFirstEnter = true;
    private List<AVObject> mList = new ArrayList<>();
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_task);
        Calendar calendar = Calendar.getInstance();
        long currentTime = calendar.getTimeInMillis();
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH),
                0, 0,0);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        if (currentTime > calendar.getTimeInMillis()) {
            calendar.add(Calendar.DATE, 1);
        }
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(WeekTaskActivity.this,
                WeekAlarmReceiver.class);
        intent.setAction("action");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                WeekTaskActivity.this,0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), 1000 * 60 * 60 * 24 * 7,
                pendingIntent);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WeekTaskActivity.this,addWeekTaskActivity.class));
            }
        });
        final RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true);

        //第一次进入演示刷新
        if (isFirstEnter) {
            isFirstEnter = false;
            refreshLayout.autoRefresh();
        }

        //初始化列表和监听
        View view = findViewById(R.id.recyclerView);
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerAdapter = new DayTaskRecyclerAdapter(mList, WeekTaskActivity.this);
            recyclerView.setAdapter(mRecyclerAdapter);
            mRecyclerAdapter.setOnItemClickListener(new DayTaskRecyclerAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, final int data) {
                    modify(data);
                }
            });
            mRecyclerAdapter.setOnItemLongClickListener(new DayTaskRecyclerAdapter.OnRecyclerItemLongListener() {
                @Override
                public void onItemLongClick(View view, final int position) {
                    AlertDialog.Builder dialog  = new AlertDialog.Builder(WeekTaskActivity.this);
                    dialog.setTitle("删除任务");
                    dialog.setMessage("确认删除该任务");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            delete(position);
                            initData();
                            Toast toast=Toast.makeText(getApplicationContext(), "已删除", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });
                    dialog.show();
                }
            });

            refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    initData();
                }

                @Override
                public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                    refreshLayout.getLayout().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            initData();
                            refreshLayout.finishRefresh();
                            refreshLayout.setNoMoreData(false);//恢复上拉状态
                        }
                    }, 2000);
                }
            });

            refreshLayout.getLayout().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setHeaderInsetStart(DensityUtil.px2dp(toolbar.getHeight()));
                }
            }, 500);
        }

        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this);
        StatusBarUtil.setPaddingSmart(this, view);
        StatusBarUtil.setPaddingSmart(this, toolbar);
        StatusBarUtil.setPaddingSmart(this, findViewById(R.id.blurView));
    }
    private void initData() {
        mList.clear();
        AVQuery<AVObject> avQuery1 = new AVQuery<>(AVUser.getCurrentUser().getUsername());
        avQuery1.whereEqualTo("Type",3);
        AVQuery<AVObject> avQuery2 = new AVQuery<>(AVUser.getCurrentUser().getUsername());
        avQuery2.whereEqualTo("status",true);
        AVQuery avQuery = AVQuery.and(Arrays.asList(avQuery1,avQuery2));
        avQuery.orderByDescending("createdAt");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    mList.addAll(list);
                    mRecyclerAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
    private void delete(int position)
    {
        String objectId = mList.get(position).getObjectId();
        String cql = " delete from "+AVUser.getCurrentUser().getUsername()+" where objectId = ? ";
        AVQuery.doCloudQueryInBackground(cql, new CloudQueryCallback() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                if (e == null) {
                } else {
                    e.printStackTrace();
                }
            }
        },objectId);
        initData();
    }
    private void modify(int position)
    {
        String objectId = mList.get(position).getObjectId();
        String title = mList.get(position).getString("Title");
        String scores = mList.get(position).getString("Scores");
        Intent intent = new Intent(WeekTaskActivity.this,ModifyWeekTaskActivity.class);
        intent.putExtra("objectId",objectId);
        intent.putExtra("title",title);
        intent.putExtra("scores",scores);
        startActivityForResult(intent, 3);
        initData();
    }
    @Override
    protected void onResume() {
        super.onResume();
        AVAnalytics.onResume(this);
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AVAnalytics.onPause(this);
    }
}
