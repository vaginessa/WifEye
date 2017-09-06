package mahorad.com.wifeye.publisher.event.tower;


import android.content.Context;

import java.util.Objects;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import mahorad.com.wifeye.publisher.broadcast.telephony.RxTelephonyManager;
import mahorad.com.wifeye.publisher.event.internet.InternetStateChangedEvent;
import mahorad.com.wifeye.publisher.event.internet.RxInternetMonitor;

import static dagger.internal.Preconditions.checkNotNull;
import static io.reactivex.Flowable.combineLatest;
import static mahorad.com.wifeye.data.Persistence.exist;
import static mahorad.com.wifeye.util.Constants.UNKNOWN_CTID;
import static mahorad.com.wifeye.util.Utils.isNullOrEmpty;

/**
 * Created by mahan on 2017-08-13.
 */

public class RxCellTowerMonitor {

    private static final String TAG = RxCellTowerMonitor.class.getSimpleName();

    public static Flowable<CellTowerIdChangedEvent> cellTowerIdChanges(@NonNull Context context) {
        checkNotNull(context, "context == null");
        return combineLatest(
                    observeTowerIdChanges(context),
                    observeInternetChanges(context),
                    toEvent())
                .distinctUntilChanged();
    }

    private static Flowable<String> observeTowerIdChanges(@NonNull Context context) {
        return RxTelephonyManager
                .cellLocationChanges(context)
                .distinctUntilChanged()
                .map(Object::toString)
                .filter(RxCellTowerMonitor::isValid)
                .toFlowable(BackpressureStrategy.LATEST);
    }

    private static Flowable<InternetStateChangedEvent> observeInternetChanges(@NonNull Context context) {
        return RxInternetMonitor
                .internetStateChanges(context);
    }

    @NonNull
    private static BiFunction<String, InternetStateChangedEvent, CellTowerIdChangedEvent> toEvent() {
        return (tower, net) -> {
            boolean known = net.connected()
                    ? exist(net.ssid(), tower)
                    : exist(tower);
            return CellTowerIdChangedEvent.create(tower, known);
        };
    }

    private static boolean isValid(String ctid) {
        return !isNullOrEmpty(ctid) && !Objects.equals(ctid, UNKNOWN_CTID);
    }

}
