package com.scwang.refreshlayout.activity.Award;

import android.app.AlertDialog;
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
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;

import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.scwang.refreshlayout.R;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MainRecyclerAdapter mRecyclerAdapter;
    private List<AVObject> mList = new ArrayList<>();
    private Toolbar mToolbar;
    private RefreshLayout mRefreshLayout;
    private AVObject avObject;
    private int stars;
    private List<AVObject> List = new ArrayList<>();
    final int Menu_1 = Menu.FIRST;
    final int Menu_2 = Menu.FIRST + 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,addAwardActivity.class));
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRecyclerAdapter = new MainRecyclerAdapter(mList, MainActivity.this);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.setOnItemClickListener(new MainRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("满足奖励");
                        dialog.setMessage("花费星星来满足奖励");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateStars(position);
                                if (mList.get(position).getString("Totaltime").compareTo("∞")==0)
                                {
                                    String objectId = mList.get(position).getObjectId();
                                    Integer a = mList.get(position).getInt("times")+1;
                                    AVObject todo = AVObject.createWithoutData(AVUser.getCurrentUser().getUsername(), objectId);
                                    todo.put("times",a);
                                    // 保存到云端
                                    todo.saveInBackground();
                                }
                                else {
                                    delete(position);
                                }
                                initData();
                            }
                        });
                        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        });
                        dialog.show();
            }
        });
        mRecyclerAdapter.setOnItemLongClickListener(new MainRecyclerAdapter.OnRecyclerItemLongListener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                AlertDialog.Builder dialog  = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("删除奖励");
                dialog.setMessage("确认删除该奖励");
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

        mRecyclerView.setItemAnimator( new DefaultItemAnimator());
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
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

    private void initData() {
        mList.clear();
        List.clear();
        AVQuery<AVObject> avQuery = new AVQuery<>(AVUser.getCurrentUser().getUsername());
        avQuery.whereEqualTo("Type",1);
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
        AVQuery<AVObject> avQuery1 =new AVQuery<>("Data_table");
        avQuery1.orderByDescending("createdAt");
        avQuery1.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    List.addAll(list);
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
    private void updateStars(int position)
    {
        String name = AVUser.getCurrentUser().getUsername();
        for (int i=0;i<List.size();i++)
        {
            if (name.compareTo(List.get(i).getString("Name"))==0)
            {
                avObject = List.get(i);
                break;
            }
        }
        stars = avObject.getInt("Scores");
        int scores =Integer.parseInt(mList.get(position).getString("Scores"));
        String id = avObject.getObjectId();
        AVObject todo = AVObject.createWithoutData("Data_table",id);
        todo.put("Scores",stars-scores);
        todo.saveInBackground();
    }
}