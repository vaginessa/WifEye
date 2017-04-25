package wifeye.app.android.mahorad.com.wifeye.consumers;

import android.content.Context;
import android.net.wifi.WifiManager;

import wifeye.app.android.mahorad.com.wifeye.persist.IPersistence;
import wifeye.app.android.mahorad.com.wifeye.states.Engine;
import wifeye.app.android.mahorad.com.wifeye.wifi.AndroidWifiHandler;
import wifeye.app.android.mahorad.com.wifeye.wifi.IWifiHandler;
import wifeye.app.android.mahorad.com.wifeye.wifi.Wifi;

/**
 * An even consumer which informs the engine about events.
 */
public class BaseConsumer implements INetConsumer, ITowerConsumer {

    private final Engine engine;

    public BaseConsumer(Engine engine) {
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

    public static BaseConsumer build(Context context, IPersistence persistence) {
        if (context == null)
            throw new IllegalArgumentException();
        if (persistence == null)
            throw new IllegalArgumentException();
        Object systemService = context.getSystemService(Context.WIFI_SERVICE);
        WifiManager wifiManager = (WifiManager) systemService;
        IWifiHandler androidWifiHandler = new AndroidWifiHandler(wifiManager);
        Wifi wifi = new Wifi(androidWifiHandler);
        Engine engine = new Engine(wifi, persistence);
        return new BaseConsumer(engine);
    }

}
