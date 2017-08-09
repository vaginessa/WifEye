package wifeye.app.android.mahorad.com.wifeye.app.publishers;

import android.content.Context;
import android.net.NetworkInfo;

import java.util.Date;

import io.reactivex.Observable;
import wifeye.app.android.mahorad.com.wifeye.app.events.InternetEvent;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.rx.wifi.NetworkStateChangedEvent;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.rx.wifi.RxWifiManager;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

import static android.net.NetworkInfo.DetailedState.CONNECTED;

/**
 * listens to connected ssid names and notifies consumers
 * if the Internet is connected or disconnected.
 */
public class Internet {

    private static String TAG = Internet.class.getSimpleName();

    private static String ssid = "n/a";
    private static Date date = Utilities.now();
    private static boolean connected;

    public static Observable<InternetEvent> observable(Context context) {
        return RxWifiManager
                .networkStateChanges(context)
                .distinctUntilChanged()
                .map(Internet::toEvent)
                .distinctUntilChanged();
    }

    private synchronized static InternetEvent toEvent(NetworkStateChangedEvent e) {
        boolean c1, c2 = true, c3 = true;
        NetworkInfo net = e.networkInfo();
        String ssid = net.getExtraInfo();
        ssid = !valid(ssid) ? "n/a" : ssid.replace("\"", "");
        if (Utilities.isNullOrEmpty(Internet.ssid))
            c1 = false;
        else {
            c1 = net.isAvailable();
            c2 = net.isConnected();
            c3 = net.getDetailedState() == CONNECTED;
        }

        if (!ssid.equals(Internet.ssid)) {
            date = Utilities.now();
        }
        Internet.ssid = ssid;
        Internet.connected = c1 && c2 && c3;
        return InternetEvent.create(ssid, connected, date);
    }

    private static boolean valid(String ssid) {
        if (Utilities.isNullOrEmpty(ssid)) return false;
        if ("0x".equals(ssid)) return false;
        if ("n/a".equalsIgnoreCase(ssid)) return false;
        return !ssid.contains("unknown");
    }

    public static String ssid() {
        return ssid;
    }

    public static Date date() { return date; }

    public static boolean connected() {
        return connected;
    }

    public static InternetEvent lastEvent() {
        return InternetEvent.create(ssid, connected, date);
    }
}
