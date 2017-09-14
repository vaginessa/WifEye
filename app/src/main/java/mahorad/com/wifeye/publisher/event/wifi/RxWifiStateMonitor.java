package mahorad.com.wifeye.publisher.event.wifi;

import android.content.Context;
import android.support.annotation.NonNull;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import mahorad.com.wifeye.publisher.broadcast.wifi.RxWifiManager;

import static dagger.internal.Preconditions.checkNotNull;
import static io.reactivex.BackpressureStrategy.LATEST;
import static mahorad.com.wifeye.util.Constants.WIFI_STATE_DISABLED;
import static mahorad.com.wifeye.util.Constants.WIFI_STATE_ENABLED;
import static mahorad.com.wifeye.util.Constants.WIFI_STATE_UNKNOWN;

/**
 * Created by mahan on 2017-08-14.
 */

public class RxWifiStateMonitor {

    public static Flowable<Boolean> wifiStateChanges(@NonNull Context context) {
        checkNotNull(context, "context == null");
        return RxWifiManager
                    .wifiStateChanges(context)
                    .filter(RxWifiStateMonitor::isImportant)
                    .distinctUntilChanged()
                    .map(wifiState -> wifiState == WIFI_STATE_ENABLED)
                    .distinctUntilChanged()
                    .toFlowable(LATEST);
    }

    private static boolean isImportant(Integer state) {
        return state == WIFI_STATE_DISABLED ||
               state == WIFI_STATE_ENABLED  ||
               state == WIFI_STATE_UNKNOWN;
    }

}
