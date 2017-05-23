package wifeye.app.android.mahorad.com.wifeye.app.consumers;

import wifeye.app.android.mahorad.com.wifeye.app.state.Engine;

/**
 * An even consumer which informs the engine about events.
 */
public class SsidTowerIdConsumer implements IWifiSsidNameConsumer, ICellTowerIdConsumer {

    private final Engine engine;

    public SsidTowerIdConsumer(Engine engine) {
        if (engine == null)
            throw new IllegalArgumentException();
        this.engine = engine;
    }

    @Override
    public void onInternetConnected(String ssid) {
        synchronized (this) {
            engine.internetConnected(ssid);
        }
    }

    @Override
    public void onInternetDisconnected() {
        synchronized (this) {
            engine.internetDisconnected();
        }
    }

    @Override
    public void onReceivedKnownTowerId(String ctid) {
        synchronized (this) {
            engine.receivedKnownTowerId();
        }
    }

    @Override
    public void onReceivedUnknownTowerId(String ctid) {
        synchronized (this) {
            engine.receivedUnknownTowerId(ctid);
        }
    }
}
