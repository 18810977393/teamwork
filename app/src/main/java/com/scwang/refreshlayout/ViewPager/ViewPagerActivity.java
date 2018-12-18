package com.scwang.refreshlayout.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;


import com.scwang.refreshlayout.R;
import com.scwang.refreshlayout.activity.IndexMainActivity;
import com.scwang.refreshlayout.activity.Mine.LoginActivity;


import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends AppCompatActivity {

    private final static int DELAY_TIME = 3000 ;
    String SHARE_APP_TAG ="Judge_the_firstTime";
    private ViewPager viewPager;
    private LinearLayout linearLayout;
    private PagerAdapter adapter;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_view_pager);

        viewPager = (ViewPager)findViewById(R.id.vp_viewGuide);
        linearLayout = findViewById(R.id.ll_indicator);

        init();
    }

    private void init() {
        for (int i=0;i<4;i++){
            ContentFragment fragment = new ContentFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("index",i);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }

        //Adapter的初始化
        adapter = new ViewpagerAdpter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
        //添加page滑动的监听事件，用于同步dot的变化
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                for (int i = 0 ; i < 4 ; i++)
                {
                    linearLayout.getChildAt(i).setBackgroundResource(i==position ? R.drawable.dot_selected : R.drawable.dot_normal);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initDot() ;
    }

    private void initDot() {
        int width = 30;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,width);
        lp.setMargins(35,35,35,35);

        for (int i = 0 ; i < 4 ; i++)
        {
            View view = new View(this) ;
            view.setId(i);
            view.setBackgroundResource(i==0 ? R.drawable.dot_selected : R.drawable.dot_normal);
            view.setLayoutParams(lp);
            /*
              实现对底部dot的点击也可以切换页面
             */
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(v.getId());

                }
            });
            linearLayout.addView(view);
        }
    }
}




























