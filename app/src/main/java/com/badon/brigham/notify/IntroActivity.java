package com.badon.brigham.notify;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.badon.brigham.notify.util.SettingsManager;

public class IntroActivity extends FragmentActivity {
    static final int NUM_ITEMS = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setContentView(R.layout.activity_intro);

        IntroAdapter mAdapter = new IntroAdapter(getSupportFragmentManager());

        ViewPager mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(0);
    }

    public class IntroAdapter extends FragmentPagerAdapter {
        public IntroAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            IntroFragment f = new IntroFragment();

            Bundle args = new Bundle();
            args.putInt("num", position);
            f.setArguments(args);
            return f;
        }
    }

    public static class IntroFragment extends Fragment {
        int mNum;

        static Fragment newInstance(int num) {
            Fragment f = new Fragment();

            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);

            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments().getInt("num");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = null;
            switch (mNum) {
                case 0:
                    v = inflater.inflate(R.layout.intro_tab_1, container, false);
                    break;
                case 1:
                    v = inflater.inflate(R.layout.intro_tab_2, container, false);
                    break;
                case 2:
                    v = inflater.inflate(R.layout.intro_tab_3, container, false);
                    break;
                case 3:
                    v = inflater.inflate(R.layout.intro_tab_4, container, false);
                    break;
            }
            return v;
        }
    }

    public void saveKey(View view) {
        EditText apikey_input = (EditText) findViewById(R.id.step_1_input);
        String apiKey = apikey_input.getText().toString();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putString("apiKey", apiKey).apply();
        Toast.makeText(getApplicationContext(), "API Key Saved", Toast.LENGTH_LONG).show();
        ViewPager mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setCurrentItem(2);
    }

    public void openSettings(View view) {
        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        ViewPager mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setCurrentItem(3);
    }

    public void finish(View view) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String apiKey = prefs.getString("apiKey", "");
        if(LIFXNotify.NOTIFICATION_ACCESS && !apiKey.isEmpty()) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.setup_incomplete_dialog)
                    .setTitle(R.string.setup_incomplete_dialog_title)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ViewPager mPager = (ViewPager) findViewById(R.id.pager);
                            mPager.setCurrentItem(0);
                        }
                    });
            builder.create().show();
        }
    }
}

