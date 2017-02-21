package com.badon.brigham.notify.intro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.badon.brigham.notify.R;

public abstract class IntroFragment extends IntroBaseFragment {

    public static final int POSITION_MIDDLE = 0;
    public static final int POSITION_END = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onInflateInnerView(LayoutInflater inflater, ViewGroup container) {
        super.onInflateInnerView(inflater, container);

        // Set the title
        TextView title = (TextView) container.findViewById(R.id.intro_tab_title);
        title.setText(getTitleResource());

        // Set the title
        TextView msg = (TextView) container.findViewById(R.id.intro_tab_msg);
        msg.setText(getMessageResource());

        // Set the icon
        ImageView icon = (ImageView) container.findViewById(R.id.intro_tab_icon);
        icon.setImageResource(getIconResource());

        // Instantiate the action buttons
        ImageButton leftButton = (ImageButton) container.findViewById(R.id.intro_icon_button_left);
        ImageButton rightButton = (ImageButton) container.findViewById(R.id.intro_icon_button_right);
        instantiateActionButtons(leftButton, rightButton);

        // Instantiate the action view
        FrameLayout actionView = (FrameLayout) container.findViewById(R.id.introTabView);
        actionView.addView(onCreateActionView(inflater, actionView));
    }

    @Override
    public final boolean getOverrideInnerView() {
        return false;
    }

    /**
     * Called when the Fragment is inflating the view and needs to know the position of the
     * Fragment to instantiate the action buttons
     *
     * @return The position of the Fragment in the ViewPager, one of POSITION_MIDDLE or
     * POSITION_END
     */
    public abstract int getPosition();

    /**
     * Called when the Fragment is inflating the view and is adding the title text
     *
     * @return A String resource that points to the title that will be displayed at the top of the
     * tab
     */
    public abstract int getTitleResource();

    /**
     * Called when the Fragment is inflating the view and is adding the message text
     *
     * @return A String resource that points to the message that will be displayed under the tab
     * title
     */
    public abstract int getMessageResource();

    /**
     * Called when the Fragment is inflating the view and is adding the icon
     *
     * @return a Drawable resource that points to the icon that will be displayed next to the title
     */
    public abstract int getIconResource();

    /**
     * Called when the Fragment is inflating the view and is ready for the action view
     *
     * @param inflater  The LayoutInflater object that can be used to inflate any views in the
     *                  fragment
     * @param container The view that can be passed to the LayoutInflater when inflating the action
     *                  view
     * @return The view that will be inflated below the intro tab message
     */
    public abstract View onCreateActionView(LayoutInflater inflater, ViewGroup container);

    /**
     * Called to instantiate the directional action buttons (forward, back, finish) based on the
     * position of the tab
     *
     * @param leftButton  The left action button
     * @param rightButton The right action button
     */
    private void instantiateActionButtons(ImageButton leftButton, ImageButton rightButton) {
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIntroActivity().backward();
            }
        });
        switch (getPosition()) {
            case POSITION_MIDDLE:
                leftButton.setImageResource(R.drawable.ic_arrow_back_black_24dp);
                rightButton.setImageResource(R.drawable.ic_arrow_forward_black_24dp);
                rightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getIntroActivity().forward();
                    }
                });
                break;
            case POSITION_END:
                leftButton.setImageResource(R.drawable.ic_arrow_back_black_24dp);
                rightButton.setImageResource(R.drawable.ic_check_black_24dp);
                rightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getIntroActivity().finish();
                    }
                });
                break;
        }
    }
}