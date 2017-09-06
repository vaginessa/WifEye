package mahorad.com.wifeye.publisher.event.internet;

import android.content.Context;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import mahorad.com.wifeye.publisher.broadcast.wifi.NetworkStateChangedEvent;
import mahorad.com.wifeye.publisher.broadcast.wifi.RxWifiManager;
import mahorad.com.wifeye.util.Utils;

import static android.net.NetworkInfo.DetailedState.CONNECTED;
import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by mahan on 2017-08-13.
 */

public class RxInternetMonitor {

    private static String TAG = RxInternetMonitor.class.getSimpleName();

    private static String ssid = "n/a";
    private static boolean connected;

    public static Flowable<InternetStateChangedEvent> internetStateChanges(@NonNull Context context) {
        checkNotNull(context, "context == null");
        return RxWifiManager
                .networkStateChanges(context)
                .distinctUntilChanged()
                .map(RxInternetMonitor::toInternetStateEvent)
                .distinctUntilChanged()
                .toFlowable(BackpressureStrategy.LATEST);
    }

    private static InternetStateChangedEvent toInternetStateEvent(NetworkStateChangedEvent e) {
        boolean c1, c2 = true, c3 = true;
        NetworkInfo net = e.networkInfo();
        String ssid = net.getExtraInfo();
        ssid = !valid(ssid) ? "n/a" : ssid.replace("\"", "");
        if (Utils.isNullOrEmpty(RxInternetMonitor.ssid))
            c1 = false;
        else {
            c1 = net.isAvailable();
            c2 = net.isConnected();
            c3 = net.getDetailedState() == CONNECTED;
        }

        RxInternetMonitor.ssid = ssid;
        RxInternetMonitor.connected = c1 && c2 && c3;
        return InternetStateChangedEvent.create(ssid, connected);
    }

    private static boolean valid(String ssid) {
        if (Utils.isNullOrEmpty(ssid)) return false;
        if ("0x".equals(ssid)) return false;
        if ("n/a".equalsIgnoreCase(ssid)) return false;
        return !ssid.contains("unknown");
    }

}
