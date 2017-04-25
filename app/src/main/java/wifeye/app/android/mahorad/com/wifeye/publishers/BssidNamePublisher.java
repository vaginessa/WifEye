package wifeye.app.android.mahorad.com.wifeye.publishers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;

import wifeye.app.android.mahorad.com.wifeye.consumers.INetConsumer;

/**
 * listens to connected ssid names and notifies consumers
 * if the Internet is connected or disconnected.
 */
public class BssidNamePublisher extends BroadcastReceiver {

    private static String TAG = BssidNamePublisher.class.getSimpleName();

    private static String ssid;
    private final WifiManager wifiManager;
    private final Context context;
    private final List<INetConsumer> consumers;

    public BssidNamePublisher(Context context) {
        if (context == null)
            throw new IllegalArgumentException();
        this.context = context;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        consumers = new ArrayList<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        synchronized (this) {
            WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            String bssid = connectionInfo.getBSSID();
            String hotSpot = (bssid == null ? null : connectionInfo.getSSID());
            if ("0x".equals(hotSpot)) return;
            if (isSame(hotSpot)) return;
            ssid = hotSpot;
            if (hotSpot == null)
                notifyInternetDisconnected();
            else
                notifyInternetGotConnected();
        }
    }

    private boolean isSame(String text) {
        if (text == null && ssid == null) return true;
        boolean anyNull = (ssid == null || text == null);
        return !anyNull && ssid.equals(text);
    }

    private void notifyInternetGotConnected() {
        for (final INetConsumer consumer : consumers) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    consumer.onInternetConnected(ssid);
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }

    private void notifyInternetDisconnected() {
        for (final INetConsumer consumer : consumers) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    consumer.onInternetDisconnected();
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }

    public void start() {
        IntentFilter intentFilter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        context.registerReceiver(this, intentFilter);
    }
    public void stop() {
        context.unregisterReceiver(this);
    }

    public boolean subscribe(INetConsumer consumer) {
        return consumers.add(consumer);
    }

    public boolean unsubscribe(INetConsumer consumer) {
        return consumers.remove(consumer);
    }

    public static String ssid() {
        return ssid;
    }
}
