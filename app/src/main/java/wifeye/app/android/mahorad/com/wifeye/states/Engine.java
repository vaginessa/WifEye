package wifeye.app.android.mahorad.com.wifeye.states;

import android.util.Log;

import wifeye.app.android.mahorad.com.wifeye.persist.IPersistence;
import wifeye.app.android.mahorad.com.wifeye.wifi.Wifi;

/**
 * A state machine and an actuator.
 * It keeps the system state as well as allowing
 * different states to make the right action.
 */
public class Engine implements IStateMachine, IActuator {

    private static final String TAG = Engine.class.getSimpleName();

    private final IState initial = new StateInitial(this);
    private final IState connected = new StateConnected(this);
    private final IState disconnected = new StateDisconnected(this);
    private final IState knownArea = new StateKnownArea(this);
    private final IState unknownArea = new StateUnknownArea(this);
    private final IState routerArea = new StateRouterArea(this);

    private IState currentState = initial;
    private String ssid;
    private String ctid;

    private final Wifi wifi;
    private final IPersistence persistence;

    public Engine(Wifi wifi, IPersistence persistence) {
        this.wifi = wifi;
        this.persistence = persistence;
    }

    /* used by client */
    @Override
    public void internetConnected(String ssid) {
        this.ssid = ssid;
        currentState.onInternetConnected();
    }

    @Override
    public void internetDisconnected() {
        currentState.onInternetDisconnects();
    }

    @Override
    public void receivedKnownTowerId() {
        currentState.onReceivedKnownTowerId();
    }

    @Override
    public void receivedUnknownTowerId(String ctid) {
        this.ctid = ctid;
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
    }

    @Override
    public void disableWifi() {
        Log.i(TAG, "---- DISABLING WIFI...");
        wifi.disable();
    }

    @Override
    public void standbyWifi() {
        Log.i(TAG, "---- BEGIN WIFI PEEK...");
        wifi.standby();
    }

    @Override
    public void halt() {
        Log.i(TAG, "---- CANCELLING...");
        wifi.halt();
    }

    @Override
    public void persist() {
        Log.i(TAG, "---- PERSISTING...");
        persistence.persist(ssid, ctid);
    }
}