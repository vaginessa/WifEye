package mahorad.com.wifeye.engine.state;

import mahorad.com.wifeye.engine.Engine;

public class StateNearbyArea extends State {

    public StateNearbyArea(Engine engine) {
        super(engine);
    }

    @Override
    public Type type() {
        return Type.NearbyArea;
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
        engine.observeWifi();
    }

    @Override
    public void onReceivedUnknownTowerId() {
        engine.toRemoteAreaState();
    }

}
