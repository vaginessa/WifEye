package wifeye.app.android.mahorad.com.wifeye.app.wifi;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class AndroidWifiHandler implements IWifiHandler {

    private static final String TAG = AndroidWifiHandler.class.getSimpleName();

    private final WifiManager wifiManager;

    public AndroidWifiHandler(Context context) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public void enable() {
        if (isEnabled()) return;
        wifiManager.setWifiEnabled(true);
        Log.d(TAG, "[[ enabling wifi... ]]");
    }

    @Override
    public boolean isEnabled() {
        int wifiState = wifiManager.getWifiState();
        boolean enabled = (wifiState == WifiManager.WIFI_STATE_ENABLED);
        Log.d(TAG, String.format("[[ wifi is %s ]]", enabled ? "ENABLE" : "DISABLE"));
        return enabled;
    }

    @Override
    public void disable() {
        if (isConnected()) return;
        wifiManager.setWifiEnabled(false);
        Log.d(TAG, "[[ disabling wifi... ]]");
    }

    private boolean isConnected() {
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        String bssid = connectionInfo.getBSSID();
        String ssid = (bssid == null ? null : connectionInfo.getSSID());
        return ssid != null && !ssid.equals("0x");
    }
}
