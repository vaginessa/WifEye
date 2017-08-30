package mahorad.com.wifeye.engine.state;

import javax.inject.Inject;

import mahorad.com.wifeye.engine.Engine;

public class StateRemoteArea extends State {

    @Inject
    public StateRemoteArea(Engine engine) {
        super(engine);
    }

    @Override
    public StateType type() {
        return StateType.RemoteArea;
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
        engine.disableWifi();
    }

}
