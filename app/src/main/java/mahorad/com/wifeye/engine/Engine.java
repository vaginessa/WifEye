package mahorad.com.wifeye.engine;

import android.content.Context;

/**
 * A state machine and an actuator.
 * It keeps the system state as well as allowing
 * different states to make the right action.
 */
public class Engine implements
//        IStateMachine,
        IActuator {

//    private static final String TAG = Engine.class.getSimpleName();
//
//    private static final ReplaySubject<EngineEvent> source = ReplaySubject.createWithSize(1);
//    private static final Observable<EngineEvent> stateObservable = source.distinctUntilChanged();
//
//    private static IState.Type state = Initial;
//    private static Date date = Utils.now();
//
//    private final IState initial = new StateInitial(this);
//    private final IState connected = new StateConnected(this);
//    private final IState disconnected = new StateDisconnected(this);
//    private final IState knownArea = new StateKnownArea(this);
//    private final IState unknownArea = new StateUnknownArea(this);
//    private final IState routerArea = new StateRouterArea(this);
//
//    private IState currentState = initial;
//
//    private static Disposable internetDisposable;
//    private static Disposable locationDisposable;
//
//    @Inject Action action;
//
//    /**
//     * Creates a state machine engine with wifi controller
//     * and persistence abilities.
//     */
//    public Engine() {
//        MainApplication
//                .mainComponent()
//                .inject(this);
//    }
//
//    /* used by client */
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
//
//    /* used by states */
    public void toConnectedState() {
//        setState(connected);
    }
//
    public void toDisconnectedState() {
//        setState(disconnected);
    }
//
    public void toKnownAreaState() {
//        setState(knownArea);
    }
//
    public void toUnknownAreaState() {
//        setState(unknownArea);
    }
//
    public void toRouterAreaState() {
//        setState(routerArea);
    }
//
//    private void setState(IState state) {
//        currentState = state;
//        Log.i(TAG, String.format("CURRENT STATE: %S", currentState.toString()));
//        notify(state);
//    }
//
//    private static synchronized void notify(final IState state) {
//        if (state.type() != Engine.state) {
//            Engine.date = Utils.now();
//        }
//        Engine.state = state.type();
//        source.onNext(EngineEvent.create(state.type(), date));
//    }
//
//    public static Observable<EngineEvent> stateObservable() {
//        return stateObservable;
//    }
//
//    public static IState.Type state() {
//        return state;
//    }
//
//    public static Date date() {
//        return date;
//    }
//
    public void start(Context context) {
//        if (internetDisposable != null || locationDisposable != null)
//            return;
//        subscribeLocation(context);
//        subscribeInternet(context);
    }
//
//    private void subscribeLocation(Context context) {
//        locationDisposable = Location
//                .stateObservable(context)
//                .subscribe(this::onLocationEvent);
//    }
//
//    private void onLocationEvent(LocationEvent le) {
//        if (le.known())
//            receivedKnownTowerId();
//        else
//            receivedUnknownTowerId(le.ctid());
//    }
//
//    private void subscribeInternet(Context context) {
//        internetDisposable = Internet
//                .stateObservable(context)
//                .subscribe(this::onInternetEvent);
//    }
//
//    private void onInternetEvent(InternetEvent ie) {
//        if (ie.connected())
//            internetConnected(ie.ssid());
//        else
//            internetDisconnected();
//    }
//
    public void stop() {
//        if (locationDisposable != null)
//            locationDisposable.dispose();
//        if (internetDisposable != null)
//            internetDisposable.dispose();
//        locationDisposable = null;
//        internetDisposable = null;
    }
//
    @Override
    public void disableWifi() {
//        Executors
//                .newSingleThreadScheduledExecutor()
//                .schedule(() -> {
//                    Log.i(TAG, "----> DISABLING WIFI...");
//                    action.runDisabler();
//                }, 1, SECONDS);
    }
//
    @Override
    public void observeWifi() {
//        Executors
//                .newSingleThreadScheduledExecutor()
//                .schedule(() -> {
//                    Log.i(TAG, "----> OBSERVING WIFI...");
//                    action.runObserver();
//                }, 1, SECONDS);
    }
//
    @Override
    public void haltWifiAct() {
//        Log.i(TAG, "----> HALTING WIFI ACTIONS...");
//        action.halt();
    }
//
    @Override
    public void persist() {
//        Log.i(TAG, "----> PERSISTING...");
//        Persistence.persist(Internet.ssid(), Location.ctid());
    }
//
//    public static EngineEvent lastEvent() {
//        return EngineEvent.create(state, date);
//    }
}