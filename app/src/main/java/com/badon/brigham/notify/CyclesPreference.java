package com.badon.brigham.notify;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

public class CyclesPreference extends DialogPreference {

    Context context;
    NumberPicker numberPicker;

    public CyclesPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setDialogTitle("Cycles");
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        int breathCycles = getPersistedInt(2);
        if (breathCycles != 1) {
            setSummary("Breath " + breathCycles + " times");
        } else {
            setSummary("Breath 1 time");
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
            int breathCycles = getPersistedInt(2);
            if (breathCycles != 1) {
                setSummary("Breath " + breathCycles + " times");
            } else {
                setSummary("Breath 1 time");
            }
        }
    }
}
