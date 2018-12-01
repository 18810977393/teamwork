package com.scwang.refreshlayout.ViewPager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewpagerAdpter extends FragmentPagerAdapter {
    private List<Fragment> fragments = new ArrayList<>();

    public  ViewpagerAdpter(FragmentManager fm){
        super(fm);
    }

    public  ViewpagerAdpter(FragmentManager fm,List<Fragment> fragments){
        super(fm);

        this.fragments =fragments;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
}
