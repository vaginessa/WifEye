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
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Wifi;
import wifeye.app.android.mahorad.com.wifeye.app.wifi.WifiHandler;

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

    @Inject
    Internet internet;
    @Inject
    Signal signal;
    @Inject
    Engine engine;
    @Inject
    WifiHandler wifiHandler;
    @Inject
    IPersistence persistence;

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
    public synchronized void onInternetConnected(String ssid) {
        internetConnected(ssid);
    }

    @Override
    public synchronized void onInternetDisconnected() {
        internetDisconnected();
    }

    @Override
    public synchronized void onReceivedKnownTowerId(String ctid) {
        receivedKnownTowerId();
    }

    @Override
    public synchronized void onReceivedUnknownTowerId(String ctid) {
        receivedUnknownTowerId(ctid);
    }


    /* used by client */
    @Override
    public synchronized void internetConnected(String ssid) {
        Log.i(TAG, String.format("--| EVENT: connected to %s |", ssid));
        currentState.onInternetConnected();
    }

    @Override
    public synchronized void internetDisconnected() {
        Log.i(TAG, String.format("--| EVENT: disconnected |"));
        currentState.onInternetDisconnects();
    }

    @Override
    public synchronized void receivedKnownTowerId() {
        Log.i(TAG, String.format("--| EVENT: known ctid |"));
        currentState.onReceivedKnownTowerId();
    }

    @Override
    public synchronized void receivedUnknownTowerId(String ctid) {
        Log.i(TAG, String.format("--| EVENT: unknown ctid %s |", ctid));
        currentState.onReceivedUnknownTowerId();
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
        engine.setState(state);
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
        wifiHandler.disable();
    }

    @Override
    public void observeWifi() {
        Log.i(TAG, "----> OBSERVING WIFI...");
        wifiHandler.observe();
    }

    @Override
    public void haltWifiAct() {
        Log.i(TAG, "----> HALTING WIFI ACTIONS...");
        wifiHandler.halt();
    }

    @Override
    public void persist() {
        Log.i(TAG, "----> PERSISTING...");
        persistence.persist(Internet.ssid(), Signal.ctid());
    }
}