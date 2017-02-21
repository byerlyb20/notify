package com.badon.brigham.notify.intro;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class IntroAdapter extends FragmentPagerAdapter {

    private ArrayList<IntroBaseFragment> mTabs = new ArrayList<>();

    public IntroAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mTabs.get(position);
    }

    public void addTab(IntroBaseFragment fragment) {
        mTabs.add(fragment);
    }
}
