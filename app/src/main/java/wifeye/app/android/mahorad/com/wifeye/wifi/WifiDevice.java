package wifeye.app.android.mahorad.com.wifeye.wifi;

import wifeye.app.android.mahorad.com.wifeye.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.utilities.BinaryCountdown;
import wifeye.app.android.mahorad.com.wifeye.utilities.BinaryCountdownBuilder;
import wifeye.app.android.mahorad.com.wifeye.utilities.UnaryCountdown;
import wifeye.app.android.mahorad.com.wifeye.utilities.UnaryCountdownBuilder;

import static java.util.concurrent.TimeUnit.SECONDS;

public class WifiDevice {

    private static final String TAG = WifiDevice.class.getSimpleName();

    private static final int OBSERVE_REPEAT_COUNT = 10;
    private static final int WIFI_ENABLE_TIMEOUT = 300;
    private static final int WIFI_DISABLE_TIMEOUT = 60;

    private final IWifiHandler wifiHandler;

    private UnaryCountdown disablingTimer;
    private BinaryCountdown observingTimer;

    /**
     * provides functionalities for controlling wifi on device
     * @param wifiHandler Android wifi manager for controlling
     *                    wifi behaviours on the phone/tablet
     */
    public WifiDevice(IWifiHandler wifiHandler) {
        this.wifiHandler = wifiHandler;
    }

    public boolean isEnabled() {
        return wifiHandler.isEnabled();
    }

    public void disable() {
        if (!isEnabled()) return;
        if (observingTimer != null && observingTimer.isActive())
            return;
        if (disablingTimer != null && disablingTimer.isActive())
            return;
        halt();
        publishDisabling();
        disablingTimer = new UnaryCountdownBuilder()
                .setEnacts(1)
                .setLength(WIFI_DISABLE_TIMEOUT, SECONDS)
                .setIntervalsAction(wifiHandler::disable)
                .build();
        disablingTimer.start();
    }

    public void observe() {
        if (observingTimer != null && observingTimer.isActive())
            return;
        halt();
        observingTimer = new BinaryCountdownBuilder()
                .setEnacts(OBSERVE_REPEAT_COUNT)
                .setMoreDelayedLength(WIFI_ENABLE_TIMEOUT, SECONDS)
                .setMoreDelayedAction(() ->  {
                    wifiHandler.enable();
                    publishObserveModeDisabling();
                })
                .setLessDelayedLength(WIFI_DISABLE_TIMEOUT, SECONDS)
                .setLessDelayedAction(() -> {
                    wifiHandler.disable();
                    publishObserveModeEnabling();
                })
                .setCompletionAction(wifiHandler::disable)
                .build();
        observingTimer.start();
    }

    public void halt() {
        publishHalt();
        stopObservingTimer();
        stopDisablingTimer();
    }

    private void stopObservingTimer() {
        if (observingTimer == null) return;
        observingTimer.stop();
        observingTimer = null;
    }

    private void stopDisablingTimer() {
        if (disablingTimer == null) return;
        disablingTimer.stop();
        disablingTimer = null;
    }

    private void publishDisabling() {
        MainApplication
                .mainComponent()
                .actionPublisher()
                .publishDisabling();
    }

    private void publishObserveModeEnabling() {
        MainApplication
                .mainComponent()
                .actionPublisher()
                .publishObserveModeEnabling();
    }

    private void publishObserveModeDisabling() {
        MainApplication
                .mainComponent()
                .actionPublisher()
                .publishObserveModeDisabling();
    }

    private void publishHalt() {
        MainApplication
                .mainComponent()
                .actionPublisher()
                .publishHalt();
    }
}
