package wifeye.app.android.mahorad.com.wifeye.consumers;

import wifeye.app.android.mahorad.com.wifeye.state.Engine;

/**
 * An even consumer which informs the engine about events.
 */
public class SsidAndCellConsumer implements ISsidNameConsumer, ICellTowerConsumer {

    private final Engine engine;

    public SsidAndCellConsumer(Engine engine) {
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
    public void onReceivedKnownTowerId() {
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
