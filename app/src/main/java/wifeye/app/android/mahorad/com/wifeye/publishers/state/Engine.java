package wifeye.app.android.mahorad.com.wifeye.publishers.state;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import wifeye.app.android.mahorad.com.wifeye.consumers.IStateConsumer;
import wifeye.app.android.mahorad.com.wifeye.persist.IPersistence;
import wifeye.app.android.mahorad.com.wifeye.publishers.ISystemStatePublisher;
import wifeye.app.android.mahorad.com.wifeye.wifi.Wifi;

/**
 * A state machine and an actuator.
 * It keeps the system state as well as allowing
 * different states to make the right action.
 */
public class Engine implements IStateMachine, IActuator, ISystemStatePublisher {

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
    private final List<IStateConsumer> consumers;

    /**
     * Creates a state machine engine with wifi controller
     * and persistence abilities.
     *
     * @param wifi
     * @param persistence
     */
    public Engine(Wifi wifi, IPersistence persistence) {
        consumers = new ArrayList<>();
        this.wifi = wifi;
        this.persistence = persistence;
    }

    /* used by client */
    @Override
    public void internetConnected(String ssid) {
        Log.i(TAG, String.format("--| EVENT: connected to %s |", ssid));
        this.ssid = ssid;
        currentState.onInternetConnected();
    }

    @Override
    public void internetDisconnected() {
        Log.i(TAG, String.format("--| EVENT: disconnected |"));
        currentState.onInternetDisconnects();
    }

    @Override
    public void receivedKnownTowerId() {
        Log.i(TAG, String.format("--| EVENT: unknown ctid |"));
        currentState.onReceivedKnownTowerId();
    }

    @Override
    public void receivedUnknownTowerId(String ctid) {
        Log.i(TAG, String.format("--| EVENT: known ctid %s |", ctid));
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
        synchronized (this) {
            currentState = state;
            Log.i(TAG, String.format("CURRENT STATE: %S", currentState.toString()));
            publishStateChanged();
        }
    }

    @Override
    public void publishStateChanged() {
        for (final IStateConsumer consumer : consumers) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    consumer.onStateChanged(currentState);
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }

    @Override
    public void disableWifi() {
        Log.i(TAG, "----> DISABLING WIFI...");
        wifi.disable();
    }

    @Override
    public void standbyWifi() {
        Log.i(TAG, "----> BEGIN WIFI PEEK...");
        wifi.standby();
    }

    @Override
    public void halt() {
        Log.i(TAG, "----> CANCELLING...");
        wifi.halt();
    }

    @Override
    public void persist() {
        Log.i(TAG, "----> PERSISTING...");
        persistence.persist(ssid, ctid);
    }

    public boolean subscribe(IStateConsumer consumer) {
        return consumers.add(consumer);
    }
}