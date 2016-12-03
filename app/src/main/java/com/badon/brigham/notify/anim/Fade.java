package com.badon.brigham.notify.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

public class Fade {

    private static final int ANIM_DURATION = 250;

    public static void show(final View view) {
        view.animate().cancel();

        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);

        view.animate()
                .alpha(1f)
                .setDuration(ANIM_DURATION)
                .setListener(null);
    }

    public static void hide(final View view, final boolean gone) {
        view.animate().cancel();

        view.animate()
                .alpha(0f)
                .setDuration(ANIM_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(gone ? View.GONE : View.INVISIBLE);
                    }
                });
    }
}
