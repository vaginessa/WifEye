package mahorad.com.wifeye.publisher.event.wifi;

import io.reactivex.Observable;
import io.reactivex.subjects.ReplaySubject;
import mahorad.com.wifeye.engine.wifi.WifiAction;

/**
 * Created by mahan on 2017-08-14.
 */

public class RxWifiActionMonitor {

    private static final String TAG = RxWifiActionMonitor.class.getSimpleName();

    private static ReplaySubject<WifiAction> source = ReplaySubject.create();

    public static synchronized void notify(WifiAction action) {
        if (action == null)
            return;
        source.onNext(action);
    }

    public static Observable<WifiAction> wifiActionChanges() {
        return source.distinctUntilChanged();
    }
}
