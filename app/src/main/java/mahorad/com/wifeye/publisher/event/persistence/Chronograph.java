package mahorad.com.wifeye.publisher.event.persistence;

import android.content.Context;
import android.support.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import mahorad.com.wifeye.publisher.event.engine.RxEngineStateMonitor;
import mahorad.com.wifeye.publisher.event.internet.RxInternetMonitor;
import mahorad.com.wifeye.publisher.event.tower.RxCellTowerMonitor;
import mahorad.com.wifeye.publisher.event.wifi.RxWifiActionMonitor;
import mahorad.com.wifeye.publisher.event.wifi.RxWifiStateMonitor;

import static mahorad.com.wifeye.data.Persistence.persist;
import static mahorad.com.wifeye.publisher.event.persistence.EventType.CellTower;
import static mahorad.com.wifeye.publisher.event.persistence.EventType.EngineState;
import static mahorad.com.wifeye.publisher.event.persistence.EventType.Internet;
import static mahorad.com.wifeye.publisher.event.persistence.EventType.Persistence;
import static mahorad.com.wifeye.publisher.event.persistence.EventType.WifiAction;
import static mahorad.com.wifeye.publisher.event.persistence.EventType.WifiState;

/**
 * Created by mahan on 2017-08-18.
 */

public class Chronograph {

    private CompositeDisposable disposables = new CompositeDisposable();

    private Context context;

    public Chronograph(Context context) {
        this.context = context;
    }

    public void start() {
        disposables.add(wifiStateDates());
        disposables.add(wifiActionDates());
        disposables.add(internetDates());
        disposables.add(cellTowerDates());
        disposables.add(engineStateDates());
        disposables.add(persistenceDates());
    }

    @NonNull
    private Disposable wifiStateDates() {
        return RxWifiStateMonitor
                .wifiStateChanges(context)
                .subscribe(b -> persist(WifiState));
    }

    @NonNull
    private Disposable wifiActionDates() {
        return RxWifiActionMonitor
                .wifiActionChanges()
                .subscribe(b -> persist(WifiAction));
    }

    @NonNull
    private Disposable internetDates() {
        return RxInternetMonitor
                .internetStateChanges(context)
                .subscribe(b -> persist(Internet));
    }

    @NonNull
    private Disposable cellTowerDates() {
        return RxCellTowerMonitor
                .cellTowerIdChanges(context)
                .subscribe(b -> persist(CellTower));
    }

    @NonNull
    private Disposable engineStateDates() {
        return RxEngineStateMonitor
                .engineStateChanges()
                .subscribe(b -> persist(EngineState));
    }

    @NonNull
    private Disposable persistenceDates() {
        return RxPersistenceMonitor
                .persistenceChanges()
                .subscribe(b -> persist(Persistence));
    }

    public void stop() {
        disposables.clear();
    }
}
