package wifeye.app.android.mahorad.com.wifeye.dagger;

import android.content.Context;
import android.net.wifi.WifiManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import wifeye.app.android.mahorad.com.wifeye.consumers.SsidTowerIdConsumer;
import wifeye.app.android.mahorad.com.wifeye.persist.BasePersistence;
import wifeye.app.android.mahorad.com.wifeye.persist.IPersistence;
import wifeye.app.android.mahorad.com.wifeye.publishers.OngoingActionPublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.WifiSsidNamePublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.CellTowerIdPublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.SystemStatePublisher;
import wifeye.app.android.mahorad.com.wifeye.state.Engine;
import wifeye.app.android.mahorad.com.wifeye.utilities.Utilities;
import wifeye.app.android.mahorad.com.wifeye.wifi.AndroidWifiHandler;
import wifeye.app.android.mahorad.com.wifeye.wifi.IWifiHandler;
import wifeye.app.android.mahorad.com.wifeye.wifi.WifiDevice;

@Module(includes = ContextModule.class)
public class MainModule {

    @Provides @Singleton
    public IWifiHandler wifiHandler(Context context) {
        Object systemService = context.getSystemService(Context.WIFI_SERVICE);
        WifiManager wifiManager = (WifiManager) systemService;
        return new AndroidWifiHandler(wifiManager);
    }

    @Provides @Singleton
    public WifiDevice wifiDevice(IWifiHandler wifiHandler) {
        return new WifiDevice(wifiHandler);
    }

    @Provides @Singleton
    public IPersistence persistence() {
        return new BasePersistence();
    }

    @Provides @Singleton
    public Engine engine(SystemStatePublisher publisher,
                         WifiDevice wifiDevice,
                         IPersistence persistence) {
        return new Engine(wifiDevice, persistence, publisher);
    }

    @Provides @Singleton
    public SsidTowerIdConsumer ssidTowerIdConsumer(Engine engine) {
        return new SsidTowerIdConsumer(engine);
    }

    @Provides @Singleton
    public OngoingActionPublisher actionPublisher() {
        return new OngoingActionPublisher() ;
    }

    @Provides @Singleton
    public WifiSsidNamePublisher bssidPublisher(Context context,
                                                SsidTowerIdConsumer consumer) {
        WifiSsidNamePublisher publisher = new WifiSsidNamePublisher(context);
        publisher.subscribe(consumer);
        return publisher;
    }

    @Provides @Singleton
    public CellTowerIdPublisher towerIdPublisher(Context context,
                                                 IPersistence persistence,
                                                 SsidTowerIdConsumer consumer) {
        CellTowerIdPublisher publisher = new CellTowerIdPublisher(context, persistence);
        publisher.subscribe(consumer);
        return publisher;
    }

    @Provides @Singleton
    public SystemStatePublisher statePublisher() {
        return new SystemStatePublisher();
    }

    @Provides
    public Utilities utils() {
        return new Utilities();
    }
}
