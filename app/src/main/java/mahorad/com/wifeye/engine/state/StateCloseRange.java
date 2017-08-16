package mahorad.com.wifeye.engine.state;

import mahorad.com.wifeye.engine.Engine;

public class StateCloseRange extends State {

    public StateCloseRange(Engine engine) {
        super(engine);
    }

    @Override
    public StateType type() {
        return StateType.CloseRange;
    }

    @Override
    public void onInternetConnected(String ssid) {
        engine.haltWifiActions();
    }

    @Override
    public void onInternetDisconnects() {
        engine.toDisconnectedState();
    }

    @Override
    public void onReceivedKnownTowerId(String ctid) {/*do nothing*/}

    @Override
    public void onReceivedUnknownTowerId(String ctid) {
        engine.persistCtid(ctid);
    }

}