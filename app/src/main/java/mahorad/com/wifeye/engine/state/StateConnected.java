package mahorad.com.wifeye.engine.state;

import mahorad.com.wifeye.engine.Engine;

public class StateConnected extends State {

    public StateConnected(Engine engine) {
        super(engine);
    }

    @Override
    public Type type() {
        return Type.Connected;
    }

    @Override
    public void onInternetConnected() {
        engine.haltWifiActions(); // stopMainService any ongoing timeouts
        engine.persist(); // persist another SSIDs perhaps
    }

    @Override
    public void onInternetDisconnects() {
        engine.toDisconnectedState();
        engine.disableWifi(); // startMainService a disabling timeout
    }

    @Override
    public void onReceivedKnownTowerId() {
        engine.toCloseRangeState();
    }

    @Override
    public void onReceivedUnknownTowerId() {
        engine.toCloseRangeState();
        engine.persist(); // persist the location
    }

}