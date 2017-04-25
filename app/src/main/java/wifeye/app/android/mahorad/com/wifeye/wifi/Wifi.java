package wifeye.app.android.mahorad.com.wifeye.wifi;

import android.util.Log;

import wifeye.app.android.mahorad.com.wifeye.utilities.HourGlass;

public class Wifi {

    private static final String TAG = Wifi.class.getSimpleName();

    private static final int PEEK_REPEAT_COUNT = 6;
    private static final int PEEK_INTERVAL_SECONDS = 5;
    private static final int WIFI_DISABLE_TIMEOUT = 10;

    private final Runnable disableWifi = new Runnable() {
        @Override
        public void run() {
            wifiHandler.disable();
            stopPeekingTimer();
        }
    };

    private final Runnable disablingCompleted = new Runnable() {
        @Override
        public void run() {
            stopDisablingTimer();
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
        Log.d(TAG, "wifi is enabled");
        if (peekingTimer != null && peekingTimer.isActive())
            return;
        if (disablingTimer != null && disablingTimer.isActive())
            return;
        halt();
        disablingTimer = new HourGlass(disableWifi, disablingCompleted);
        disablingTimer.setFlips(1);
        disablingTimer.setDuration(WIFI_DISABLE_TIMEOUT);
        disablingTimer.setName("Wifi Disabling Timer");
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
        disablingTimer.setName("Wifi Peeking Timer");
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
