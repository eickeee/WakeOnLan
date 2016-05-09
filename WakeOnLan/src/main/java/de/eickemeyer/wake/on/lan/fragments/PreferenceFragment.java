package de.eickemeyer.wake.on.lan.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.squareup.leakcanary.RefWatcher;

import de.eickemeyer.wake.on.lan.R;
import de.eickemeyer.wake.on.lan.WakeOnLanApp;
import de.eickemeyer.wake.on.lan.ui.NumberPickerDialogPreferenceDialogFragmentCompat;
import de.eickemeyer.wake.on.lan.ui.NumberPickerPreference;


public class PreferenceFragment extends PreferenceFragmentCompat {

    public static final String TAG = "TAG_PreferenceFragment";

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = WakeOnLanApp.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        if (preference instanceof NumberPickerPreference) {
            // check if dialog is already showing
            if (getActivity().getSupportFragmentManager().findFragmentByTag("android.support.v7.preference.PreferenceFragment.DIALOG") != null)
                return;

            DialogFragment numberPickerPreferenceDialogFragment = NumberPickerDialogPreferenceDialogFragmentCompat.newInstance(preference.getKey());
            numberPickerPreferenceDialogFragment.setTargetFragment(this, 0);
            numberPickerPreferenceDialogFragment.show(getFragmentManager(), "android.support.v7.preference.PreferenceFragment.DIALOG");
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }
}