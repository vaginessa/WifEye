package mahorad.com.wifeye.engine.state;

import mahorad.com.wifeye.engine.Engine;

public class StateRemoteArea extends State {

    public StateRemoteArea(Engine engine) {
        super(engine);
    }

    @Override
    public Type type() {
        return Type.RemoteArea;
    }

    @Override
    public void onInternetConnected() {
        engine.toConnectedState();
        engine.haltWifiActions();
        engine.persist();
    }

    @Override
    public void onInternetDisconnects() {/*do nothing*/}

    @Override
    public void onReceivedKnownTowerId() {
        engine.toNearbyAreaState();
        engine.observeWifi();
    }

    @Override
    public void onReceivedUnknownTowerId() {
        engine.disableWifi();
    }

}
