package de.eickemeyer.wake.on.lan.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.eickemeyer.wake.on.lan.R;
import de.eickemeyer.wake.on.lan.WakeOnLanApp;
import de.eickemeyer.wake.on.lan.activities.MainActivity;
import de.eickemeyer.wake.on.lan.entities.ScanResult;
import de.eickemeyer.wake.on.lan.network.IPv4Address;
import de.eickemeyer.wake.on.lan.network.NetworkInformation;
import de.eickemeyer.wake.on.lan.network.NetworkUtils;

public class RetainedScanTaskFragment extends Fragment {

    public final static String TAG = "TAG_RetainedScanTaskFragment";
    private final static int TIMEOUT = 900;
    boolean mReady = false;
    private List<ScanResult> mScanResults;
    private List<String> mIpsToScan;
    private String mOwnIP;
    private ProgressBar mProgressBar;
    private int mProgressBarProgress;
    private int mProgressBarMax;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mScanThread.start();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        synchronized (mScanThread) {
            updateProgressBarView();
            mReady = true;
            mScanThread.notify();
        }
    }

    @Override
    public void onDetach() {
        synchronized (mScanThread) {
            //to prevent a leak
            mProgressBar = null;
            mReady = false;
            mScanThread.notify();
        }
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        synchronized (mScanThread) {
            mReady = false;
            mScanThread.notify();
        }
    }

    private void updateProgressBarView() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ScanFragment.ScanProgressDialogFragment fr = (ScanFragment.ScanProgressDialogFragment) getFragmentManager().
                        findFragmentByTag(ScanFragment.ScanProgressDialogFragment.TAG);
                if (fr != null) {
                    final View view = fr.getProgressView();
                    if (view != null) {
                        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarScanTaskDialog);
                        mProgressBar.setMax(mProgressBarMax);
                        mProgressBar.setProgress(mProgressBarProgress);
                    }
                }
            }
        });
    }

    final Thread mScanThread = new Thread() {

        private int cird;

        @Override
        public void run() {
            final Context context = WakeOnLanApp.getWakeOnLanApp();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            cird = prefs.getInt(context.getResources().getString(R.string.prefs_advanced_scan_cird_key), 24);

            mScanResults = new ArrayList<>();
            int processors = Runtime.getRuntime().availableProcessors();
            ExecutorService executorService = Executors.newFixedThreadPool(processors != 0 ? processors * 10 : 15);
            CompletionService<ScanResult> completionService = new ExecutorCompletionService<>(executorService);

            setScanConfig();
            mProgressBarMax = mIpsToScan.size();
            setupProgressBar();

            for (int i = 0; i < mIpsToScan.size(); i++) {
                addScanTaskToCompletionService(completionService, mIpsToScan.get(i));
            }

            //retrieve the results from completion service and update progressbar
            for (int i = 0; i < mIpsToScan.size(); i++) {
                retrieveResultsFromCompletionService(completionService);
                mProgressBarProgress = i;
                updateProgressBar();
            }

            executorService.shutdown();

            putThreadAsleepIfNotReady();
            final ScanTaskListener scanTaskListener = (ScanFragment) getFragmentManager().findFragmentByTag(ScanFragment.TAG);
            putThreadAsleepIfNotReady();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    scanTaskListener.onScanTaskFinished(mScanResults);
                }
            });
        }

        private void setScanConfig() {
            mOwnIP = NetworkInformation.getOwnIpAddress();

            String rangeStartIp = NetworkInformation.getLowestNetAddress(cird);
            String rangeEndIp = NetworkInformation.getHighestNetAddress(cird);

            Log.d(MainActivity.TAG, String.format("setScanConfig: IP:%s, ScanRange:%s - %s, CIRD:%d", mOwnIP, rangeStartIp, rangeEndIp, cird));

            //get all ips which need to be scanned
            mIpsToScan = new ArrayList<>();
            IPv4Address currentAddress = new IPv4Address(rangeStartIp);
            while (!currentAddress.toString().equals(new IPv4Address(rangeEndIp).increment().toString())) {
                mIpsToScan.add(currentAddress.toString());
                currentAddress = currentAddress.increment();
            }
        }

        private void setupProgressBar() {
            putThreadAsleepIfNotReady();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setMax(mProgressBarMax);
                }
            });
        }

        /**
         * Even though some devices where online and the scan happened in a fast ethernet environment,
         * the normal ping sometimes didn't find the device.
         * That's why a combination of ArpScan, Ping, ArpScan is used which showed the best result with the lowest waiting time.
         */
        private void addScanTaskToCompletionService(CompletionService<ScanResult> completionService, final String currentScanIP) {
            completionService.submit(new Callable<ScanResult>() {
                @Override
                public ScanResult call() throws Exception {
                    // try arp check
                    boolean positiveFound = NetworkUtils.scanWithArpTable(currentScanIP);
                    // if there was no positive found -> ping
                    if (!positiveFound)
                        positiveFound = NetworkUtils.scanWithPing(currentScanIP, TIMEOUT);
                    // if there was no positive found -> arp check again
                    if (!positiveFound)
                        positiveFound = NetworkUtils.scanWithArpTable(currentScanIP);

                    if (positiveFound) {
                        final String hostname = NetworkUtils.getHostnameFromIp(currentScanIP);

                        return new ScanResult.ScanResultBuilder()
                                .setIp(currentScanIP)
                                .setBroadcast(NetworkInformation.getBroadcastAddress(cird))
                                .setMac(currentScanIP.equals(mOwnIP) ? NetworkInformation.getOwnMacAddress() : NetworkUtils.getMacFromIpByArpLookup(currentScanIP))
                                .setHostname(hostname)
                                .setIcon(getHostIcon(hostname, currentScanIP))
                                .build();
                    }
                    return null;
                }

                private int getHostIcon(String hostname, String ip) {
                    String[] routerNameList = {"fritzbox", "easybox", "router", "vodafone", "dd-wrt"};
                    hostname = hostname.toLowerCase();

                    if (hostname.contains("android")) {
                        return R.drawable.icon_android;
                    } else if (hostname.contains("iphone") || hostname.contains("ipad")) {
                        return R.drawable.icon_iphone;
                    } else {
                        if (ip.equals(NetworkInformation.getGatewayAddress())) {
                            return R.drawable.icon_router;
                        } else {
                            for (String routerName : routerNameList) {
                                if (hostname.contains(routerName)) {
                                    return R.drawable.icon_router;
                                }
                            }
                        }
                    }
                    //if nothing matches, return pc drawable
                    return R.drawable.icon_pc;
                }
            });
        }

        private void retrieveResultsFromCompletionService(CompletionService<ScanResult> completionService) {
            try {
                ScanResult scanResult = completionService.take().get();
                if (scanResult != null) mScanResults.add(scanResult);
            } catch (InterruptedException | ExecutionException e) {
                Log.e(MainActivity.TAG, e.getMessage());
            }
        }

        private void updateProgressBar() {
            putThreadAsleepIfNotReady();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mProgressBar != null) mProgressBar.setProgress(mProgressBarProgress + 1);
                }
            });
        }

        private void putThreadAsleepIfNotReady() {
            synchronized (this) {
                while (!mReady || getActivity() == null) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    };

    interface ScanTaskListener {
        void onScanTaskFinished(List<ScanResult> scanResults);
    }
}