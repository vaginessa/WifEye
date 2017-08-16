package mahorad.com.wifeye.engine.state;

import mahorad.com.wifeye.engine.Engine;

public class StateNearbyArea extends State {

    public StateNearbyArea(Engine engine) {
        super(engine);
    }

    @Override
    public StateType type() {
        return StateType.NearbyArea;
    }

    @Override
    public void onInternetConnected(String ssid) {
        engine.toConnectedState();
        engine.haltWifiActions();
        engine.persistSsid(ssid);
    }

    @Override
    public void onInternetDisconnects() {/*do nothing*/}

    @Override
    public void onReceivedKnownTowerId(String ctid) {
        engine.observeWifi();
    }

    @Override
    public void onReceivedUnknownTowerId(String ctid) {
        engine.toRemoteAreaState();
    }

}
