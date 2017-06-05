package wifeye.app.android.mahorad.com.wifeye.app.dagger;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.annotations.ApplicationContext;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.annotations.ApplicationScope;
import wifeye.app.android.mahorad.com.wifeye.app.persist.IPersistence;
import wifeye.app.android.mahorad.com.wifeye.app.persist.MemoryPersistence;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Engine;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Signal;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Action;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Wifi;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Internet;
import wifeye.app.android.mahorad.com.wifeye.app.state.StateMachine;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;
import wifeye.app.android.mahorad.com.wifeye.app.wifi.WifiHandler;

@Module(includes = ApplicationModule.class)
public class MainModule {

    @Provides
    @ApplicationScope
    public Internet bssidPublisher(@ApplicationContext Context context, Wifi wifi) {
        return new Internet(context, wifi);
    }

    @Provides
    @ApplicationScope
    public Wifi wifi(@ApplicationContext Context context) {
        return new Wifi(context);
    }

    @Provides
    @ApplicationScope
    public Signal towerIdPublisher(@ApplicationContext Context context,
                                   IPersistence persistence) {
        return new Signal(context, persistence);
    }

    @Provides
    @ApplicationScope
    public WifiHandler wifiDevice(Wifi wifi, Action actionPublisher) {
        return new WifiHandler(wifi, actionPublisher);
    }

    @Provides
    @ApplicationScope
    public IPersistence persistence() {
        return new MemoryPersistence();
    }

    @Provides
    @ApplicationScope
    public Action actionPublisher() {
        return new Action() ;
    }

    @Provides
    @ApplicationScope
    public Engine statePublisher() {
        return new Engine();
    }

    @Provides
    @ApplicationScope
    public Utilities utils() {
        return new Utilities();
    }

    @Provides
    @ApplicationScope
    public StateMachine engine() { return new StateMachine(); }
}
