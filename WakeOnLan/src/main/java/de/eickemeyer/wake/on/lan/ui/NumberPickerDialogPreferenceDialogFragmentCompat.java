package de.eickemeyer.wake.on.lan.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.view.View;
import android.widget.NumberPicker;

import de.eickemeyer.wake.on.lan.R;

public class NumberPickerDialogPreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {


    private static final String KEY_NUM_PICKER_VAL = "KEY_NUM_PICKER_VAL";
    private NumberPicker mNumberPicker;


    public static NumberPickerDialogPreferenceDialogFragmentCompat newInstance(String key) {
        final NumberPickerDialogPreferenceDialogFragmentCompat fragment = new NumberPickerDialogPreferenceDialogFragmentCompat();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity activity = getActivity();
        final NumberPickerPreference preference = getNumberPickerPreference();

        View numberPickerView = activity.getLayoutInflater().inflate(R.layout.prefs_number_picker, null);
        mNumberPicker = (NumberPicker) numberPickerView.findViewById(R.id.numPickerView);
        mNumberPicker.setMinValue(preference.getStartValue());
        mNumberPicker.setMaxValue(preference.getEndValue());
        mNumberPicker.setValue(preference.getValue());

        if (savedInstanceState != null)
            mNumberPicker.setValue(savedInstanceState.getInt(KEY_NUM_PICKER_VAL, preference.getValue()));

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle(preference.getDialogTitle())
                .setIcon(preference.getDialogIcon())
                .setPositiveButton(preference.getPositiveButtonText(), this)
                .setNegativeButton(preference.getNegativeButtonText(), this)
                .setView(numberPickerView);
        View contentView = onCreateDialogView(activity);
        if (contentView != null) {
            onBindDialogView(contentView);
            builder.setView(contentView);
        } else {
            builder.setMessage(preference.getDialogMessage());
        }
        onPrepareDialogBuilder(builder);

        return builder.create();
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            final int setValue = mNumberPicker.getValue();
            if (getPreference().callChangeListener(setValue)) {
                getNumberPickerPreference().setValue(setValue);
            }
        }
    }

    private NumberPickerPreference getNumberPickerPreference() {
        return (NumberPickerPreference) getPreference();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_NUM_PICKER_VAL, mNumberPicker.getValue());
    }
}
