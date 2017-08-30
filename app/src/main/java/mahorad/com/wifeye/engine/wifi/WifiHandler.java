package mahorad.com.wifeye.engine.wifi;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import mahorad.com.wifeye.publisher.event.wifi.RxWifiActionMonitor;
import mahorad.com.wifeye.publisher.event.wifi.RxWifiActionTimerMonitor;
import mahorad.com.wifeye.util.BinaryCountdown;
import mahorad.com.wifeye.util.UnaryCountdown;
import timber.log.Timber;

import static java.util.concurrent.TimeUnit.SECONDS;
import static mahorad.com.wifeye.engine.wifi.WifiAction.DisablingMode;
import static mahorad.com.wifeye.engine.wifi.WifiAction.Halt;
import static mahorad.com.wifeye.engine.wifi.WifiAction.ObserveModeDisabling;
import static mahorad.com.wifeye.engine.wifi.WifiAction.ObserveModeEnabling;
import static mahorad.com.wifeye.engine.wifi.WifiDevice.isEnabled;
import static mahorad.com.wifeye.util.Constants.OBSERVE_REPEAT_COUNT;
import static mahorad.com.wifeye.util.Constants.WIFI_DISABLE_TIMEOUT;
import static mahorad.com.wifeye.util.Constants.WIFI_ENABLE_TIMEOUT;

/**
 * Created by mahan on 2017-08-14.
 */

public class WifiHandler {

    private static final String TAG = WifiHandler.class.getSimpleName();

    private static final Object sync = new Object();

    private final UnaryCountdown disablingTimer;
    private final BinaryCountdown observingTimer;

    private boolean isStarted;

    private Consumer<Long> tickConsumer = RxWifiActionTimerMonitor::notify;

    @Inject
    public WifiHandler() {
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
        disabler.subscribe(tickConsumer);
        return disabler;
    }

    private BinaryCountdown createObservingTimer() {
        BinaryCountdown observer = BinaryCountdown
                .builder()
                .setRunTimes(OBSERVE_REPEAT_COUNT)
                .setMoreDelayedLength(WIFI_ENABLE_TIMEOUT, SECONDS)
                .setMoreDelayedAction(this::observeModeEnable)
                .setMoreDelayedCondition(() -> !isEnabled())
                .setLessDelayedLength(WIFI_DISABLE_TIMEOUT, SECONDS)
                .setLessDelayedAction(this::observeModeDisable)
                .setLessDelayedCondition(() -> isEnabled())
                .setCompletionAction(this::completionAction)
                .startWithMoreDelayedAction()
                .build();
        observer.subscribe(tickConsumer);
        return observer;
    }

    private void completionAction() {
        WifiDevice.disable();
        haltActions();
    }

    private void observeModeEnable() {
        WifiDevice.enable();
        notify(ObserveModeDisabling);
    }

    private void observeModeDisable() {
        WifiDevice.disable();
        notify(ObserveModeEnabling);
    }

    public void start() {
        if (isStarted) return;
        isStarted = true;
        Timber.tag(TAG).d("starting wifi handler");
    }

    public void runDisabler() {
        synchronized (sync) {
            Timber.tag(TAG).d("running disabler?");
            if (disabling() || !isEnabled()) return;
            haltActions();
            notify(DisablingMode);
            Timber.tag(TAG).d("starting disabler timer");
            disablingTimer.start();
        }
    }

    public void runObserver() {
        synchronized (sync) {
            Timber.tag(TAG).d("running observer?");
            if (observing()) return;
            haltActions();
            notify(ObserveModeEnabling);
            Timber.tag(TAG).d("starting observer timer");
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
        Timber.tag(TAG).d("stopping wifi handler");
        haltActions();
    }

    public void haltActions() {
        synchronized (sync) {
            Timber.tag(TAG).d("halting wifi actions");
            stopTimers();
            notify(Halt);
        }
    }

    private void stopTimers() {
        observingTimer.stop();
        disablingTimer.stop();
    }

    private static void notify(WifiAction wifiAction) {
        synchronized (sync) {
            RxWifiActionMonitor.notify(wifiAction);
        }
    }

}
