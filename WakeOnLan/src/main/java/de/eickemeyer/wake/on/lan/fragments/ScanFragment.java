package de.eickemeyer.wake.on.lan.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.eickemeyer.wake.on.lan.R;
import de.eickemeyer.wake.on.lan.adapter.ScanListAdapter;
import de.eickemeyer.wake.on.lan.entities.ScanResult;
import de.eickemeyer.wake.on.lan.fragments.dialogs.OnContextItemClickListener;
import de.eickemeyer.wake.on.lan.fragments.dialogs.ScanResultListAddFavoriteDialogFragment;
import de.eickemeyer.wake.on.lan.fragments.dialogs.ScanResultsListContextDialogFragment;
import de.eickemeyer.wake.on.lan.network.NetworkConnectivity;
import de.eickemeyer.wake.on.lan.network.WakeOnLanPackageSender;


public class ScanFragment extends BaseFragment implements ScanListAdapter.OnClickListener, RetainedScanTaskFragment.ScanTaskListener, OnContextItemClickListener {

    public static final String TAG = "TAG_ScanFragment";
    private static final String KEY_SCAN_RESULTS = "KEY_SCAN_RESULTS";
    private ScanListAdapter mRecyclerViewAdapter;
    private int mListClickPosition;
    private ArrayList<ScanResult> mScanResults;

    private FloatingActionButton mFloatingActionButton;
    @BindView(R.id.recyclerScan)
    RecyclerView mRecyclerView;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_scan, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (savedInstanceState != null)
            mScanResults = savedInstanceState.getParcelableArrayList(KEY_SCAN_RESULTS);

        setupList();
        return view;
    }

    private void setupList() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (mScanResults == null)
            mScanResults = new ArrayList<>();

        mRecyclerViewAdapter = new ScanListAdapter(mScanResults, this);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupActionButton();
    }

    private void setupActionButton() {
        mFloatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        mFloatingActionButton.setVisibility(View.VISIBLE);
        mFloatingActionButton.setImageResource(R.drawable.fab_scan);
        mFloatingActionButton.setRippleColor(ContextCompat.getColor(getActivity(), R.color.white_pressed));
        mFloatingActionButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.buttonColorNormal)));
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnectivity.isConnectedToWifi()) {
                    mRecyclerViewAdapter.removeAll();
                    new ScanProgressDialogFragment().show(getFragmentManager(), ScanProgressDialogFragment.TAG);

                    getFragmentManager().beginTransaction().add(new RetainedScanTaskFragment(), RetainedScanTaskFragment.TAG).commit();
                } else {
                    final View fragmentContainer = getActivity().findViewById(R.id.fragmentContainer);
                    Snackbar.make(fragmentContainer, R.string.wifiNotConnectedMessageSearch, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mFloatingActionButton.setVisibility(View.GONE);
        //prevent leak of scan fragment
        mFloatingActionButton.setOnClickListener(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_SCAN_RESULTS, (ArrayList<? extends Parcelable>) mRecyclerViewAdapter.getScanResults());
    }

    @Override
    public void onScanTaskFinished(List<ScanResult> scanResults) {
        for (ScanResult scanResult : scanResults)
            mRecyclerViewAdapter.add(scanResult);

        final FragmentManager fragmentManager = getFragmentManager();
        Fragment fr = fragmentManager.findFragmentByTag(ScanProgressDialogFragment.TAG);
        if (fr != null)
            fragmentManager.beginTransaction().remove(fr).commitAllowingStateLoss();
    }

    @Override
    public void onRecyclerViewItemClick(int position) {
        mListClickPosition = position;
        ScanResultsListContextDialogFragment.newInstance(R.string.chooseAction).show(getFragmentManager(), ScanResultsListContextDialogFragment.TAG);
    }

    @Override
    public void onContextItemClick(int position) {
        switch (position) {
            case 0:
                ScanResult scanResult = mRecyclerViewAdapter.getItem(mListClickPosition);
                if (NetworkConnectivity.isConnectedToWifi()) {
                    WakeOnLanPackageSender.sendWakeOnLanPackage(getActivity(), scanResult.mac, scanResult.broadcast);
                } else {
                    final View fragmentContainer = getActivity().findViewById(R.id.fragmentContainer);
                    Snackbar.make(fragmentContainer, R.string.wifiNotConnectedMessageWake, Snackbar.LENGTH_LONG).show();
                }
                break;
            case 1:
                showAddFavoriteDialog();
                break;
        }
        final FragmentManager fragmentManager = getFragmentManager();
        final Fragment fr = fragmentManager.findFragmentByTag(ScanResultsListContextDialogFragment.TAG);
        if (fr != null)
            fragmentManager.beginTransaction().remove(fr).commitAllowingStateLoss();
    }

    private void showAddFavoriteDialog() {
        ScanResultListAddFavoriteDialogFragment.newInstance(mListClickPosition).show(getFragmentManager(), null);
    }

    public ScanListAdapter getRecyclerViewAdapter() {
        return mRecyclerViewAdapter;
    }

    public static class ScanProgressDialogFragment extends DialogFragment {

        public static final String TAG = "TAG";
        private View mProgressView;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setCancelable(false);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Activity activity = getActivity();
            mProgressView = activity.getLayoutInflater().inflate(R.layout.dialog_scan_progress, null);
            ProgressBar pb = (ProgressBar) mProgressView.findViewById(R.id.progressBarScanTaskDialog);
            if (pb != null)
                pb.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

            return new AlertDialog.Builder(activity, R.style.HdrDialogStyle)
                    .setTitle(R.string.networkScanProgressDialogTitle)
                    .setView(mProgressView, 60, 20, 60, 10)
                    .show();
        }

        public View getProgressView() {
            return mProgressView;
        }
    }
}
