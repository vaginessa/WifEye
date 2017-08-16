package mahorad.com.wifeye.publisher.event.tower;


import android.content.Context;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import mahorad.com.wifeye.data.Persistence;
import mahorad.com.wifeye.publisher.broadcast.telephony.RxTelephonyManager;
import mahorad.com.wifeye.publisher.event.internet.InternetStateChangedEvent;
import mahorad.com.wifeye.publisher.event.internet.RxInternetMonitor;

import static dagger.internal.Preconditions.checkNotNull;
import static mahorad.com.wifeye.util.Constants.UNKNOWN_CTID;
import static mahorad.com.wifeye.util.Utils.isNullOrEmpty;

/**
 * Created by mahan on 2017-08-13.
 */

public class RxCellTowerMonitor {

    private static Disposable internetChanges;
    private static InternetStateChangedEvent event;

    public static Observable<CellTowerIdChangedEvent> cellTowerIdChanges(@NonNull Context context) {
        checkNotNull(context, "context == null");
        subscribeInternetStateChanges(context);
        return RxTelephonyManager
                .cellLocationChanges(context)
                .distinctUntilChanged()
                .map(Object::toString)
                .filter(RxCellTowerMonitor::isValid)
                .map(RxCellTowerMonitor::toEvent)
                .doOnDispose(() -> internetChanges.dispose());
    }

    private static boolean isValid(String ctid) {
        return !isNullOrEmpty(ctid) && !Objects.equals(ctid, UNKNOWN_CTID);
    }

    private static void subscribeInternetStateChanges(Context context) {
        if (internetChanges != null) return;
        internetChanges = RxInternetMonitor
                .internetStateChanges(context)
                .subscribe(e -> event = e);
    }

    private static CellTowerIdChangedEvent toEvent(String ctid) {
        boolean known = event.connected()
                        ? Persistence.exist(event.ssid(), ctid)
                        : Persistence.exist(ctid);
        return CellTowerIdChangedEvent.create(ctid, known);
    }

}
