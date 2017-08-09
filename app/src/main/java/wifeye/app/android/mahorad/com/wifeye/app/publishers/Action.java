package wifeye.app.android.mahorad.com.wifeye.app.publishers;

import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.ReplaySubject;
import wifeye.app.android.mahorad.com.wifeye.app.MainService;
import wifeye.app.android.mahorad.com.wifeye.app.events.ActionEvent;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.BinaryCountdown;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.UnaryCountdown;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

import static java.util.concurrent.TimeUnit.SECONDS;
import static wifeye.app.android.mahorad.com.wifeye.app.constants.Constants.OBSERVE_REPEAT_COUNT;
import static wifeye.app.android.mahorad.com.wifeye.app.constants.Constants.WIFI_DISABLE_TIMEOUT;
import static wifeye.app.android.mahorad.com.wifeye.app.constants.Constants.WIFI_ENABLE_TIMEOUT;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.Type.DisablingMode;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.Type.Halt;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.Type.ObserveModeDisabling;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.Type.ObserveModeEnabling;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Internet.connected;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Wifi.isDisabled;

public class Action {

    private static final String TAG = Action.class.getSimpleName();

    private static final ReplaySubject<ActionEvent> source = ReplaySubject.createWithSize(1);
    private static final Observable<ActionEvent> observable = source.distinctUntilChanged();

    private final Wifi wifi;

    private final UnaryCountdown disablingTimer;
    private final BinaryCountdown observingTimer;

    private Disposable serviceDisposable;

    private static Type type = Halt;
    private static Date date = Utilities.now();

    private long elapsed;
    private Consumer<Long> observerConsumer = tick -> elapsed = tick;
    private Consumer<Long> disablerConsumer = tick -> elapsed = tick;

    public enum Type {

        Halt("idle"),
        DisablingMode("disabling"),
        ObserveModeDisabling("..disabling.."),
        ObserveModeEnabling("..enabling..");

        private final String title;

        Type(String title) {
            this.title = title;
        }

        public String title() {
            return title;
        }
    }

    /**
     * provides functionalities for controlling wifi on device
     * @param wifi Android wifi manager for controlling
     *                    wifi behaviours on the phone/tablet
     */
    public Action(Wifi wifi) {
        this.wifi = wifi;
        disablingTimer = createDisablingTimer();
        observingTimer = createObservingTimer();
        observeServiceEvents();
    }

    private void observeServiceEvents() {
        if (serviceDisposable != null && !serviceDisposable.isDisposed())
            serviceDisposable.dispose();
        serviceDisposable = MainService
                .observable()
                .subscribe(e -> { if (!e) halt(); });
    }

    private UnaryCountdown createDisablingTimer() {
        UnaryCountdown disabler = UnaryCountdown
                .builder()
                .setDuration(WIFI_DISABLE_TIMEOUT, SECONDS)
                .setCondition(Wifi::isEnabled)
                .setCompletionAction(this::completeModeDisable)
                .build();
        disabler.subscribe(disablerConsumer);
        return disabler;
    }

    private long s;
    private BinaryCountdown createObservingTimer() {
        BinaryCountdown observer = BinaryCountdown
                .builder()
                .setRunTimes(OBSERVE_REPEAT_COUNT)
                .setMoreDelayedLength(WIFI_ENABLE_TIMEOUT, SECONDS)
                .setMoreDelayedAction(this::observeModeEnable)
                .setMoreDelayedCondition(Wifi::isDisabled)
                .setLessDelayedLength(WIFI_DISABLE_TIMEOUT, SECONDS)
                .setLessDelayedAction(this::observeModeDisable)
                .setLessDelayedCondition(Wifi::isEnabled)
                .setCompletionAction(this::completeModeDisable)
                .startWithMoreDelayedAction()
                .build();
        observer.subscribe(observerConsumer);
        return observer;
    }

    public void runDisabler() {
        synchronized (this) {
            if (!canDisable()) {
                halt();
                return;
            }
            if (disabling()) return;
            halt();
            notify(DisablingMode);
            disablingTimer.start();
        }
    }

    private boolean canDisable() {
        return !isDisabled() && !connected();
    }

    public void runObserver() {
        synchronized (this) {
            if (connected()) {
                halt();
                return;
            }
            if (observing()) return;
            halt();
            notify(ObserveModeEnabling);
            observingTimer.start();
        }
    }

    private void completeModeDisable() {
        if (connected()) {
            halt();
            return;
        }
        wifi.disable();
        halt();
    }

    private void observeModeEnable() {
        if (connected()) {
            halt();
            return;
        }
        wifi.enable();
        notify(ObserveModeDisabling);
    }

    private void observeModeDisable() {
        if (connected()) {
            halt();
            return;
        }
        wifi.disable();
        notify(ObserveModeEnabling);
    }

    private boolean disabling() {
        return disablingTimer.isActive();
    }

    private boolean observing() {
        return observingTimer.isActive();
    }

    public long elapsed() {
        return elapsed;
    }

    public void halt() {
        synchronized (this) {
            stopTimers();
            notify(Halt);
        }
    }

    private void stopTimers() {
        observingTimer.stop();
        disablingTimer.stop();
    }

    private static synchronized void notify(Type type) {
        if (type != Action.type) {
            Action.date = Utilities.now();
        }
        Action.type = type;
        source.onNext(ActionEvent.create(type, date));
    }

    public static Observable<ActionEvent> observable() {
        return observable;
    }

    public static Type type() {
        return type;
    }

    public static Date date() { return date; }

    public static ActionEvent lastEvent() {
        return ActionEvent.create(type, date);
    }
}
