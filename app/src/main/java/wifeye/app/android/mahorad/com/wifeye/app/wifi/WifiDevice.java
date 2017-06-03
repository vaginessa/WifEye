package wifeye.app.android.mahorad.com.wifeye.app.wifi;

import wifeye.app.android.mahorad.com.wifeye.app.consumers.IWifiDeviceStateConsumer;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.OngoingActionPublisher;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.WifiDeviceStatePublisher;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.WifiSsidNamePublisher;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.WifiState;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.BinaryCountdown;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.UnaryCountdown;

import static java.util.concurrent.TimeUnit.SECONDS;
import static wifeye.app.android.mahorad.com.wifeye.app.constants.Constants.OBSERVE_REPEAT_COUNT;
import static wifeye.app.android.mahorad.com.wifeye.app.constants.Constants.WIFI_DISABLE_TIMEOUT;
import static wifeye.app.android.mahorad.com.wifeye.app.constants.Constants.WIFI_ENABLE_TIMEOUT;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.DisablingMode;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.Halt;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.ObserveModeDisabling;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.ObserveModeEnabling;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.WifiState.Disabled;

public class WifiDevice implements IWifiDeviceStateConsumer {

    private static final String TAG = WifiDevice.class.getSimpleName();

    private final IWifiHandler wifiHandler;
    private final OngoingActionPublisher actionPublisher;

    private final UnaryCountdown disablingTimer;
    private final BinaryCountdown observingTimer;

    /**
     * provides functionalities for controlling wifi on device
     * @param wifiHandler Android wifi manager for controlling
     *                    wifi behaviours on the phone/tablet
     */
    public WifiDevice(IWifiHandler wifiHandler,
                      OngoingActionPublisher actionPublisher,
                      WifiDeviceStatePublisher wifiPublisher) {
        this.wifiHandler = wifiHandler;
        this.actionPublisher = actionPublisher;
        disablingTimer = createDisablingTimer();
        observingTimer = createObservingTimer();
        wifiPublisher.subscribe(this);
    }

    private UnaryCountdown createDisablingTimer() {
        return UnaryCountdown
                .builder()
                .setDuration(WIFI_DISABLE_TIMEOUT, SECONDS)
                .setCompletionAction(wifiHandler::disable)
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
                .setCompletionAction(wifiHandler::disable)
                .build();
    }

    public synchronized void disable() {
        if (!isEnabled()) return;
        if (isDisabling()) return;
        halt();
        actionPublisher.publish(DisablingMode);
        disablingTimer.start();
    }

    public synchronized void observe() {
        if (isObserving()) return;
        halt();
        actionPublisher.publish(ObserveModeEnabling);
        observingTimer.start();
    }

    private void enableWifi() {
        if (isEnabled()) {
            halt();
            return;
        }
        wifiHandler.enable();
        actionPublisher.publish(ObserveModeDisabling);
    }

    private void disableWifi() {
        if (isConnected()) {
            halt();
            return;
        }
        wifiHandler.disable();
        actionPublisher.publish(ObserveModeEnabling);
    }

    public boolean isEnabled() {
        return wifiHandler.isEnabled();
    }

    private boolean isDisabling() {
        return disablingTimer.isActive();
    }

    private boolean isConnected() {
        return WifiSsidNamePublisher.ssid() != null;
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
        actionPublisher.publish(Halt);
    }

    private void stopDisablingTimer() {
        disablingTimer.stop();
        actionPublisher.publish(Halt);
    }

    @Override
    public synchronized void onWifiStateChanged(WifiState state) {
        if (state != Disabled)
            return;
        stopDisablingTimer();
    }
}
