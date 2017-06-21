package com.badon.brigham.notify.intro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badon.brigham.notify.R;

public abstract class IntroBaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.intro_tab, container, false);

        ViewGroup innerView = (ViewGroup) view.findViewById(R.id.tab_card_view);
        onInflateInnerView(inflater, innerView);

        return view;
    }

    /**
     * Used to inflate the inner view based on the default outline or to override the inner view
     * layout and inflate a custom layout. Subclasses that override this method must call super.
     *
     * @param inflater  A LayoutInflater that can be used to inflate and attach a custom layout, if
     *                  need be
     * @param container The inner view container
     */
    public void onInflateInnerView(LayoutInflater inflater, ViewGroup container) {
        if (getOverrideInnerView()) {
            container.removeAllViews();
        }
    }

    /**
     * A wrapper for the {@link #getActivity()} method that casts the generic Activity to
     * IntroBaseActivity
     *
     * @return The Activity this Fragment is attached to
     */
    public final IntroBaseActivity getIntroActivity() {
        return (IntroBaseActivity) getActivity();
    }

    /**
     * Called when the Fragment is rendering the layout
     *
     * @return Whether or not the Fragment will remove the default inner view outline
     */
    public abstract boolean getOverrideInnerView();

    /**
     * Called when the tab should save any data (preferences, etc.) it is responsible for managing
     */
    public void saveData() {
    }
}