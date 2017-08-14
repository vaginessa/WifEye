package mahorad.com.wifeye.broadcast.manager.app.internet;

import android.content.Context;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import io.reactivex.Observable;
import mahorad.com.wifeye.broadcast.manager.rx.wifi.NetworkStateChangedEvent;
import mahorad.com.wifeye.broadcast.manager.rx.wifi.RxWifiManager;
import mahorad.com.wifeye.util.Utils;

import static android.net.NetworkInfo.DetailedState.CONNECTED;
import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by mahan on 2017-08-13.
 */

public class RxInternetManager {

    private static String TAG = RxInternetManager.class.getSimpleName();

    private static String ssid = "n/a";
    private static boolean connected;

    public static Observable<InternetStateChangedEvent> internetStateChanges(@NonNull Context context) {
        checkNotNull(context, "context == null");
        return RxWifiManager
                .networkStateChanges(context)
                .distinctUntilChanged()
                .map(RxInternetManager::toEvent)
                .distinctUntilChanged();
    }

    private static InternetStateChangedEvent toEvent(NetworkStateChangedEvent e) {
        boolean c1, c2 = true, c3 = true;
        NetworkInfo net = e.networkInfo();
        String ssid = net.getExtraInfo();
        ssid = !valid(ssid) ? "n/a" : ssid.replace("\"", "");
        if (Utils.isNullOrEmpty(RxInternetManager.ssid))
            c1 = false;
        else {
            c1 = net.isAvailable();
            c2 = net.isConnected();
            c3 = net.getDetailedState() == CONNECTED;
        }

        RxInternetManager.ssid = ssid;
        RxInternetManager.connected = c1 && c2 && c3;
        return InternetStateChangedEvent.create(ssid, connected);
    }

    private static boolean valid(String ssid) {
        if (Utils.isNullOrEmpty(ssid)) return false;
        if ("0x".equals(ssid)) return false;
        if ("n/a".equalsIgnoreCase(ssid)) return false;
        return !ssid.contains("unknown");
    }

}
