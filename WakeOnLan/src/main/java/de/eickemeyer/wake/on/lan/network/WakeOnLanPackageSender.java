package de.eickemeyer.wake.on.lan.network;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import de.eickemeyer.wake.on.lan.R;
import de.eickemeyer.wake.on.lan.activities.MainActivity;


public class WakeOnLanPackageSender {

    public static void sendWakeOnLanPackage(final Activity activity, final String mac, final String broadcast) {
        final int port = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(activity).getString(activity.getResources().getString(R.string.prefs_port_key), "9"));

        new Thread(new Runnable() {
            public void run() {
                byte[] macBytes = getMacBytes(mac);
                byte[] bytes = new byte[6 + 16 * macBytes.length];
                for (int i = 0; i < 6; i++) {
                    bytes[i] = (byte) 0xff;
                }
                for (int i = 6; i < bytes.length; i += macBytes.length) {
                    System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
                }
                try {
                    InetAddress address = InetAddress.getByName(broadcast);
                    DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
                    DatagramSocket socket = new DatagramSocket();

                    //sent three packages to circumvent problems in unreliable networks
                    socket.send(packet);
                    socket.send(packet);
                    socket.send(packet);

                    socket.close();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final View fragmentContainer = activity.findViewById(R.id.fragmentContainer);
                            Snackbar.make(fragmentContainer, R.string.computerShouldStart, Snackbar.LENGTH_LONG).show();
                        }
                    });
                } catch (IOException e) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final View fragmentContainer = activity.findViewById(R.id.fragmentContainer);
                            Snackbar.make(fragmentContainer, R.string.wolFailed, Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
            }

            private byte[] getMacBytes(String macStr) throws IllegalArgumentException {
                byte[] bytes = new byte[6];
                String[] hex = macStr.split("(\\:|\\-)");
                if (hex.length != 6) {
                    Log.e(MainActivity.TAG, "getMacBytes - mac has not the length six");
                }
                try {
                    for (int i = 0; i < 6; i++) {
                        bytes[i] = (byte) Integer.parseInt(hex[i], 16);
                    }
                } catch (NumberFormatException e) {
                    Log.e(MainActivity.TAG, String.format("getMacBytes - NumberFormatException %s", e.getMessage()));
                }
                return bytes;
            }
        }).start();
    }
}
