package com.scwang.refreshlayout.activity.Task;

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
import java.util.List;

public class TaskActivity extends AppCompatActivity {
    private DayTaskRecyclerAdapter mRecyclerAdapter;
    private static boolean isFirstEnter = true;
    private List<AVObject> mList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
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
                startActivity(new Intent(TaskActivity.this,addTaskActivity.class));
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
            mRecyclerAdapter = new DayTaskRecyclerAdapter(mList, TaskActivity.this);
            recyclerView.setAdapter(mRecyclerAdapter);
            mRecyclerAdapter.setOnItemClickListener(new DayTaskRecyclerAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, final int data) {
                    modify(data);
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
        avQuery1.whereEqualTo("Type",4);
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
        Intent intent = new Intent(TaskActivity.this,ModifyTaskActivity.class);
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
