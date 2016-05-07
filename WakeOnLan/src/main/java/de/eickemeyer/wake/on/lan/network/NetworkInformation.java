package de.eickemeyer.wake.on.lan.network;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import org.apache.commons.net.util.SubnetUtils;

import de.eickemeyer.wake.on.lan.WakeOnLanApp;

public class NetworkInformation {

    public static String getOwnIpAddress() {
        WifiManager wifiMgr = (WifiManager) WakeOnLanApp.getWakeOnLanApp().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int myIpTemp = wifiInfo.getIpAddress();
        return String.format("%d.%d.%d.%d", (myIpTemp & 0xff), (myIpTemp >> 8 & 0xff), (myIpTemp >> 16 & 0xff), (myIpTemp >> 24 & 0xff));
    }

    public static String getOwnMacAddress() {
        WifiManager wifiMgr = (WifiManager) WakeOnLanApp.getWakeOnLanApp().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        return wifiInfo.getMacAddress();
    }

    public static String getGatewayAddress() {
        WifiManager wifiManager = (WifiManager) WakeOnLanApp.getWakeOnLanApp().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
            if (dhcpInfo != null)
                return String.format("%d.%d.%d.%d", (dhcpInfo.gateway & 0xff), (dhcpInfo.gateway >> 8 & 0xff), (dhcpInfo.gateway >> 16 & 0xff), (dhcpInfo.gateway >> 24 & 0xff));
        }
        return null;
    }

    public static String getBroadcastAddress(int cird) {
        return new SubnetUtils(String.format("%s/%s", getOwnIpAddress(), cird)).getInfo().getBroadcastAddress();
    }

    public static String getLowestNetAddress(int cird) {
        return new SubnetUtils(String.format("%s/%s", getOwnIpAddress(), cird)).getInfo().getLowAddress();
    }

    public static String getHighestNetAddress(int cird) {
        return new SubnetUtils(String.format("%s/%s", getOwnIpAddress(), cird)).getInfo().getHighAddress();
    }
}
