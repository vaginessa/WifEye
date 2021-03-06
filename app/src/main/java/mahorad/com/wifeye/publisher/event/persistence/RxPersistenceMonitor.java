package mahorad.com.wifeye.publisher.event.persistence;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

import static io.reactivex.BackpressureStrategy.LATEST;
import static mahorad.com.wifeye.util.Utils.isNullOrEmpty;

/**
 * Created by mahan on 2017-08-13.
 */

public abstract class RxPersistenceMonitor {

    public static final String TAG = RxPersistenceMonitor.class.getSimpleName();

    private static final BehaviorSubject<PersistenceChangedEvent> source = BehaviorSubject.create();

    public static void notify(String ssid, String ctid) {
        if (isNullOrEmpty(ssid) || isNullOrEmpty(ctid))
            return;
        Timber.tag(TAG).d("PERSIST: ssid: %s, ctid: %s", ssid, ctid);
        source.onNext(PersistenceChangedEvent.create(ssid, ctid));
    }

    public static Flowable<PersistenceChangedEvent> persistenceChanges() {
        return source
                .distinctUntilChanged()
                .toFlowable(LATEST);
    }


}
