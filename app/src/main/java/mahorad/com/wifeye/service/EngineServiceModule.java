package mahorad.com.wifeye.service;

import dagger.Binds;
import dagger.Module;
import mahorad.com.wifeye.di.qualifier.engine.CloseRangeState;
import mahorad.com.wifeye.di.qualifier.engine.ConnectedState;
import mahorad.com.wifeye.di.qualifier.engine.DisconnectedState;
import mahorad.com.wifeye.di.qualifier.engine.InitialState;
import mahorad.com.wifeye.di.qualifier.engine.NearbyAreaState;
import mahorad.com.wifeye.di.qualifier.engine.RemoteAreaState;
import mahorad.com.wifeye.di.scope.PerService;
import mahorad.com.wifeye.engine.state.IState;
import mahorad.com.wifeye.engine.state.StateCloseRange;
import mahorad.com.wifeye.engine.state.StateConnected;
import mahorad.com.wifeye.engine.state.StateDisconnected;
import mahorad.com.wifeye.engine.state.StateInitial;
import mahorad.com.wifeye.engine.state.StateNearbyArea;
import mahorad.com.wifeye.engine.state.StateRemoteArea;

/**
 * Created by mahan on 2017-08-29.
 */

@Module
public abstract class EngineServiceModule {

    @PerService
    @Binds
    @InitialState
    abstract IState initial(StateInitial state);

    @PerService
    @Binds
    @ConnectedState
    abstract IState connected(StateConnected state);

    @PerService
    @Binds
    @DisconnectedState
    abstract IState disconnected(StateDisconnected state);

    @PerService
    @Binds
    @NearbyAreaState
    abstract IState nearbyArea(StateNearbyArea state);

    @PerService
    @Binds
    @RemoteAreaState
    abstract IState remoteArea(StateRemoteArea state);

    @PerService
    @Binds
    @CloseRangeState
    abstract IState closeRange(StateCloseRange state);

}
