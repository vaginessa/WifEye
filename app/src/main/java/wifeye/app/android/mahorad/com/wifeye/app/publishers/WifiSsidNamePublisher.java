package wifeye.app.android.mahorad.com.wifeye.app.publishers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import wifeye.app.android.mahorad.com.wifeye.app.consumers.IWifiDeviceStateConsumer;
import wifeye.app.android.mahorad.com.wifeye.app.consumers.IWifiSsidNameConsumer;

import static wifeye.app.android.mahorad.com.wifeye.app.publishers.WifiState.*;

/**
 * listens to connected ssid names and notifies consumers
 * if the Internet is connected or disconnected.
 */
public class WifiSsidNamePublisher
        extends BroadcastReceiver
        implements IWifiDeviceStateConsumer {

    private static String TAG = WifiSsidNamePublisher.class.getSimpleName();

    private static String ssid = null;
    private static Date date = Calendar.getInstance().getTime();;
    private final WifiManager wifiManager;
    private final WifiDeviceStatePublisher wifiPublisher;
    private final Context context;
    private final Set<IWifiSsidNameConsumer> consumers;

    public WifiSsidNamePublisher(Context context, WifiDeviceStatePublisher wifiPublisher) {
        this.context = context;
        this.wifiPublisher = wifiPublisher;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        consumers = new HashSet<>();
    }

    @Override
    public void onWifiStateChanged(WifiState state) {
        boolean wifiDisabled =
                WifiDeviceStatePublisher.state() == Disabled;
        if (!wifiDisabled) return;
        notifyInternetDisconnected();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        synchronized (this) {

            boolean wifiEnabled =
                    WifiDeviceStatePublisher.state() == Enabled;
            if (!wifiEnabled) {
                notifyInternetDisconnected();
                return;
            }

            WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            String bssid = connectionInfo.getBSSID();
            String hotSpot = (bssid == null
                    ? null
                    : connectionInfo.getSSID());
            if (!isValidName(hotSpot)) {
                ssid = null;
                notifyInternetDisconnected();
                return;
            }

            if (isSame(hotSpot)) return;
            ssid = hotSpot;
            date = Calendar.getInstance().getTime();
            notifyInternetGotConnected();
        }
    }

    private synchronized boolean isValidName(String hotSpot) {
        if (hotSpot == null) return false;
        if ("0x".equals(hotSpot)) return false;
        if (hotSpot.length() == 0) return false;
        return !hotSpot.contains("unknown");
    }

    private boolean isSame(String text) {
        if (text == null && ssid == null) return true;
        boolean anyNull = (ssid == null || text == null);
        return !anyNull && ssid.equals(text);
    }

    private void notifyInternetGotConnected() {
        for (final IWifiSsidNameConsumer consumer : consumers) {
            Executors
                    .newSingleThreadExecutor()
                    .submit(() -> consumer.onInternetConnected(ssid));
        }
    }

    private void notifyInternetDisconnected() {
        for (final IWifiSsidNameConsumer consumer : consumers) {
            Executors
                    .newSingleThreadExecutor()
                    .submit(consumer::onInternetDisconnected);
        }
    }

    public void start() {
        wifiPublisher.subscribe(this);
        IntentFilter intentFilter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        context.registerReceiver(this, intentFilter);
    }

    public void stop() {
        wifiPublisher.unsubscribe(this);
        context.unregisterReceiver(this);
        consumers.clear();
    }

    public boolean subscribe(IWifiSsidNameConsumer consumer) {
        return consumers.add(consumer);
    }

    public boolean unsubscribe(IWifiSsidNameConsumer consumer) {
        return consumers.remove(consumer);
    }

    public static String ssid() {
        return ssid;
    }

    public static Date date() { return date; }

}
