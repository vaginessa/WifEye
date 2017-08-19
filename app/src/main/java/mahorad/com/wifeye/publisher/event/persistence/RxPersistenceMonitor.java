package mahorad.com.wifeye.publisher.event.persistence;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;
import mahorad.com.wifeye.util.Utils;

import static mahorad.com.wifeye.util.Utils.isNullOrEmpty;

/**
 * Created by mahan on 2017-08-13.
 */

public abstract class RxPersistenceMonitor {

    public static final String TAG = RxPersistenceMonitor.class.getSimpleName();

    private static final ReplaySubject<PersistenceChangedEvent> source = ReplaySubject.createWithSize(1);

    public static synchronized void notify(String ssid, String ctid) {
        if (isNullOrEmpty(ssid) || isNullOrEmpty(ctid))
            return;
        source.onNext(PersistenceChangedEvent.create(ssid, ctid));
    }

    public static Flowable<PersistenceChangedEvent> persistenceChanges() {
        return source
                .distinctUntilChanged()
                .toFlowable(BackpressureStrategy.LATEST)
                .observeOn(Schedulers.newThread());
    }


}
