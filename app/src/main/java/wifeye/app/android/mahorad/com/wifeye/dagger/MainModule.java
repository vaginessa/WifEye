package wifeye.app.android.mahorad.com.wifeye.dagger;

import android.content.Context;
import android.net.wifi.WifiManager;

import dagger.Module;
import dagger.Provides;
import wifeye.app.android.mahorad.com.wifeye.consumers.SsidTowerIdConsumer;
import wifeye.app.android.mahorad.com.wifeye.persist.IPersistence;
import wifeye.app.android.mahorad.com.wifeye.persist.MemoryPersistence;
import wifeye.app.android.mahorad.com.wifeye.publishers.CellTowerIdPublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.OngoingActionPublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.PersistencePublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.SystemStatePublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.WifiSsidNamePublisher;
import wifeye.app.android.mahorad.com.wifeye.state.Engine;
import wifeye.app.android.mahorad.com.wifeye.utilities.Utilities;
import wifeye.app.android.mahorad.com.wifeye.wifi.AndroidWifiHandler;
import wifeye.app.android.mahorad.com.wifeye.wifi.IWifiHandler;
import wifeye.app.android.mahorad.com.wifeye.wifi.WifiDevice;

@Module(includes = AppModule.class)
public class MainModule {

    @Provides
    @ApplicationScope
    public IWifiHandler wifiHandler(Context context) {
        Object systemService = context.getSystemService(Context.WIFI_SERVICE);
        WifiManager wifiManager = (WifiManager) systemService;
        return new AndroidWifiHandler(wifiManager);
    }

    @Provides
    @ApplicationScope
    public WifiDevice wifiDevice(IWifiHandler wifiHandler, OngoingActionPublisher publisher) {
        return new WifiDevice(wifiHandler, publisher);
    }

    @Provides
    @ApplicationScope
    public IPersistence persistence(PersistencePublisher publisher) {
        return new MemoryPersistence(publisher);
    }

    @Provides
    @ApplicationScope
    public Engine engine(SystemStatePublisher publisher,
                         WifiDevice wifiDevice,
                         IPersistence persistence) {
        return new Engine(wifiDevice, persistence, publisher);
    }

    @Provides
    @ApplicationScope
    public SsidTowerIdConsumer ssidTowerIdConsumer(Engine engine) {
        return new SsidTowerIdConsumer(engine);
    }

    @Provides
    @ApplicationScope
    public PersistencePublisher persistencePublisher() {
        return new PersistencePublisher();
    }

    @Provides
    @ApplicationScope
    public OngoingActionPublisher actionPublisher() {
        return new OngoingActionPublisher() ;
    }

    @Provides
    @ApplicationScope
    public WifiSsidNamePublisher bssidPublisher(Context context,
                                                SsidTowerIdConsumer consumer) {
        WifiSsidNamePublisher publisher = new WifiSsidNamePublisher(context);
        publisher.subscribe(consumer);
        return publisher;
    }

    @Provides
    @ApplicationScope
    public CellTowerIdPublisher towerIdPublisher(Context context,
                                                 IPersistence persistence,
                                                 SsidTowerIdConsumer consumer) {
        CellTowerIdPublisher publisher = new CellTowerIdPublisher(context, persistence);
        publisher.subscribe(consumer);
        return publisher;
    }

    @Provides
    @ApplicationScope
    public SystemStatePublisher statePublisher() {
        return new SystemStatePublisher();
    }

    @Provides
    @ApplicationScope
    public Utilities utils() {
        return new Utilities();
    }
}
