package wifeye.app.android.mahorad.com.wifeye.app.publishers;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.Date;

import io.reactivex.Observable;
import wifeye.app.android.mahorad.com.wifeye.app.events.WifiEvent;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.rx.wifi.RxWifiManager;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Wifi.State.Enabled;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Wifi.State.Unknown;

public class Wifi {

    private static final String TAG = Wifi.class.getSimpleName();

    private final WifiManager wifiManager;
    private static State state = Unknown;
    private static Date date = Utilities.now();

    public enum State {

        Unknown(-1), Disabling(0), Disabled(1), Enabling(2), Enabled(3);

        private int value;

        State(int value) {
            this.value = value;
        }

        public static State toState(int state) {
            switch (state) {
                case 0: return Disabling;
                case 1: return Disabled;
                case 2: return Enabling;
                case 3: return Enabled;
                default: return Unknown;
            }
        }

        public int value() {
            return value;
        }
    }

    /**
     *
     * @param context
     */
    public Wifi(Context context) {
        this.wifiManager = Utilities.getWifiManager(context);
    }

    public static Observable<WifiEvent> observable(Context context) {
        return RxWifiManager
                .wifiStateChanges(context)
                .distinctUntilChanged()
                .map(State::toState)
                .map(Wifi::toEvent);
    }

    private static WifiEvent toEvent(State state) {
        Wifi.date = Utilities.now();
        Wifi.state = state;
        return WifiEvent.create(Wifi.state, Wifi.date());
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
        Log.d(TAG, String.format("[[ wifiManager %b, this.wifiEvent %b ]]", enabled, state == Enabled));
        return enabled;
    }

    public static State state() {
        return state;
    }

    public static Date date() {
        return date;
    }

    public static WifiEvent lastEvent() {
        return WifiEvent.create(state, date);
    }

}
