package mahorad.com.wifeye.engine.state;

import mahorad.com.wifeye.engine.Engine;

public class StateDisconnected extends State {

    public StateDisconnected(Engine engine) {
        super(engine);
    }

    @Override
    public StateType type() {
        return StateType.Disconnected;
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
        engine.toNearbyAreaState();
        engine.observeWifi();
    }

    @Override
    public void onReceivedUnknownTowerId(String ctid) {
        engine.toRemoteAreaState();
        engine.disableWifi();
    }

}