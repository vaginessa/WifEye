package mahorad.com.wifeye.publisher.event.tower;


import android.content.Context;

import java.util.Objects;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import mahorad.com.wifeye.publisher.broadcast.telephony.RxTelephonyManager;

import static dagger.internal.Preconditions.checkNotNull;
import static io.reactivex.BackpressureStrategy.LATEST;
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
        return RxTelephonyManager
                .cellLocationChanges(context)
                .map(Object::toString)
                .filter(RxCellTowerMonitor::isValid)
                .map(RxCellTowerMonitor::toEvent)
                .toFlowable(LATEST);
    }

    @NonNull
    private static CellTowerIdChangedEvent toEvent(String tower) {
        return CellTowerIdChangedEvent.create(tower, exist(tower));
    }

    private static boolean isValid(String ctid) {
        return !isNullOrEmpty(ctid) && !Objects.equals(ctid, UNKNOWN_CTID);
    }

}
