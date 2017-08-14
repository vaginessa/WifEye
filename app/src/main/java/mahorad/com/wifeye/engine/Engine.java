package mahorad.com.wifeye.engine;

import android.content.Context;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.ReplaySubject;
import mahorad.com.wifeye.base.BaseApplication;
import mahorad.com.wifeye.broadcast.manager.rx.wifi.RxWifiManager;
import mahorad.com.wifeye.di.qualifier.ApplicationContext;
import mahorad.com.wifeye.di.qualifier.engine.CloseRangeState;
import mahorad.com.wifeye.di.qualifier.engine.ConnectedState;
import mahorad.com.wifeye.di.qualifier.engine.DisconnectedState;
import mahorad.com.wifeye.di.qualifier.engine.InitialState;
import mahorad.com.wifeye.di.qualifier.engine.NearbyAreaState;
import mahorad.com.wifeye.di.qualifier.engine.RemoteAreaState;
import mahorad.com.wifeye.engine.state.IState;
import mahorad.com.wifeye.util.Utils;
import timber.log.Timber;

import static mahorad.com.wifeye.engine.EngineStateChangedEvent.create;

/**
 * A state machine and an actuator.
 * It keeps the system state as well as allowing
 * different states to make the right action.
 */
public class Engine implements IActuator {

    private static final String TAG = Engine.class.getSimpleName();

    private static final ReplaySubject<EngineStateChangedEvent> stateChanges = ReplaySubject.createWithSize(1);

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

    private IState currentState = initial;

    public void start() {
        injectDependencies();

    }

    private void injectDependencies() {
        BaseApplication
                .component()
                .inject(this);
    }

    public void stop() {
    }

    //    @Override
//    public synchronized void internetConnected(String ssid) {
//        Log.i(TAG, String.format("--| EVENT: connected to %s |", ssid));
//        currentState.onInternetConnected();
//    }
//
//    @Override
//    public synchronized void internetDisconnected() {
//        Log.i(TAG, String.format("--| EVENT: disconnected |"));
//        currentState.onInternetDisconnects();
//    }
//
//    @Override
//    public synchronized void receivedKnownTowerId() {
//        Log.i(TAG, String.format("--| EVENT: known ctid |"));
//        currentState.onReceivedKnownTowerId();
//    }
//
//    @Override
//    public synchronized void receivedUnknownTowerId(String ctid) {
//        Log.i(TAG, String.format("--| EVENT: unknown ctid %s |", ctid));
//        currentState.onReceivedUnknownTowerId();
//    }

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

    private synchronized void setState(IState state) {
        if (currentState == state)
            return;
        currentState = state;
        EngineStateChangedEvent e = create(currentState.type(), Utils.now());
        stateChanges.onNext(e);
        Timber.tag(TAG).i("ENGINE STATE CHANGED TO : %S", currentState.toString());
    }

    public static Observable<EngineStateChangedEvent> stateChangeObservable() {
        return stateChanges.hide();
    }

    /* wifi actions */
    @Override
    public void disableWifi() {
    }

    @Override
    public void observeWifi() {
    }

    @Override
    public void haltWifiActions() {
    }

    @Override
    public void persist() {

    }
}