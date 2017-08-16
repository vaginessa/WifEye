package mahorad.com.wifeye.engine.state;

import mahorad.com.wifeye.engine.Engine;

public class StateInitial extends State {

    public StateInitial(Engine engine) {
        super(engine);
    }

    @Override
    public StateType type() {
        return StateType.Initial;
    }

    @Override
    public void onInternetConnected(String ssid) {
        engine.toConnectedState();
    }

    @Override
    public void onInternetDisconnects() {
        engine.toDisconnectedState();
    }

    @Override
    public void onReceivedKnownTowerId(String ctid) {
        engine.toNearbyAreaState();
        engine.observeWifi();
    }

    @Override
    public void onReceivedUnknownTowerId(String ctid) {
        engine.toRemoteAreaState();
    }

}