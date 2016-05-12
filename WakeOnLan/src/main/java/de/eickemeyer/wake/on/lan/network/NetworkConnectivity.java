package de.eickemeyer.wake.on.lan.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import de.eickemeyer.wake.on.lan.WakeOnLanApp;

public final class NetworkConnectivity {

    private NetworkConnectivity() {
    }

    public static boolean isConnectedToWifi() {
        Context context = WakeOnLanApp.getWakeOnLanApp();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
    }
}
