package mahorad.com.wifeye.publisher.event.wifi;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;
import timber.log.Timber;

/**
 * Created by mahan on 2017-08-18.
 */

public class RxWifiActionTimerMonitor {

    private static final String TAG = RxWifiActionTimerMonitor.class.getSimpleName();

    private static final ReplaySubject<Long> source = ReplaySubject.createWithSize(1);

    public static void notify(Long tick) {
        if (tick < 0) return;
        Timber.tag(TAG).w("TICK: %d", tick);
        source.onNext(tick);
    }

    public static Flowable<Long> timerTickChanges() {
        return source
                .distinctUntilChanged()
                .toFlowable(BackpressureStrategy.LATEST)
                .observeOn(Schedulers.newThread());
    }
}
