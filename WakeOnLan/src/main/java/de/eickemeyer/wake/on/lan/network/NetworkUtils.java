package de.eickemeyer.wake.on.lan.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.eickemeyer.wake.on.lan.activities.MainActivity;

public final class NetworkUtils {

    public static final String NO_MAC = "00:00:00:00:00:00";
    public static final String NO_IP = "0.0.0.0";

    private final static String MAC_RE = "^%s\\s+0x1\\s+0x2\\s+([:0-9a-fA-F]+)\\s+\\*\\s+\\w+$";

    private NetworkUtils() {
    }

    public static String getHostnameFromIp(String ip) {
        try {
            return InetAddress.getByName(ip).getHostName();
        } catch (UnknownHostException e) {
            Log.e(MainActivity.TAG, "getHostnameFromIp: " + e.getMessage());
        }
        return NO_IP;
    }

    public static String getMacFromIpByArpLookup(String ip) {
        if (ip == null || ip.equals(NO_IP))
            return NO_MAC;

        String mac = NO_MAC;
        try {
            Pattern pattern = Pattern.compile(String.format(MAC_RE, ip.replace(".", "\\.")));
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/net/arp"), 8 * 1024);
            String line;
            Matcher matcher;
            while ((line = bufferedReader.readLine()) != null) {
                matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    mac = matcher.group(1);
                    break;
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            Log.e(MainActivity.TAG, "Can't open/read file ARP: " + e.getMessage());
            return mac;
        }
        return mac;
    }

    public static boolean scanWithArpTable(String ip) {
        return !NO_MAC.equals(getMacFromIpByArpLookup(ip));
    }

    public static boolean scanWithPing(String ip, int timeout) throws IOException {
        return InetAddress.getByName(ip).isReachable(timeout);
    }
}
