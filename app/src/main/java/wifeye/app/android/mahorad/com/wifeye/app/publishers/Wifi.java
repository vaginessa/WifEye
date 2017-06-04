package wifeye.app.android.mahorad.com.wifeye.app.publishers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import wifeye.app.android.mahorad.com.wifeye.app.consumers.IWifiConsumer;

import static android.content.Context.WIFI_SERVICE;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.WifiState.Disabled;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.WifiState.Enabled;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.WifiState.Unknown;

public class Wifi extends BroadcastReceiver {

    private static final String TAG = Wifi.class.getSimpleName();

    private final Context context;
    private final WifiManager wifiManager;
    private static WifiState wifiState = Unknown;
    private static Date date;

    private final Set<IWifiConsumer> consumers;

    public Wifi(Context context) {
        this.context = context;
        wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        consumers = new HashSet<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        synchronized (this) {
            int wifiState = intent.getIntExtra(
                    WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN);
            WifiState state = WifiState.get(wifiState);
            boolean updated = update(state);
            if (updated) publish();
        }
    }

    private boolean update(WifiState state) {
        if (wifiState == state)
            return false;
        wifiState = state;
        if (wifiState == Enabled || wifiState == Disabled)
            date = Calendar.getInstance().getTime();
        return true;
    }

    private void publish() {
        for (IWifiConsumer consumer : consumers) {
            Executors
                    .newSingleThreadExecutor()
                    .submit(() -> consumer.onWifiStateChanged(wifiState));
        }
    }

    public void register() {
        IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        context.registerReceiver(this, intentFilter);
    }

    public void unregister() {
        context.unregisterReceiver(this);
        consumers.clear();
    }

    public boolean subscribe(IWifiConsumer consumer) {
        return consumers.add(consumer);
    }

    public boolean unsubscribe(IWifiConsumer consumer) {
        return consumers.remove(consumer);
    }

    public void enable() {
        if (isEnabled()) return;
        wifiManager.setWifiEnabled(true);
        Log.d(TAG, "[[ enabling wifi... ]]");
    }

    public void disable() {
        if (WifiSsidNamePublisher.ssid() != null)
            return;
        wifiManager.setWifiEnabled(false);
        Log.d(TAG, "[[ disabling wifi... ]]");
    }

    public boolean isEnabled() {
        int state = wifiManager.getWifiState();
        boolean enabled = (state == WifiManager.WIFI_STATE_ENABLED);
        Log.d(TAG, String.format("[[ wifiManager %b, wifiState %b ]]", enabled, wifiState == Enabled));
        return enabled;
    }

    public boolean isDisabled() {
        int state = wifiManager.getWifiState();
        boolean disabled = (state == WifiManager.WIFI_STATE_DISABLED);
        Log.d(TAG, String.format("[[ wifiManager %b, wifiState %b ]]", disabled, wifiState == Disabled));
        return disabled;
    }

    public static WifiState state() {
        return wifiState;
    }

    public static Date date() {
        return date;
    }

}
