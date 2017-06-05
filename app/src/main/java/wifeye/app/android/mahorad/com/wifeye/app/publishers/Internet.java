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

import wifeye.app.android.mahorad.com.wifeye.app.consumers.IWifiListener;
import wifeye.app.android.mahorad.com.wifeye.app.consumers.IInternetListener;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

/**
 * listens to connected ssid names and notifies consumers
 * if the Internet is connected or disconnected.
 */
public class Internet
        extends BroadcastReceiver
        implements IWifiListener {

    private static String TAG = Internet.class.getSimpleName();

    private static String ssid = null;
    private static Date date = Calendar.getInstance().getTime();;
    private final WifiManager wifiManager;
    private final Wifi wifi;
    private final Context context;
    private final Set<IInternetListener> consumers;

    public Internet(Context context, Wifi wifi) {
        this.context = context;
        this.wifi = wifi;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        consumers = new HashSet<>();
    }

    @Override
    public void onWifiStateChanged(Wifi.State state) {
        if (!wifi.isDisabled()) return;
        notifyInternetDisconnected();
    }

    @Override
    public synchronized void onReceive(Context context, Intent intent) {
        if (!wifi.isEnabled()) {
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

        hotSpot = hotSpot.replace("\"", "");
        if (Utilities.areEqual(ssid, hotSpot)) return;
        ssid = hotSpot;
        date = Calendar.getInstance().getTime();
        notifyInternetGotConnected();
    }

    private synchronized boolean isValidName(String hotSpot) {
        if (Utilities.isNullOrEmpty(hotSpot)) return false;
        if ("0x".equals(hotSpot)) return false;
        return !hotSpot.contains("unknown");
    }

    private void notifyInternetGotConnected() {
        for (final IInternetListener consumer : consumers) {
            Executors
                    .newSingleThreadExecutor()
                    .submit(() -> consumer.onInternetConnected(ssid));
        }
    }

    private void notifyInternetDisconnected() {
        for (final IInternetListener consumer : consumers) {
            Executors
                    .newSingleThreadExecutor()
                    .submit(consumer::onInternetDisconnected);
        }
    }

    public void registerBroadcast() {
        wifi.subscribe(this);
        IntentFilter intentFilter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        context.registerReceiver(this, intentFilter);
    }

    public void unregisterBroadcast() {
        wifi.unsubscribe(this);
        context.unregisterReceiver(this);
        consumers.clear();
    }

    public boolean subscribe(IInternetListener consumer) {
        return consumers.add(consumer);
    }

    public boolean unsubscribe(IInternetListener consumer) {
        return consumers.remove(consumer);
    }

    public static String ssid() {
        return ssid;
    }

    public static Date date() { return date; }

    public static boolean connected() {
        return ssid != null;
    }
}
