package mahorad.com.wifeye.publisher.event.service;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by mahan on 2017-09-05.
 */

public class RxEngineServiceMonitor {

    private static final String TAG = RxEngineServiceMonitor.class.getSimpleName();

    private static final BehaviorSubject<Boolean> source = BehaviorSubject.create();

    public static Flowable<Boolean> serviceStateChanges() {
        return source
                .distinctUntilChanged()
                .toFlowable(BackpressureStrategy.LATEST);
    }

    public static void notify(boolean enabled) {
        source.onNext(enabled);
    }

}
