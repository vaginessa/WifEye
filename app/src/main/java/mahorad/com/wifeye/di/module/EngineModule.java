package mahorad.com.wifeye.di.module;

import dagger.Module;
import dagger.Provides;
import mahorad.com.wifeye.di.qualifier.engine.CloseRangeState;
import mahorad.com.wifeye.di.qualifier.engine.ConnectedState;
import mahorad.com.wifeye.di.qualifier.engine.DisconnectedState;
import mahorad.com.wifeye.di.qualifier.engine.InitialState;
import mahorad.com.wifeye.di.qualifier.engine.NearbyAreaState;
import mahorad.com.wifeye.di.qualifier.engine.RemoteAreaState;
import mahorad.com.wifeye.di.scope.PerApplication;
import mahorad.com.wifeye.engine.Engine;
import mahorad.com.wifeye.engine.state.IState;
import mahorad.com.wifeye.engine.state.StateCloseRange;
import mahorad.com.wifeye.engine.state.StateConnected;
import mahorad.com.wifeye.engine.state.StateDisconnected;
import mahorad.com.wifeye.engine.state.StateInitial;
import mahorad.com.wifeye.engine.state.StateNearbyArea;
import mahorad.com.wifeye.engine.state.StateRemoteArea;

/**
 * Created by mahan on 2017-08-13.
 */
@Module
public class EngineModule {

    @Provides
    @PerApplication
    public Engine engine() {
        return new Engine();
    }

    @Provides
    @PerApplication
    @InitialState
    public IState initial(Engine engine) {
        return new StateInitial(engine);
    }

    @Provides
    @PerApplication
    @ConnectedState
    public IState connected(Engine engine) {
        return new StateConnected(engine);
    }

    @Provides
    @PerApplication
    @DisconnectedState
    public IState disconnected(Engine engine) {
        return new StateDisconnected(engine);
    }

    @Provides
    @PerApplication
    @NearbyAreaState
    public IState nearbyArea(Engine engine) {
        return new StateNearbyArea(engine);
    }

    @Provides
    @PerApplication
    @RemoteAreaState
    public IState remoteArea(Engine engine) {
        return new StateRemoteArea(engine);
    }

    @Provides
    @PerApplication
    @CloseRangeState
    public IState closeRange(Engine engine) {
        return new StateCloseRange(engine);
    }


}
