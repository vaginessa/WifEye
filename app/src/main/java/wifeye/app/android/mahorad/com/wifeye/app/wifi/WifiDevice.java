package wifeye.app.android.mahorad.com.wifeye.app.wifi;

import wifeye.app.android.mahorad.com.wifeye.app.consumers.IWifiDeviceStateConsumer;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.OngoingActionPublisher;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.WifiDeviceStatePublisher;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.WifiState;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.BinaryCountdown;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.BinaryCountdownBuilder;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.UnaryCountdown;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.UnaryCountdownBuilder;

import static java.util.concurrent.TimeUnit.SECONDS;
import static wifeye.app.android.mahorad.com.wifeye.app.constants.Constants.OBSERVE_REPEAT_COUNT;
import static wifeye.app.android.mahorad.com.wifeye.app.constants.Constants.WIFI_DISABLE_TIMEOUT;
import static wifeye.app.android.mahorad.com.wifeye.app.constants.Constants.WIFI_ENABLE_TIMEOUT;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.Action.*;
import static wifeye.app.android.mahorad.com.wifeye.app.publishers.WifiState.Disabled;

public class WifiDevice implements IWifiDeviceStateConsumer {

    private static final String TAG = WifiDevice.class.getSimpleName();

    private final IWifiHandler wifiHandler;
    private final OngoingActionPublisher actionPublisher;

    private UnaryCountdown disablingTimer;
    private BinaryCountdown observingTimer;

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
        wifiPublisher.subscribe(this);

    }

    public boolean isEnabled() {
        return wifiHandler.isEnabled();
    }

    public void disable() {
        synchronized (this) {
            if (!isEnabled())  return;
            if (isDisabling()) return;
            halt();
            actionPublisher.publish(DisablingMode);
            disablingTimer = new UnaryCountdownBuilder()
                    .setRunTimes(1)
                    .setDuration(WIFI_DISABLE_TIMEOUT, SECONDS)
                    .setIntervalsAction(wifiHandler::disable)
                    .build();
            disablingTimer.start();
        }
    }

    private boolean isDisabling() {
        return disablingTimer != null && disablingTimer.isActive();
    }

    public void observe() {
        synchronized (this) {
            if (isObserving()) return;
            halt();
            observingTimer = new BinaryCountdownBuilder()
                    .setRunTimes(OBSERVE_REPEAT_COUNT)
                    .setMoreDelayedLength(WIFI_ENABLE_TIMEOUT, SECONDS)
                    .setMoreDelayedAction(() ->  {
                        wifiHandler.enable();
                        actionPublisher.publish(ObserveModeDisabling);
                    })
                    .setLessDelayedLength(WIFI_DISABLE_TIMEOUT, SECONDS)
                    .setLessDelayedAction(() -> {
                        wifiHandler.disable();
                        actionPublisher.publish(ObserveModeEnabling);
                    })
                    .setCompletionAction(wifiHandler::disable)
                    .build();
            actionPublisher.publish(ObserveModeEnabling);
            observingTimer.start();
        }
    }

    private boolean isObserving() {
        return observingTimer != null && observingTimer.isActive();
    }

    public void halt() {
        synchronized (this) {
            stopObservingTimer();
            stopDisablingTimer();
        }
    }

    public long elapsed() {
        if (isDisabling())
            return disablingTimer.elapsed();
        if (isObserving())
            return observingTimer.elapsed();
        return -1;
    }

    private void stopObservingTimer() {
        if (observingTimer == null)
            return;
        observingTimer.stop();
        observingTimer = null;
        actionPublisher.publish(Halt);
    }

    private void stopDisablingTimer() {
        if (disablingTimer == null)
            return;
        disablingTimer.stop();
        disablingTimer = null;
        actionPublisher.publish(Halt);
    }

    @Override
    public void onWifiStateChanged(WifiState state) {
        if (state != Disabled)
            return;
        stopDisablingTimer();
    }
}
