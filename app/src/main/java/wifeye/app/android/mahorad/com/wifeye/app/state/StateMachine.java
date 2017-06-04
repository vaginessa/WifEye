package wifeye.app.android.mahorad.com.wifeye.app.state;

import android.util.Log;

import javax.inject.Inject;

import wifeye.app.android.mahorad.com.wifeye.app.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.app.consumers.ISignalListener;
import wifeye.app.android.mahorad.com.wifeye.app.consumers.IInternetListener;
import wifeye.app.android.mahorad.com.wifeye.app.persist.IPersistence;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Engine;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Internet;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Signal;
import wifeye.app.android.mahorad.com.wifeye.app.wifi.WifiDevice;

/**
 * A state machine and an actuator.
 * It keeps the system state as well as allowing
 * different states to make the right action.
 */
public class StateMachine implements
        IStateMachine,
        IActuator,
        IInternetListener,
        ISignalListener {

    private static final String TAG = StateMachine.class.getSimpleName();

    private final IState initial = new StateInitial(this);
    private final IState connected = new StateConnected(this);
    private final IState disconnected = new StateDisconnected(this);
    private final IState knownArea = new StateKnownArea(this);
    private final IState unknownArea = new StateUnknownArea(this);
    private final IState routerArea = new StateRouterArea(this);

    private IState currentState = initial;
    private String ssid;
    private String ctid;

    @Inject Internet internet;
    @Inject Signal signal;
    @Inject Engine engine;
    @Inject WifiDevice wifiDevice;
    @Inject IPersistence persistence;

    /**
     * Creates a state machine stateMachine with wifi controller
     * and persistence abilities.
     */
    public StateMachine() {
        MainApplication
                .mainComponent()
                .inject(this);
    }

    /* listeners */
    @Override
    public void onInternetConnected(String ssid) {
        synchronized (this) {
            internetConnected(ssid);
        }
    }

    @Override
    public void onInternetDisconnected() {
        synchronized (this) {
            internetDisconnected();
        }
    }

    @Override
    public void onReceivedKnownTowerId(String ctid) {
        synchronized (this) {
            receivedKnownTowerId();
        }
    }

    @Override
    public void onReceivedUnknownTowerId(String ctid) {
        synchronized (this) {
            receivedUnknownTowerId(ctid);
        }
    }


    /* used by client */
    @Override
    public void internetConnected(String ssid) {
        synchronized (this) {
            Log.i(TAG, String.format("--| EVENT: connected to %s |", ssid));
            this.ssid = ssid;
            currentState.onInternetConnected();
        }
    }

    @Override
    public void internetDisconnected() {
        synchronized (this) {
            Log.i(TAG, String.format("--| EVENT: disconnected |"));
            currentState.onInternetDisconnects();
        }
    }

    @Override
    public void receivedKnownTowerId() {
        synchronized (this) {
            Log.i(TAG, String.format("--| EVENT: known ctid |"));
            currentState.onReceivedKnownTowerId();
        }
    }

    @Override
    public void receivedUnknownTowerId(String ctid) {
        synchronized (this) {
            Log.i(TAG, String.format("--| EVENT: unknown ctid %s |", ctid));
            this.ctid = ctid;
            currentState.onReceivedUnknownTowerId();
        }
    }

    /* used by states */
    void toConnectedState() {
        setState(connected);
    }

    void toDisconnectedState() {
        setState(disconnected);
    }

    void toKnownAreaState() {
        setState(knownArea);
    }

    void toUnknownAreaState() {
        setState(unknownArea);
    }

    void toRouterAreaState() {
        setState(routerArea);
    }

    private void setState(IState state) {
        currentState = state;
        Log.i(TAG, String.format("CURRENT STATE: %S", currentState.toString()));
        engine.publish(state);
    }

    @Override
    public void start() {
        signal.subscribe(this);
        internet.subscribe(this);
    }

    @Override
    public void stop() {
        signal.unsubscribe(this);
        internet.unsubscribe(this);
    }

    @Override
    public void disableWifi() {
        Log.i(TAG, "----> DISABLING WIFI...");
        wifiDevice.disable();
    }

    @Override
    public void observeWifi() {
        Log.i(TAG, "----> OBSERVING WIFI...");
        wifiDevice.observe();
    }

    @Override
    public void haltWifiAct() {
        Log.i(TAG, "----> HALTING WIFI ACTIONS...");
        wifiDevice.halt();
    }

    @Override
    public void persist() {
        Log.i(TAG, "----> PERSISTING...");
        persistence.persist(ssid, ctid);
    }
}