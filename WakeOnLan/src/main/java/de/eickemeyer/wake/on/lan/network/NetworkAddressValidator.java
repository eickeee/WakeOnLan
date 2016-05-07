package de.eickemeyer.wake.on.lan.network;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NetworkAddressValidator {

    private static final String IP_ADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    private static final String MAC_PATTERN = "^([0-9a-fA-F][0-9a-fA-F]:){5}([0-9a-fA-F][0-9a-fA-F])$";

    private NetworkAddressValidator() {
    }

    public static boolean isInvalidIp(String ip) {
        if (ip == null)
            return true;
        Pattern pattern = Pattern.compile(IP_ADDRESS_PATTERN);
        Matcher matcher = pattern.matcher(ip);
        return !matcher.matches();
    }

    public static boolean isInvalidMac(String mac) {
        if (mac == null)
            return true;

        Pattern pattern = Pattern.compile(MAC_PATTERN);
        Matcher matcher = pattern.matcher(mac.replace("-", ":"));
        return !matcher.matches();
    }
}
