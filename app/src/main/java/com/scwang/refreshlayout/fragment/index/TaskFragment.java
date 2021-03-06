package com.scwang.refreshlayout.fragment.index;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;


import com.scwang.refreshlayout.R;

import com.scwang.refreshlayout.activity.FragmentActivity;

import com.scwang.refreshlayout.activity.MenuButton.SrcMenu;
import com.scwang.refreshlayout.activity.Task.DayTaskActivity;

import com.scwang.refreshlayout.activity.Task.TaskActivity;
import com.scwang.refreshlayout.activity.Task.WeekTaskActivity;
import com.scwang.refreshlayout.adapter.BaseRecyclerAdapter;
import com.scwang.refreshlayout.adapter.SmartViewHolder;
import com.scwang.refreshlayout.countDown.CountdownActivity;
import com.scwang.refreshlayout.util.StatusBarUtil;
import com.scwang.smartrefresh.header.FunGameBattleCityHeader;
import com.scwang.smartrefresh.header.FunGameHitBlockHeader;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Arrays;


import static android.R.layout.simple_list_item_2;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class TaskFragment extends Fragment implements AdapterView.OnItemClickListener {

    private SrcMenu mSrcMenu;
    private TextView textView;

    private enum Item {
        DayTask(R.string.index_practice_repast, DayTaskActivity.class),
        WeekTask(R.string.title_activity_week_task, WeekTaskActivity.class),
        Task(R.string.title_activity_normalActivity, TaskActivity.class),
        Absorption(R.string.index_task_countdown,CountdownActivity.class)
        ;
        @StringRes
        public int name;
        public Class<?> clazz;
        Item(@StringRes int name, Class<?> clazz) {
            this.name = name;
            this.clazz = clazz;
        }
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refresh_practive, container, false);
        textView= (TextView) view.findViewById(R.id.scores_tv);
        initView(view);
        return view;
    }
    private void initView(View view) {
        String title= (String) getArguments().get("scores");

        textView.setText(title);
    }


    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);
        StatusBarUtil.setPaddingSmart(getContext(), root.findViewById(R.id.toolbar));


        final View view1 = root.findViewById(R.id.recyclerView);

        if (view1 instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view1;
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
            recyclerView.setAdapter(new BaseRecyclerAdapter<Item>(Arrays.asList(Item.values()), simple_list_item_2,this) {
                @Override
                protected void onBindViewHolder(SmartViewHolder holder, Item model, int position) {
                    holder.text(android.R.id.text1, model.name());
                    holder.text(android.R.id.text2, model.name);
                    holder.textColorId(android.R.id.text2, R.color.colorTextAssistant);
                }
            });
        }
        RefreshLayout refreshLayout = root.findViewById(R.id.refreshLayout);
        if (refreshLayout != null) {
            refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                    initView(view1);
                    refreshLayout.finishRefresh(3000);
                    refreshLayout.getLayout().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            RefreshHeader refreshHeader = refreshLayout.getRefreshHeader();
                            if (refreshHeader instanceof FunGameHitBlockHeader) {
                                refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
                            } else if(refreshHeader instanceof FunGameBattleCityHeader) {
                                refreshLayout.setRefreshHeader(new FunGameHitBlockHeader(getContext()));
                            }
                            refreshLayout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
                        }
                    },4000);
                }
            });
        }
        mSrcMenu = (SrcMenu) root.findViewById(R.id.src_menu);
        mSrcMenu.setOnMenuItemClickListener(new SrcMenu.OnMenuItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case 1:
                        startActivity(new Intent(getContext(),DayTaskActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(getContext(),WeekTaskActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(getContext(),TaskActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(getContext(),CountdownActivity.class));
                        break;
                }
            }
        });


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Item item = Item.values()[position];
        if (Activity.class.isAssignableFrom(item.clazz)) {
            startActivity(new Intent(getContext(), item.clazz));
        } else if (Fragment.class.isAssignableFrom(item.clazz)) {
            FragmentActivity.start(this, item.clazz);
        }
    }
}
