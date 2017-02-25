package com.badon.brigham.notify.intro;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import com.badon.brigham.notify.R;

/**
 * An Activity that manages a ViewPager to be used in conjunction with Intro Fragments
 */
public abstract class IntroBaseActivity extends FragmentActivity {

    private ViewPager mPager;
    private IntroAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        setContentView(R.layout.activity_intro);

        mAdapter = new IntroAdapter(getSupportFragmentManager());
        onAddTabs();

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();

        // When the Activity is paused, tell the Fragment to save it's data
        getCurrentFragment().saveData();
    }

    /**
     * Called when the activity has created the IntroAdapter, but has not yet attached it. Make all
     * calls of {@link #addTab(IntroBaseFragment)} from here.
     */
    protected abstract void onAddTabs();

    /**
     * Adds a tab to the underlying ViewPager in the order it is called. This method should only be
     * called from {@link #onAddTabs()}.
     *
     * @param fragment The fragment that will be added to the underlying ViewPager
     */
    public void addTab(IntroBaseFragment fragment) {
        mAdapter.addTab(fragment);
    }

    /**
     * Sets the page of the underlying ViewPager
     *
     * @param page The page to jump to
     */
    public void setCurrentPage(int page) {
        int currentItem = mPager.getCurrentItem();
        if (page != currentItem) {
            // When tabs are switched, tell the fragment to save it's data
            getCurrentFragment().saveData();

            mPager.setCurrentItem(page);
        }
    }

    /**
     * Moves forward one tab, if not at the last tab
     */
    public void backward() {
        int currentItem = mPager.getCurrentItem();
        if (currentItem > 0) {
            currentItem--;
        }
        setCurrentPage(currentItem);
    }

    /**
     * Moves backward one tab, if not at the first tab
     */
    public void forward() {
        int currentItem = mPager.getCurrentItem();
        if (currentItem < mAdapter.getCount()) {
            currentItem++;
        }
        setCurrentPage(currentItem);
    }

    /**
     * Called to get an instance of the Fragment that is currently in view
     *
     * @return The Fragment that is currently in view
     */
    private IntroBaseFragment getCurrentFragment() {
        int currentItem = mPager.getCurrentItem();
        return (IntroBaseFragment) mAdapter.getItem(currentItem);
    }

    /**
     * Called when the intro has finished, must call super
     */
    public void introFinish() {
        // When the intro is finished, tell the fragment to save it's data
        getCurrentFragment().saveData();
    }
}