package wifeye.app.android.mahorad.com.wifeye.wifi;

import wifeye.app.android.mahorad.com.wifeye.utilities.BinaryCountdown;
import wifeye.app.android.mahorad.com.wifeye.utilities.BinaryCountdownBuilder;
import wifeye.app.android.mahorad.com.wifeye.utilities.UnaryCountdown;
import wifeye.app.android.mahorad.com.wifeye.utilities.UnaryCountdownBuilder;

import static java.util.concurrent.TimeUnit.SECONDS;

public class WifiDevice {

    private static final String TAG = WifiDevice.class.getSimpleName();

    private static final int PEEK_REPEAT_COUNT = 10;
    private static final int WIFI_ENABLE_TIMEOUT = 300;
    private static final int WIFI_DISABLE_TIMEOUT = 60;

    private final IWifiHandler wifiHandler;

    private UnaryCountdown disablingTimer;
    private BinaryCountdown peekingTimer;

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
        if (peekingTimer != null && peekingTimer.isActive())
            return;
        if (disablingTimer != null && disablingTimer.isActive())
            return;
        halt();
        disablingTimer = new UnaryCountdownBuilder()
                .setEnacts(1)
                .setLength(WIFI_DISABLE_TIMEOUT, SECONDS)
                .setIntervalsAction(wifiHandler::disable)
                .build();
        disablingTimer.start();
    }

    public void enable() {
        if (isEnabled()) return;
        halt();
        wifiHandler.enable();
    }

    public void standby() {
        if (peekingTimer != null && peekingTimer.isActive())
            return;
        halt();
        peekingTimer = new BinaryCountdownBuilder()
                .setEnacts(PEEK_REPEAT_COUNT)
                .setMoreDelayedLength(WIFI_ENABLE_TIMEOUT, SECONDS)
                .setMoreDelayedAction(this::enable)
                .setLessDelayedLength(WIFI_DISABLE_TIMEOUT, SECONDS)
                .setLessDelayedAction(wifiHandler::disable)
                .setCompletionAction(wifiHandler::disable)
                .build();
        peekingTimer.start();
    }

    public void halt() {
        stopPeekingTimer();
        stopDisablingTimer();
    }

    private void stopPeekingTimer() {
        if (peekingTimer == null) return;
        peekingTimer.stop();
        peekingTimer = null;
    }

    private void stopDisablingTimer() {
        if (disablingTimer == null) return;
        disablingTimer.stop();
        disablingTimer = null;
    }
}
