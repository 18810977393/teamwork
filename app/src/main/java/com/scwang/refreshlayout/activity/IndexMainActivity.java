package com.scwang.refreshlayout.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.scwang.refreshlayout.R;
import com.scwang.refreshlayout.activity.menu.SrcMenu;
import com.scwang.refreshlayout.fragment.index.RefreshExampleFragment;
import com.scwang.refreshlayout.fragment.index.RefreshPracticeFragment;
import com.scwang.refreshlayout.fragment.index.RefreshStylesFragment;
import com.scwang.refreshlayout.util.StatusBarUtil;

public class IndexMainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

//    private SrcMenu mSrcMenu;

    private enum TabFragment {
        任务(R.id.navigation_practice, RefreshPracticeFragment.class),
        奖励(R.id.navigation_style, RefreshStylesFragment.class),
        我的(R.id.navigation_example, RefreshExampleFragment.class),
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
                return TabFragment.values()[position].fragment();
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

//        //
//        mSrcMenu = (SrcMenu) findViewById(R.id.src_menu);
//        mSrcMenu.setOnMenuItemClickListener(new SrcMenu.OnMenuItemClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                switch (position){
//                    case 1:
//                        Toast.makeText(IndexMainActivity.this, position + ":" + view.getTag(), Toast.LENGTH_SHORT)
//                                .show();
//                        break;
//                    case 2:
//                        Toast.makeText(IndexMainActivity.this, position + ":" + view.getTag(), Toast.LENGTH_SHORT)
//                                .show();
//                        break;
//                    case 3:
//                        Toast.makeText(IndexMainActivity.this, position + ":" + view.getTag(), Toast.LENGTH_SHORT)
//                                .show();
//                        break;
//                    case 4:
//                        Toast.makeText(IndexMainActivity.this, position + ":" + view.getTag(), Toast.LENGTH_SHORT)
//                                .show();
//                        break;
//                }
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TabFragment.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        ((ViewPager)findViewById(R.id.content)).setCurrentItem(TabFragment.from(item.getItemId()).ordinal());
        return true;
    }



}
