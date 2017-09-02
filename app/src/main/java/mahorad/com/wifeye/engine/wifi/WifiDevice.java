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

    WifiManager wifiManager;

    @Inject
    public WifiDevice(@ApplicationContext Context context) {
        wifiManager = Utils.getWifiManager(context);
    }

    public void enable() {
        wifiManager.setWifiEnabled(true);
        Timber.tag(TAG).v( "[[ enabling wifi... ]]");
    }

    public void disable() {
        wifiManager.setWifiEnabled(false);
        Timber.tag(TAG).v("[[ disabling wifi... ]]");
    }

    public boolean isEnabled() {
        boolean isEnabled = wifiManager.isWifiEnabled();
        Timber.tag(TAG).v("[[ isWifiEnabled? %b ]]", isEnabled);
        return isEnabled;
    }
}
