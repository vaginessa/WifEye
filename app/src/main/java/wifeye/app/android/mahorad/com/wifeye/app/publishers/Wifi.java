package wifeye.app.android.mahorad.com.wifeye.app.publishers;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.Date;

import io.reactivex.Observable;
import wifeye.app.android.mahorad.com.wifeye.app.events.WifiEvent;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.rx.wifi.RxWifiManager;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Wifi.State.Disabled;
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

    private synchronized static WifiEvent toEvent(State state) {
        if (state != Wifi.state()) {
            Wifi.date = Utilities.now();
        }
        Wifi.state = state;
        return WifiEvent.create(Wifi.state, Wifi.date());
    }

    public void enable() {
        wifiManager.setWifiEnabled(true);
        Log.d(TAG, "[[ enabling wifi... ]]");
    }

    public void disable() {
        if (Internet.connected()) return;
        wifiManager.setWifiEnabled(false);
        Log.d(TAG, "[[ disabling wifi... ]]");
    }

    public static boolean isEnabled() {
        return state == Enabled;
    }

    public static boolean isDisabled() {
        return state == Disabled;
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
