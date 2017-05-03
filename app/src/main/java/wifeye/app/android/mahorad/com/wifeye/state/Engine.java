package wifeye.app.android.mahorad.com.wifeye.state;

import android.util.Log;

import wifeye.app.android.mahorad.com.wifeye.persist.IPersistence;
import wifeye.app.android.mahorad.com.wifeye.publishers.SystemStatePublisher;
import wifeye.app.android.mahorad.com.wifeye.wifi.WifiDevice;

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

    private final SystemStatePublisher publisher;
    private final WifiDevice wifiDevice;
    private final IPersistence persistence;

    /**
     * Creates a state machine engine with wifi controller
     * and persistence abilities.
     *
     * @param wifiDevice
     * @param persistence
     * @param publisher
     */
    public Engine(WifiDevice wifiDevice, IPersistence persistence, SystemStatePublisher publisher) {
        this.wifiDevice = wifiDevice;
        this.persistence = persistence;
        this.publisher = publisher;
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
        publisher.publish(state);
    }

    @Override
    public void disableWifi() {
        Log.i(TAG, "----> DISABLING WIFI...");
        wifiDevice.disable();
    }

    @Override
    public void standbyWifi() {
        Log.i(TAG, "----> BEGIN WIFI OBSERVE...");
        wifiDevice.observe();
    }

    @Override
    public void halt() {
        Log.i(TAG, "----> CANCELLING...");
        wifiDevice.halt();
    }

    @Override
    public void persist() {
        Log.i(TAG, "----> PERSISTING...");
        persistence.persist(ssid, ctid);
    }
}