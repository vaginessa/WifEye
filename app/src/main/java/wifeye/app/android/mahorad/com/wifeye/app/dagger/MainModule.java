package wifeye.app.android.mahorad.com.wifeye.app.dagger;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.annotations.ApplicationContext;
import wifeye.app.android.mahorad.com.wifeye.app.dagger.annotations.ApplicationScope;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Location;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Action;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Wifi;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.Internet;
import wifeye.app.android.mahorad.com.wifeye.app.state.Engine;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;

@Module(includes = ApplicationModule.class)
public class MainModule {

    @Provides
    @ApplicationScope
    public Internet bssidPublisher() {
        return new Internet();
    }

    @Provides
    @ApplicationScope
    public Wifi wifi(@ApplicationContext Context context) {
        return new Wifi(context);
    }

    @Provides
    @ApplicationScope
    public Location towerIdPublisher() {
        return new Location();
    }

    @Provides
    @ApplicationScope
    public Action actionPublisher(Wifi wifi) {
        return new Action(wifi) ;
    }

    @Provides
    @ApplicationScope
    public Utilities utils() {
        return new Utilities();
    }

    @Provides
    @ApplicationScope
    public Engine engine() { return new Engine(); }
}
