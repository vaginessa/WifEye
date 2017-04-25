package wifeye.app.android.mahorad.com.wifeye.wifi;

import android.net.wifi.WifiManager;

public class AndroidWifiHandler implements IWifiHandler {

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
    }

    @Override
    public void disable() {
        wifiManager.setWifiEnabled(false);
    }
}
