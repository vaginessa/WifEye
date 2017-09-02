package mahorad.com.wifeye.engine;

import android.content.Context;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import mahorad.com.wifeye.data.Persistence;
import mahorad.com.wifeye.di.component.DaggerServiceComponent;
import mahorad.com.wifeye.di.qualifier.ApplicationContext;
import mahorad.com.wifeye.di.qualifier.engine.CloseRangeState;
import mahorad.com.wifeye.di.qualifier.engine.ConnectedState;
import mahorad.com.wifeye.di.qualifier.engine.DisconnectedState;
import mahorad.com.wifeye.di.qualifier.engine.InitialState;
import mahorad.com.wifeye.di.qualifier.engine.NearbyAreaState;
import mahorad.com.wifeye.di.qualifier.engine.RemoteAreaState;
import mahorad.com.wifeye.engine.state.IState;
import mahorad.com.wifeye.engine.wifi.WifiHandler;
import mahorad.com.wifeye.publisher.event.engine.RxEngineStateMonitor;
import mahorad.com.wifeye.publisher.event.internet.RxInternetMonitor;
import mahorad.com.wifeye.publisher.event.tower.RxCellTowerMonitor;
import timber.log.Timber;

import static mahorad.com.wifeye.base.BaseApplication.component;
import static mahorad.com.wifeye.util.Utils.isNullOrEmpty;

/**
 * A state machine and an actuator.
 * It keeps the system state as well as allowing
 * different states to make the right action.
 */
public class Engine {

    private static final String TAG = Engine.class.getSimpleName();

    @Inject
    @InitialState
    IState initial;

    @Inject
    @ConnectedState
    IState connected;

    @Inject
    @DisconnectedState
    IState disconnected;

    @Inject
    @NearbyAreaState
    IState nearbyArea;

    @Inject
    @RemoteAreaState
    IState remoteArea;

    @Inject
    @CloseRangeState
    IState closeRange;

    @Inject
    @ApplicationContext
    Context context;

    @Inject
    WifiHandler wifiHandler;

    @Inject
    CompositeDisposable compositeDisposable;

    private boolean started;
    private volatile IState currentState;

    private static String ssid;
    private static String ctid;

    public Engine() {}

    public void start() {
        if (started) return;
        started = true;
        injectDependencies();
        wifiHandler.start();
        currentState = initial;
        compositeDisposable.add(subscribeCellTower());
        compositeDisposable.add(subscribeInternet());
    }

    private void injectDependencies() {
        DaggerServiceComponent
                .builder()
                .applicationComponent(component())
                .build()
                .inject(this);
    }

    private Disposable subscribeInternet() {
        return RxInternetMonitor
                .internetStateChanges(context)
                .subscribe(e -> {
                    Timber.tag(TAG).d("RxInternetMonitor: ssid: %s, connected %b", e.ssid(), e.connected());
                    if (e.connected()) {
                        internetConnected(e.ssid());
                    } else {
                        internetDisconnected();
                    }
                }, Timber::e);
    }

    private Disposable subscribeCellTower() {
        return RxCellTowerMonitor
                .cellTowerIdChanges(context)
                .subscribe(e -> {
                    Timber.tag(TAG).d("RxCellTowerMonitor: ctid: %s, known %b", e.ctid(), e.known());
                    if (e.known()) {
                        receivedKnownTowerId(e.ctid());
                    } else {
                        receivedUnknownTowerId(e.ctid());
                    }
                }, Timber::e);
    }

    public void stop() {
        if (!started) return;
        started = false;
        compositeDisposable.clear();
        wifiHandler.stop();
    }

    public void internetConnected(String ssid) {
        this.ssid = ssid;
        Timber.tag(TAG).v("EVENT: connected to %s", ssid);
        currentState.onInternetConnected(ssid);
    }

    public void internetDisconnected() {
        Timber.tag(TAG).v("EVENT: disconnected");
        currentState.onInternetDisconnects();
    }

    public void receivedKnownTowerId(String ctid) {
        this.ctid = ctid;
        Timber.tag(TAG).v("EVENT: known ctid %s", ctid);
        currentState.onReceivedKnownTowerId(ctid);
    }

    public void receivedUnknownTowerId(String ctid) {
        this.ctid = ctid;
        Timber.tag(TAG).v("EVENT: unknown ctid %s", ctid);
        currentState.onReceivedUnknownTowerId(ctid);
    }

    /* used by states */
    public void toConnectedState() {
        setState(connected);
    }

    public void toDisconnectedState() {
        setState(disconnected);
    }

    public void toNearbyAreaState() {
        setState(nearbyArea);
    }

    public void toRemoteAreaState() {
        setState(remoteArea);
    }

    public void toCloseRangeState() {
        setState(closeRange);
    }

    private void setState(IState state) {
        if (currentState == state)
            return;
        currentState = state;
        RxEngineStateMonitor.notify(currentState.type());
    }

    /* wifi actions */
    public void disableWifi() {
        Timber.tag(TAG).v("ACTION: Disable");
        wifiHandler.runDisabler();
    }

    public void observeWifi() {
        Timber.tag(TAG).v("ACTION: Observe");
        wifiHandler.runObserver();
    }

    public void haltWifiActions() {
        Timber.tag(TAG).v("ACTION: Halt...");
        wifiHandler.haltActions();
    }

    public void persistSsid(String ssid) {
        if (isNullOrEmpty(ctid))
            return;
        Persistence.persist(ssid, ctid);
    }

    public void persistCtid(String ctid) {
        if (isNullOrEmpty(ssid))
            return;
        Persistence.persist(ssid, ctid);
    }
}