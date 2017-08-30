package mahorad.com.wifeye.di.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import mahorad.com.wifeye.R;
import mahorad.com.wifeye.di.qualifier.ApplicationContext;
import mahorad.com.wifeye.di.qualifier.engine.CloseRangeState;
import mahorad.com.wifeye.di.qualifier.engine.ConnectedState;
import mahorad.com.wifeye.di.qualifier.engine.DisconnectedState;
import mahorad.com.wifeye.di.qualifier.engine.InitialState;
import mahorad.com.wifeye.di.qualifier.engine.NearbyAreaState;
import mahorad.com.wifeye.di.qualifier.engine.RemoteAreaState;
import mahorad.com.wifeye.engine.Engine;
import mahorad.com.wifeye.engine.state.IState;
import mahorad.com.wifeye.engine.state.StateCloseRange;
import mahorad.com.wifeye.engine.state.StateConnected;
import mahorad.com.wifeye.engine.state.StateDisconnected;
import mahorad.com.wifeye.engine.state.StateInitial;
import mahorad.com.wifeye.engine.state.StateNearbyArea;
import mahorad.com.wifeye.engine.state.StateRemoteArea;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Mahan Rad on 2017-08-24.
 */

@Module
public abstract class ApplicationModule {

    @Binds
    @ApplicationContext
    abstract Context context(Application application);

    @Provides
    static CalligraphyConfig provideCalligraphyDefaultConfig() {
        return new CalligraphyConfig
                .Builder()
                .setDefaultFontPath("font/helvetica_neue_light.otf")
                .setFontAttrId(R.attr.fontPath)
                .build();
    }

    @Provides
    static CompositeDisposable compositeDisposable() {
        return new CompositeDisposable();
    }

    @Singleton
    @Provides
    static Engine engine() {
        return new Engine();
    }

    @Binds
    @InitialState
    abstract IState initial(StateInitial state);

    @Binds
    @ConnectedState
    abstract IState connected(StateConnected state);

    @Binds
    @DisconnectedState
    abstract IState disconnected(StateDisconnected state);

    @Binds
    @NearbyAreaState
    abstract IState nearbyArea(StateNearbyArea state);

    @Binds
    @RemoteAreaState
    abstract IState remoteArea(StateRemoteArea state);

    @Binds
    @CloseRangeState
    abstract IState closeRange(StateCloseRange state);

}
