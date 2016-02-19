package com.badon.brigham.notify.preference;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.badon.brigham.notify.R;

public class CyclesPreference extends DialogPreference {

    Context context;
    NumberPicker numberPicker;

    public CyclesPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setDialogTitle(R.string.cycles_dialog_title);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        int pulseCycles = getPersistedInt(2);
        if (pulseCycles != 1) {
            setSummary("Pulse " + pulseCycles + " times");
        } else {
            setSummary("Pulse 1 time");
        }
        return super.onCreateView(parent);
    }

    @Override
    protected View onCreateDialogView() {
        numberPicker = new NumberPicker(context);
        numberPicker.setMaxValue(10);
        numberPicker.setValue(getPersistedInt(2));
        return numberPicker;
    }

    @Override
    public void onClick(DialogInterface dialog, int button) {
        if (button == DialogInterface.BUTTON_POSITIVE) {
            persistInt(numberPicker.getValue());
            int pulseCycles = getPersistedInt(2);
            if (pulseCycles != 1) {
                setSummary("Pulse " + pulseCycles + " times");
            } else {
                setSummary("Pulse 1 time");
            }
        }
    }
}
