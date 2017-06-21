package com.badon.brigham.notify.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.badon.brigham.notify.R;
import com.badon.brigham.notify.intro.IntroBaseFragment;

public class SplashFragment extends IntroBaseFragment {

    @Override
    public void onInflateInnerView(LayoutInflater inflater, ViewGroup container) {
        super.onInflateInnerView(inflater, container);

        inflater.inflate(R.layout.intro_tab_1, container, true);

        ImageButton rightButton = (ImageButton) container.findViewById(R.id.intro_icon_button_right);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIntroActivity().forward();
            }
        });
    }

    @Override
    public boolean getOverrideInnerView() {
        return true;
    }
}