package wifeye.app.android.mahorad.com.wifeye.wifi;

import wifeye.app.android.mahorad.com.wifeye.utilities.HourGlass;

public class Wifi {

    private static final int PEEK_REPEAT_COUNT = 6;
    private static final int PEEK_INTERVAL_SECONDS = 5;
    private static final int WIFI_DISABLE_TIMEOUT = 10;

    private final Runnable disableWifi = new Runnable() {
        @Override
        public void run() {
            wifiHandler.disable();
        }
    };

    private final Runnable toggleWifi = new Runnable() {
        @Override
        public void run() {
            if (wifiHandler.isEnabled())
                disable();
            else
                enable();
        }
    };

    private final IWifiHandler wifiHandler;

    private HourGlass disablingTimer;
    private HourGlass peekingTimer;

    /**
     * provides functionalities for controlling wifi on device
     * @param wifiHandler Android wifi manager for controlling
     *                    wifi behaviours on the phone/tablet
     */
    public Wifi(IWifiHandler wifiHandler) {
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
        disablingTimer = new HourGlass(disableWifi);
        disablingTimer.setFlips(1);
        disablingTimer.setDuration(WIFI_DISABLE_TIMEOUT);
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
        peekingTimer = new HourGlass(toggleWifi, disableWifi);
        peekingTimer.setFlips(PEEK_REPEAT_COUNT);
        peekingTimer.setDuration(PEEK_INTERVAL_SECONDS);
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
