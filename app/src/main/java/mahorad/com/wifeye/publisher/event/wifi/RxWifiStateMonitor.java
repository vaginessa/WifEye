package mahorad.com.wifeye.publisher.event.wifi;

import android.content.Context;
import android.support.annotation.NonNull;

import io.reactivex.Observable;
import mahorad.com.wifeye.publisher.broadcast.wifi.RxWifiManager;

import static dagger.internal.Preconditions.checkNotNull;
import static mahorad.com.wifeye.util.Constants.WIFI_STATE_DISABLED;
import static mahorad.com.wifeye.util.Constants.WIFI_STATE_ENABLED;
import static mahorad.com.wifeye.util.Constants.WIFI_STATE_UNKNOWN;

/**
 * Created by mahan on 2017-08-14.
 */

public class RxWifiStateMonitor {

    public Observable<Boolean> wifiStateChanges(@NonNull Context context) {
        checkNotNull(context, "context == null");
        return RxWifiManager
//                how to publish on a new thread
//                .toFlowable(BackpressureStrategy.LATEST)
//                .observeOn(Schedulers.io())
                .wifiStateChanges(context)
                .filter(i ->
                        i == WIFI_STATE_DISABLED ||
                        i == WIFI_STATE_ENABLED  ||
                        i == WIFI_STATE_UNKNOWN)
                .distinctUntilChanged()
                .map(wifiState -> wifiState == WIFI_STATE_ENABLED)
                .distinctUntilChanged();
    }

}
