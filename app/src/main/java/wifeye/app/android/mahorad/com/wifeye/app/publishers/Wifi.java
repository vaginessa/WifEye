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

import wifeye.app.android.mahorad.com.wifeye.app.consumers.IWifiListener;

import static android.content.Context.WIFI_SERVICE;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Wifi.State.*;

public class Wifi extends BroadcastReceiver {

    private static final String TAG = Wifi.class.getSimpleName();

    private final Context context;
    private final WifiManager wifiManager;
    private static State state = Unknown;
    private static Date date;

    private final Set<IWifiListener> consumers;

    /**
     * wifi states
     */
    public enum State {
        Disabling(0), Disabled(1), Enabling(2), Enabled(3), Unknown(4);
        private int value;

        State(int value) {
            this.value = value;
        }

        public static State get(int state) {
            switch (state) {
                case 0: return Disabling;
                case 1: return Disabled;
                case 2: return Enabling;
                case 3: return Enabled;
                default: return Unknown;
            }
        }

        public int value() { return value; }
    }

    /**
     * constructor
     * @param context
     */
    public Wifi(Context context) {
        this.context = context;
        wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        consumers = new HashSet<>();
    }

    @Override
    public synchronized void onReceive(Context context, Intent intent) {
        int wifiState = intent.getIntExtra(
                WifiManager.EXTRA_WIFI_STATE,
                WifiManager.WIFI_STATE_UNKNOWN);
        State state = State.get(wifiState);
        boolean updated = update(state);
        if (updated) publish();
    }

    private boolean update(State wifiState) {
        if (state.equals(wifiState))
            return false;
        state = wifiState;
        date = Calendar.getInstance().getTime();
        return true;
    }

    private void publish() {
        for (IWifiListener consumer : consumers) {
            Executors
                    .newSingleThreadExecutor()
                    .submit(() -> consumer.onWifiStateChanged(state));
        }
    }

    public void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        context.registerReceiver(this, intentFilter);
    }

    public void unregisterBroadcast() {
        context.unregisterReceiver(this);
        consumers.clear();
    }

    public boolean subscribe(IWifiListener consumer) {
        return consumers.add(consumer);
    }

    public boolean unsubscribe(IWifiListener consumer) {
        return consumers.remove(consumer);
    }

    public void enable() {
        if (isEnabled()) return;
        wifiManager.setWifiEnabled(true);
        Log.d(TAG, "[[ enabling wifi... ]]");
    }

    public void disable() {
        if (Internet.ssid() != null)
            return;
        wifiManager.setWifiEnabled(false);
        Log.d(TAG, "[[ disabling wifi... ]]");
    }

    public boolean isEnabled() {
        boolean enabled = (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED);
        Log.d(TAG, String.format("[[ wifiManager %b, this.state %b ]]", enabled, state == Enabled));
        return enabled;
    }

    public boolean isDisabled() {
        boolean disabled = (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED);
        Log.d(TAG, String.format("[[ wifiManager %b, this.state %b ]]", disabled, state == Disabled));
        return disabled;
    }

    public static State state() {
        return state;
    }

    public static Date date() {
        return date;
    }

}
