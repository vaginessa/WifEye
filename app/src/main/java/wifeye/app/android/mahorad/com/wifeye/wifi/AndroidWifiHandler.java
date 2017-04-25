package wifeye.app.android.mahorad.com.wifeye.wifi;

import android.net.wifi.WifiManager;
import android.util.Log;

public class AndroidWifiHandler implements IWifiHandler {

    private static final String TAG = AndroidWifiHandler.class.getSimpleName();

    private final WifiManager wifiManager;

    public AndroidWifiHandler(WifiManager wifiManager) {
        if (wifiManager == null)
            throw new IllegalArgumentException();
        this.wifiManager = wifiManager;
    }

    @Override
    public boolean isEnabled() {
        return wifiManager.isWifiEnabled();
    }

    @Override
    public void enable() {
        wifiManager.setWifiEnabled(true);
        Log.d(TAG, "enabling wifi...");
    }

    @Override
    public void disable() {
        wifiManager.setWifiEnabled(false);
        Log.d(TAG, "disabling wifi...");
    }
}
