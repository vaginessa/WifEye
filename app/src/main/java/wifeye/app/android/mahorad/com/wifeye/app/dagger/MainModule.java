package wifeye.app.android.mahorad.com.wifeye.app.dagger;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import wifeye.app.android.mahorad.com.wifeye.app.consumers.SsidTowerIdConsumer;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.annotations.ApplicationContext;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.annotations.ApplicationScope;
import wifeye.app.android.mahorad.com.wifeye.app.persist.IPersistence;
import wifeye.app.android.mahorad.com.wifeye.app.persist.MemoryPersistence;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.CellTowerIdPublisher;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.OngoingActionPublisher;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.PersistencePublisher;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.SystemStatePublisher;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.WifiDeviceStatePublisher;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.WifiSsidNamePublisher;
import wifeye.app.android.mahorad.com.wifeye.app.state.Engine;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;
import wifeye.app.android.mahorad.com.wifeye.app.wifi.AndroidWifiHandler;
import wifeye.app.android.mahorad.com.wifeye.app.wifi.IWifiHandler;
import wifeye.app.android.mahorad.com.wifeye.app.wifi.WifiDevice;

@Module(includes = ApplicationModule.class)
public class MainModule {

    @Provides
    @ApplicationScope
    public WifiSsidNamePublisher bssidPublisher(@ApplicationContext Context context) {
        return new WifiSsidNamePublisher(context);
    }

    @Provides
    @ApplicationScope
    public WifiDeviceStatePublisher wifiPublisher(@ApplicationContext Context context) {
        return new WifiDeviceStatePublisher(context);
    }

    @Provides
    @ApplicationScope
    public CellTowerIdPublisher towerIdPublisher(@ApplicationContext Context context,
                                                 IPersistence persistence) {
        return new CellTowerIdPublisher(context, persistence);
    }

    @Provides
    @ApplicationScope
    public IWifiHandler wifiHandler(@ApplicationContext Context context) {
        return new AndroidWifiHandler(context);
    }

    @Provides
    @ApplicationScope
    public WifiDevice wifiDevice(IWifiHandler wifiHandler,
                                 OngoingActionPublisher actionPublisher,
                                 WifiDeviceStatePublisher wifiPublisher) {
        return new WifiDevice(wifiHandler, actionPublisher, wifiPublisher);
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
    public SystemStatePublisher statePublisher() {
        return new SystemStatePublisher();
    }

    @Provides
    @ApplicationScope
    public Utilities utils() {
        return new Utilities();
    }
}
