package mahorad.com.wifeye.engine.state;

import javax.inject.Inject;

import mahorad.com.wifeye.engine.Engine;

public class StateConnected extends State {

    @Inject
    public StateConnected(Engine engine) {
        super(engine);
    }

    @Override
    public StateType type() {
        return StateType.Connected;
    }

    @Override
    public void onInternetConnected(String ssid) {
        engine.haltWifiActions(); // stopMainService any ongoing timeouts
        engine.persistSsid(ssid); // persist another SSIDs perhaps
    }

    @Override
    public void onInternetDisconnects() {
        engine.toDisconnectedState();
        engine.disableWifi(); // startMainService a disabling timeout
    }

    @Override
    public void onReceivedKnownTowerId(String ctid) {
        engine.toCloseRangeState();
    }

    @Override
    public void onReceivedUnknownTowerId(String ctid) {
        engine.toCloseRangeState();
        engine.persistCtid(ctid); // persist the location
    }

}