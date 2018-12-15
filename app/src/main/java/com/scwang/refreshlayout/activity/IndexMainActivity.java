package com.scwang.refreshlayout.activity;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;


import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.scwang.refreshlayout.R;
import com.scwang.refreshlayout.fragment.index.MineFragment;
import com.scwang.refreshlayout.fragment.index.TaskFragment;
import com.scwang.refreshlayout.fragment.index.AwardFragment;
import com.scwang.refreshlayout.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class IndexMainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

    private int stars;
    private AVObject avObject;
    private List<AVObject> mList = new ArrayList<>();

    private enum TabFragment {
        任务(R.id.navigation_practice, TaskFragment.class),
        奖励(R.id.navigation_style, AwardFragment.class),
        我的(R.id.navigation_example, MineFragment.class),
        ;

        private Fragment fragment;
        private final int menuId;
        private final Class<? extends Fragment> clazz;

        TabFragment(@IdRes int menuId, Class<? extends Fragment> clazz) {
            this.menuId = menuId;
            this.clazz = clazz;
        }

        @NonNull
        public Fragment fragment() {
            if (fragment == null) {
                try {
                    fragment = clazz.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    fragment = new Fragment();
                }
            }
            return fragment;
        }

        public static TabFragment from(int itemId) {
            for (TabFragment fragment : values()) {
                if (fragment.menuId == itemId) {
                    return fragment;
                }
            }
            return 奖励;
        }

        public static void onDestroy() {
            for (TabFragment fragment : values()) {
                fragment.fragment = null;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_main);
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        ViewPager viewPager = findViewById(R.id.content);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return TabFragment.values().length;
            }
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = TabFragment.values()[position].fragment();
                Bundle bundle = new Bundle();
                try {
                    bundle.putString("scores", String.valueOf(getStars()));
                } catch (AVException e) {
                    e.printStackTrace();
                }
                fragment.setArguments(bundle);
                return fragment;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                navigation.setSelectedItemId(TabFragment.values()[position].menuId);
            }
        });
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this, 0xff000000, 0.1f);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TabFragment.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AVAnalytics.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        AVAnalytics.onPause(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        ((ViewPager)findViewById(R.id.content)).setCurrentItem(TabFragment.from(item.getItemId()).ordinal());
        return true;
    }
    private void initData()
    {
        mList.clear();
        AVQuery<AVObject> avQuery1 =new AVQuery<>("Data_table");
        avQuery1.orderByDescending("createdAt");
        avQuery1.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    mList.addAll(list);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
    public int getStars() throws AVException {
        if(AVUser.getCurrentUser().getUsername()==null)
            stars =0;
        else {
            final String name = AVUser.getCurrentUser().getUsername();
            mList.clear();
            AVQuery<AVObject> avQuery =new AVQuery<>("Data_table");
            mList = avQuery.find();
            for (int i =0;i<mList.size();i++)
            {
                if (name.compareTo(mList.get(i).getString("Name"))==0)
                {
                    avObject = mList.get(i);
                    break;
                }
            }
            if (avObject==null)
                stars = 0;
            else
                stars = avObject.getInt("Scores");
        }
        return stars;
    }

}
