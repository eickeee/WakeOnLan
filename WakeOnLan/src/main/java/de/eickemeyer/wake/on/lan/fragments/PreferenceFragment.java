package de.eickemeyer.wake.on.lan.fragments;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.squareup.leakcanary.RefWatcher;

import de.eickemeyer.wake.on.lan.R;
import de.eickemeyer.wake.on.lan.WakeOnLanApp;


public class PreferenceFragment extends PreferenceFragmentCompat {

    public static final String TAG = "TAG_PreferenceFragment";

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public RecyclerView onCreateRecyclerView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return super.onCreateRecyclerView(inflater, parent, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = WakeOnLanApp.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        super.onDisplayPreferenceDialog(preference);
    }
}