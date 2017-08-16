package mahorad.com.wifeye.engine.wifi;

import android.net.wifi.WifiManager;

import mahorad.com.wifeye.base.BaseApplication;
import timber.log.Timber;

import static mahorad.com.wifeye.util.Utils.getWifiManager;

public class WifiDevice {

    private static final String TAG = WifiDevice.class.getSimpleName();

    private static WifiManager wifiManager = getWifiManager(
            BaseApplication.component().context());

    public static void enable() {
        wifiManager.setWifiEnabled(true);
        Timber.tag(TAG).d( "[[ >>> enabling wifi... ]]");
    }

    public static void disable() {
        wifiManager.setWifiEnabled(false);
        Timber.tag(TAG).d("[[ <<< disabling wifi... ]]");
    }

    public static boolean isEnabled() {
        return wifiManager.isWifiEnabled();
    }
}
