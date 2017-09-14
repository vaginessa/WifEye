package mahorad.com.wifeye.publisher.event.engine;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subjects.BehaviorSubject;
import mahorad.com.wifeye.engine.state.StateType;
import timber.log.Timber;

import static io.reactivex.BackpressureStrategy.LATEST;

/**
 * Created by mahan on 2017-08-14.
 */

public class RxEngineStateMonitor {

    private static final String TAG = RxEngineStateMonitor.class.getSimpleName();

    private static BehaviorSubject<StateType> source = BehaviorSubject.create();

    public static void notify(StateType type) {
        if (type == null)
            return;
        Timber.tag(TAG).d("ENGINE STATE: %s", type);
        source.onNext(type);
    }

    public static Flowable<StateType> engineStateChanges() {
        return source
                .distinctUntilChanged()
                .toFlowable(LATEST);
    }
}
