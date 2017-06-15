package wifeye.app.android.mahorad.com.wifeye.app.state;

import android.content.Context;
import android.util.Log;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;
import wifeye.app.android.mahorad.com.wifeye.app.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.app.events.EngineEvent;
import wifeye.app.android.mahorad.com.wifeye.app.events.InternetEvent;
import wifeye.app.android.mahorad.com.wifeye.app.events.LocationEvent;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Action;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Internet;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Location;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Persistence;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

import static wifeye.app.android.mahorad.com.wifeye.app.state.IState.Type.Initial;

/**
 * A state machine and an actuator.
 * It keeps the system state as well as allowing
 * different states to make the right action.
 */
public class Engine implements IStateMachine, IActuator {

    private static final String TAG = Engine.class.getSimpleName();

    private static final ReplaySubject<EngineEvent> source = ReplaySubject.create();
    private static final Observable<EngineEvent> observable = source.distinctUntilChanged();

    private static IState.Type state = Initial;
    private static Date date = Utilities.now();

    private final IState initial = new StateInitial(this);
    private final IState connected = new StateConnected(this);
    private final IState disconnected = new StateDisconnected(this);
    private final IState knownArea = new StateKnownArea(this);
    private final IState unknownArea = new StateUnknownArea(this);
    private final IState routerArea = new StateRouterArea(this);

    private IState currentState = initial;

    private static Disposable internetDisposable;
    private static Disposable locationDisposable;

    @Inject Action action;

    /**
     * Creates a state machine engine with wifi controller
     * and persistence abilities.
     */
    public Engine() {
        MainApplication
                .mainComponent()
                .inject(this);
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
        notify(state);
    }

    private void notify(final IState state) {
        Engine.state = state.type();
        Engine.date = Utilities.now();
        source.onNext(EngineEvent.create(state.type(), date));
    }

    public static Observable<EngineEvent> observable() {
        return observable;
    }

    public static IState.Type state() {
        return state;
    }

    public static Date date() {
        return date;
    }

    public void start(Context context) {
        if (internetDisposable != null || locationDisposable != null)
            return;
        subscribeLocation(context);
        subscribeInternet(context);
    }

    private void subscribeLocation(Context context) {
        locationDisposable = Location
                .observable(context)
                .observeOn(Schedulers.io())
                .subscribe(this::onLocationEvent);
    }

    private void onLocationEvent(LocationEvent le) {
        if (le.known())
            receivedKnownTowerId();
        else
            receivedUnknownTowerId(le.ctid());
    }

    private void subscribeInternet(Context context) {
        internetDisposable = Internet
                .observable(context)
                .observeOn(Schedulers.io())
                .subscribe(this::onInternetEvent);
    }

    private void onInternetEvent(InternetEvent ie) {
        if (ie.connected())
            internetConnected(ie.ssid());
        else
            internetDisconnected();
    }

    public void stop() {
        if (locationDisposable != null)
            locationDisposable.dispose();
        if (internetDisposable != null)
            internetDisposable.dispose();
        locationDisposable = null;
        internetDisposable = null;
    }

    @Override
    public void disableWifi() {
        Log.i(TAG, "----> DISABLING WIFI...");
        action.disable();
    }

    @Override
    public void observeWifi() {
        Log.i(TAG, "----> OBSERVING WIFI...");
        action.observe();
    }

    @Override
    public void haltWifiAct() {
        Log.i(TAG, "----> HALTING WIFI ACTIONS...");
        action.halt();
    }

    @Override
    public void persist() {
        Log.i(TAG, "----> PERSISTING...");
        Persistence.persist(Internet.ssid(), Location.ctid());
    }

    public static EngineEvent lastEvent() {
        return EngineEvent.create(state, date);
    }
}