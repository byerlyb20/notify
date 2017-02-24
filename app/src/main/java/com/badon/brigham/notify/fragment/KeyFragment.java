package com.badon.brigham.notify.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.badon.brigham.notify.R;
import com.badon.brigham.notify.intro.IntroFragment;
import com.badon.brigham.notify.util.CustomTabsFallback;

public class KeyFragment extends IntroFragment {

    private EditText mKeyInput;

    @Override
    public int getPosition() {
        return IntroFragment.POSITION_MIDDLE;
    }

    @Override
    public int getTitleResource() {
        return R.string.preference_api_key;
    }

    @Override
    public int getMessageResource() {
        return R.string.intro_key_detail;
    }

    @Override
    public int getIconResource() {
        return R.drawable.ic_lock_outline_white_24dp;
    }

    @Override
    public View onCreateActionView(LayoutInflater inflater, ViewGroup container) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.intro_action_2, container, false);
        mKeyInput = (EditText) view.findViewById(R.id.key_input);

        Button getApiKeyShortcut = (Button) view.findViewById(R.id.get_api_key_shortcut);
        getApiKeyShortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGenerateAPIKeySite();
            }
        });

        return view;
    }

    @Override
    public void saveData() {
        String apiKey = mKeyInput.getText().toString();

        // Only save the API Key if the field has changed
        if (!apiKey.isEmpty()) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            prefs.edit().putString("apiKey", apiKey).apply();
        }
    }

    private void openGenerateAPIKeySite() {
        String url = "https://cloud.lifx.com/settings";

        if (!CustomTabsFallback.useFallback(getContext())) {
            int primaryColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);

            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(primaryColor);
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(getActivity(), Uri.parse(url));
        } else {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(i);
        }
    }
}