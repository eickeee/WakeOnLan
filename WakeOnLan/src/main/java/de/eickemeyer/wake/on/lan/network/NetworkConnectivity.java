package de.eickemeyer.wake.on.lan.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import de.eickemeyer.wake.on.lan.WakeOnLanApp;

public final class NetworkConnectivity {

    private NetworkConnectivity() {
    }

    public static boolean isConnectedToWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) WakeOnLanApp.getWakeOnLanApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (Build.VERSION.SDK_INT <= 20)
            networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        else {
            Network[] networks = connectivityManager.getAllNetworks();
            if (networks != null && networks.length > 0)
                networkInfo = connectivityManager.getNetworkInfo(networks[0]);
        }
        return networkInfo != null && networkInfo.isConnected();
    }
}
