package wifeye.app.android.mahorad.com.wifeye.app.publishers;

import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.subjects.ReplaySubject;
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

public class Action {

    private static final String TAG = Action.class.getSimpleName();

    private static final ReplaySubject<ActionEvent> source = ReplaySubject.createWithSize(1);
    private static final Observable<ActionEvent> observable = source.distinctUntilChanged();

    private final Wifi wifi;

    private final UnaryCountdown disablingTimer;
    private final BinaryCountdown observingTimer;

    private static Type type = Halt;
    private static Date date = Utilities.now();

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
    }

    private UnaryCountdown createDisablingTimer() {
        return UnaryCountdown
                .builder()
                .setDuration(WIFI_DISABLE_TIMEOUT, SECONDS)
                .setCompletionAction(wifi::disable)
                .build();
    }

    private BinaryCountdown createObservingTimer() {
        return BinaryCountdown
                .builder()
                .setRunTimes(OBSERVE_REPEAT_COUNT)
                .setMoreDelayedLength(WIFI_ENABLE_TIMEOUT, SECONDS)
                .setMoreDelayedAction(this::enableWifi)
                .setLessDelayedLength(WIFI_DISABLE_TIMEOUT, SECONDS)
                .setLessDelayedAction(this::disableWifi)
                .setCompletionAction(wifi::disable)
                .startWithMoreDelayedAction()
                .build();
    }

    public void disable() {
        synchronized (this) {
            if (!wifi.isEnabled()) return;
            if (isDisabling()) return;
            halt();
            notify(DisablingMode);
            disablingTimer.start();
        }
    }

    public void observe() {
        synchronized (this) {
            if (isObserving()) return;
            halt();
            notify(ObserveModeEnabling);
            observingTimer.start();
        }
    }

    private void enableWifi() {
        if (wifi.isEnabled()) {
            halt();
            return;
        }
        wifi.enable();
        notify(ObserveModeDisabling);
    }

    private void disableWifi() {
        if (Internet.connected()) {
            halt();
            return;
        }
        wifi.disable();
        notify(ObserveModeEnabling);
    }

    private boolean isDisabling() {
        return disablingTimer.isActive();
    }

    private boolean isObserving() {
        return observingTimer.isActive();
    }

    public long elapsed() {
        if (isDisabling())
            return disablingTimer.elapsed();
        if (isObserving())
            return observingTimer.elapsed();
        return 0;
    }

    public void halt() {
        synchronized (this) {
            stopObservingTimer();
            stopDisablingTimer();
        }
    }

    private void stopObservingTimer() {
        observingTimer.stop();
        notify(Halt);
    }

    private void stopDisablingTimer() {
        disablingTimer.stop();
        notify(Halt);
    }

    private void notify(Type type) {
        Action.type = type;
        Action.date = Utilities.now();
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
