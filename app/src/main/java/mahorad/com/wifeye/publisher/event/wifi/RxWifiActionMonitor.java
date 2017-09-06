package mahorad.com.wifeye.publisher.event.wifi;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subjects.BehaviorSubject;
import mahorad.com.wifeye.engine.wifi.WifiAction;
import timber.log.Timber;

/**
 * Created by mahan on 2017-08-14.
 */

public class RxWifiActionMonitor {

    private static final String TAG = RxWifiActionMonitor.class.getSimpleName();

    private static BehaviorSubject<WifiAction> source = BehaviorSubject.create();

    public static void notify(WifiAction action) {
        if (action == null) return;
        Timber.tag(TAG).d("ACTION: %s", action);
        source.onNext(action);
    }

    public static Flowable<WifiAction> wifiActionChanges() {
        return source
                .distinctUntilChanged()
                .toFlowable(BackpressureStrategy.LATEST);
    }
}
