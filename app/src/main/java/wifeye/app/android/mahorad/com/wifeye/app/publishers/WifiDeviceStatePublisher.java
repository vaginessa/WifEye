package wifeye.app.android.mahorad.com.wifeye.app.publishers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import wifeye.app.android.mahorad.com.wifeye.app.consumers.IWifiDeviceStateConsumer;

import static wifeye.app.android.mahorad.com.wifeye.app.publishers.WifiState.Unknown;

public class WifiDeviceStatePublisher extends BroadcastReceiver {

    private final Context context;
    private final Set<IWifiDeviceStateConsumer> consumers;
    private WifiState wifiState = Unknown;

    public WifiDeviceStatePublisher(Context context) {
        this.context = context;
        consumers = new HashSet<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        synchronized (this) {
            int extraWifiState = intent.getIntExtra(
                    WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN);
            wifiState = WifiState.get(extraWifiState);
            publish();
        }
    }

    private void publish() {
        for (IWifiDeviceStateConsumer consumer : consumers) {
            Executors
                    .newSingleThreadExecutor()
                    .submit(() -> consumer.onWifiStateChanged(wifiState));
        }
    }

    public void start() {
        IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        context.registerReceiver(this, intentFilter);
    }

    public void stop() {
        context.unregisterReceiver(this);
        consumers.clear();
    }

    public boolean subscribe(IWifiDeviceStateConsumer consumer) {
        return consumers.add(consumer);
    }

    public boolean unsubscribe(IWifiDeviceStateConsumer consumer) {
        return consumers.remove(consumer);
    }

    public WifiState state() {
        return wifiState;
    }
}
