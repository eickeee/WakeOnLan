package de.eickemeyer.wake.on.lan.fragments;

import android.support.v4.app.Fragment;

import com.squareup.leakcanary.RefWatcher;

import de.eickemeyer.wake.on.lan.WakeOnLanApp;
import de.eickemeyer.wake.on.lan.activities.MainActivity;

public abstract class BaseFragment extends Fragment {


    @Override
    public void onStart() {
        super.onStart();
        expandAppBar();
    }

    @Override
    public void onStop() {
        super.onStop();
        expandAppBar();
    }

    private void expandAppBar() {
        ((MainActivity) getActivity()).getAppBarLayout().setExpanded(true, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = WakeOnLanApp.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}

