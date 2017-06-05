package wifeye.app.android.mahorad.com.wifeye.app.wifi;

import wifeye.app.android.mahorad.com.wifeye.app.consumers.IWifiListener;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Action;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Wifi;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Internet;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.BinaryCountdown;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.UnaryCountdown;

import static java.util.concurrent.TimeUnit.SECONDS;
import static wifeye.app.android.mahorad.com.wifeye.app.constants.Constants.OBSERVE_REPEAT_COUNT;
import static wifeye.app.android.mahorad.com.wifeye.app.constants.Constants.WIFI_DISABLE_TIMEOUT;
import static wifeye.app.android.mahorad.com.wifeye.app.constants.Constants.WIFI_ENABLE_TIMEOUT;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.Type.DisablingMode;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.Type.Halt;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.Type.ObserveModeDisabling;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.Type.ObserveModeEnabling;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Wifi.State.Disabled;

public class WifiHandler implements IWifiListener {

    private static final String TAG = WifiHandler.class.getSimpleName();

    private final Wifi wifi;
    private final Action action;

    private final UnaryCountdown disablingTimer;
    private final BinaryCountdown observingTimer;

    /**
     * provides functionalities for controlling wifi on device
     * @param wifi Android wifi manager for controlling
     *                    wifi behaviours on the phone/tablet
     */
    public WifiHandler(Wifi wifi, Action action) {
        this.wifi = wifi;
        this.wifi.subscribe(this);
        this.action = action;
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

    public synchronized void disable() {
        if (!wifi.isEnabled()) return;
        if (isDisabling()) return;
        halt();
        action.setType(DisablingMode);
        disablingTimer.start();
    }

    public synchronized void observe() {
        if (isObserving()) return;
        halt();
        action.setType(ObserveModeEnabling);
        observingTimer.start();
    }

    private void enableWifi() {
        if (wifi.isEnabled()) {
            halt();
            return;
        }
        wifi.enable();
        action.setType(ObserveModeDisabling);
    }

    private void disableWifi() {
        if (Internet.connected()) {
            halt();
            return;
        }
        wifi.disable();
        action.setType(ObserveModeEnabling);
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

    public synchronized void halt() {
        stopObservingTimer();
        stopDisablingTimer();
    }

    private void stopObservingTimer() {
        observingTimer.stop();
        action.setType(Halt);
    }

    private void stopDisablingTimer() {
        disablingTimer.stop();
        action.setType(Halt);
    }

    @Override
    public synchronized void onWifiStateChanged(Wifi.State state) {
        if (state != Disabled)
            return;
        stopDisablingTimer();
    }
}
