package de.eickemeyer.wake.on.lan.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.eickemeyer.wake.on.lan.R;
import de.eickemeyer.wake.on.lan.network.NetworkAddressValidator;
import de.eickemeyer.wake.on.lan.network.NetworkConnectivity;
import de.eickemeyer.wake.on.lan.network.WakeOnLanPackageSender;


public class WakeFragment extends BaseFragment {

    public static final String TAG = "TAG_WakeFragment";

    @BindView(R.id.inputBroadcast)
    AppCompatEditText mEditBroadcast;
    @BindView(R.id.inputMac)
    AppCompatEditText mEditMac;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_wake, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_wakeup)
    public void wakeBtnClicked() {
        final FragmentActivity activity = getActivity();
        final String broadcast = mEditBroadcast.getText().toString();
        final String mac = mEditMac.getText().toString();
        final View fragmentContainer = getActivity().findViewById(R.id.fragmentContainer);

        if (NetworkAddressValidator.isInvalidIp(broadcast)) {
            Snackbar.make(fragmentContainer, R.string.invalidBroadcast, Snackbar.LENGTH_LONG).show();
        } else if (NetworkAddressValidator.isInvalidMac(mac)) {
            Snackbar.make(fragmentContainer, R.string.invalidMac, Snackbar.LENGTH_LONG).show();
        } else if (!NetworkConnectivity.isConnectedToWifi()) {
            Snackbar.make(fragmentContainer, R.string.wifiNotConnectedMessageWake, Snackbar.LENGTH_LONG).show();
        } else {
            WakeOnLanPackageSender.sendWakeOnLanPackage(activity, mac, broadcast);
        }
    }
}