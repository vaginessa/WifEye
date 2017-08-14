package mahorad.com.wifeye.broadcast.manager.app.tower;


import android.content.Context;
import android.telephony.CellLocation;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import mahorad.com.wifeye.broadcast.manager.app.internet.InternetStateChangedEvent;
import mahorad.com.wifeye.broadcast.manager.app.internet.RxInternetManager;
import mahorad.com.wifeye.broadcast.manager.app.persist.RxPersistManager;
import mahorad.com.wifeye.broadcast.manager.rx.telephony.RxTelephonyManager;
import mahorad.com.wifeye.util.Utils;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by mahan on 2017-08-13.
 */
public class RxTowerIdManager {

    private static Disposable internetChanges;
    private static InternetStateChangedEvent event;

    public static Observable<CellTowerIdChangedEvent> cellTowerIdChanges(@NonNull Context context) {
        checkNotNull(context, "context == null");
        subscribeInternetStateChanges(context);
        return RxTelephonyManager
                .cellLocationChanges(context)
                .distinctUntilChanged()
                .map(RxTowerIdManager::toEvent)
                .doOnDispose(() -> internetChanges.dispose());
    }

    private static void subscribeInternetStateChanges(Context context) {
        if (internetChanges != null) return;
        internetChanges = RxInternetManager
                .internetStateChanges(context)
                .subscribe(e -> event = e);
    }

    private static CellTowerIdChangedEvent toEvent(CellLocation location) {
        String ctid = (location == null ? "" : location.toString());
        boolean known = Utils.isNullOrEmpty(ctid) ||
                (event.connected()
                        ? RxPersistManager.exist(event.ssid(), ctid)
                        : RxPersistManager.exist(ctid));
        return CellTowerIdChangedEvent.create(ctid, known);
    }

}
