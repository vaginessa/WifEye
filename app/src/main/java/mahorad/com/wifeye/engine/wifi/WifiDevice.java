package mahorad.com.wifeye.engine.wifi;

import android.content.Context;
import android.net.wifi.WifiManager;

import javax.inject.Inject;

import mahorad.com.wifeye.di.qualifier.ApplicationContext;
import mahorad.com.wifeye.util.Utils;
import timber.log.Timber;

/**
 * Created by Mahan Rad on 2017-08-24.
 */

public class WifiDevice {

    private static final String TAG = WifiDevice.class.getSimpleName();

    static WifiManager wifiManager;

    @Inject
    public WifiDevice(@ApplicationContext Context context) {
        wifiManager = Utils.getWifiManager(context);
    }

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
