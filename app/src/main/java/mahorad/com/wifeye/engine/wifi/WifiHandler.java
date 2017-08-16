package mahorad.com.wifeye.engine.wifi;

import android.content.Context;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import mahorad.com.wifeye.base.BaseApplication;
import mahorad.com.wifeye.di.qualifier.ApplicationContext;
import mahorad.com.wifeye.publisher.event.internet.RxInternetMonitor;
import mahorad.com.wifeye.publisher.event.wifi.RxWifiActionMonitor;
import mahorad.com.wifeye.util.BinaryCountdown;
import mahorad.com.wifeye.util.UnaryCountdown;

import static java.util.concurrent.TimeUnit.SECONDS;
import static mahorad.com.wifeye.util.Constants.OBSERVE_REPEAT_COUNT;
import static mahorad.com.wifeye.util.Constants.WIFI_DISABLE_TIMEOUT;
import static mahorad.com.wifeye.util.Constants.WIFI_ENABLE_TIMEOUT;

/**
 * Created by mahan on 2017-08-14.
 */

public class WifiHandler {

    private final UnaryCountdown disablingTimer;
    private final BinaryCountdown observingTimer;

    private static WifiAction wifiAction = WifiAction.Halt;

    private long elapsed;
    private boolean isStarted;
    private boolean connected;
    private Disposable internetStateDisposable;

    private Consumer<Long> observerConsumer = tick -> elapsed = tick;
    private Consumer<Long> disablerConsumer = tick -> elapsed = tick;

    private Context context;

    public WifiHandler(@ApplicationContext Context context) {
        this.context = context;
        disablingTimer = createDisablingTimer();
        observingTimer = createObservingTimer();
    }

    private UnaryCountdown createDisablingTimer() {
        UnaryCountdown disabler = UnaryCountdown
                .builder()
                .setDuration(WIFI_DISABLE_TIMEOUT, SECONDS)
                .setCondition(WifiDevice::isEnabled)
                .setCompletionAction(this::completionAction)
                .build();
        disabler.subscribe(disablerConsumer);
        return disabler;
    }

    private BinaryCountdown createObservingTimer() {
        BinaryCountdown observer = BinaryCountdown
                .builder()
                .setRunTimes(OBSERVE_REPEAT_COUNT)
                .setMoreDelayedLength(WIFI_ENABLE_TIMEOUT, SECONDS)
                .setMoreDelayedAction(this::observeModeEnable)
                .setMoreDelayedCondition(() -> !WifiDevice.isEnabled())
                .setLessDelayedLength(WIFI_DISABLE_TIMEOUT, SECONDS)
                .setLessDelayedAction(this::observeModeDisable)
                .setLessDelayedCondition(() -> WifiDevice.isEnabled())
                .setCompletionAction(this::completionAction)
                .startWithMoreDelayedAction()
                .build();
        observer.subscribe(observerConsumer);
        return observer;
    }

    private void completionAction() {
        if (connected) {
            halt();
            return;
        }
        WifiDevice.disable();
        halt();
    }

    private void observeModeEnable() {
        if (connected) {
            halt();
            return;
        }
        WifiDevice.enable();
        notify(WifiAction.ObserveModeDisabling);
    }

    private void observeModeDisable() {
        if (connected) {
            halt();
            return;
        }
        WifiDevice.disable();
        notify(WifiAction.ObserveModeEnabling);
    }

    public void start() {
        if (isStarted) return;
        isStarted = true;
        injectDependencies();
        internetStateDisposable = RxInternetMonitor
                .internetStateChanges(context)
                .subscribe(e -> connected = e.connected());

    }

    private void injectDependencies() {
        BaseApplication.component().inject(this);
    }

    public void runDisabler() {
        synchronized (this) {
            if (!canDisable()) {
                halt();
                return;
            }
            if (disabling()) return;
            halt();
            notify(WifiAction.DisablingMode);
            disablingTimer.start();
        }
    }

    private boolean canDisable() {
        return WifiDevice.isEnabled() && !connected;
    }

    public void runObserver() {
        synchronized (this) {
            if (connected) {
                halt();
                return;
            }
            if (observing()) return;
            halt();
            notify(WifiAction.ObserveModeEnabling);
            observingTimer.start();
        }
    }

    private boolean disabling() {
        return disablingTimer.isActive();
    }

    private boolean observing() {
        return observingTimer.isActive();
    }

    public void stop() {
        if (!isStarted) return;
        isStarted = false;
        internetStateDisposable.dispose();
        halt();
    }

    public void halt() {
        synchronized (this) {
            stopTimers();
            notify(WifiAction.Halt);
        }
    }

    private void stopTimers() {
        observingTimer.stop();
        disablingTimer.stop();
    }

    private static synchronized void notify(WifiAction wifiAction) {
        WifiHandler.wifiAction = wifiAction;
        RxWifiActionMonitor.notify(wifiAction);
    }

}
