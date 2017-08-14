package mahorad.com.wifeye.engine.state;

import mahorad.com.wifeye.engine.Engine;

public class StateCloseRange extends State {

    public StateCloseRange(Engine engine) {
        super(engine);
    }

    @Override
    public Type type() {
        return Type.CloseRange;
    }

    @Override
    public void onInternetConnected() {
        engine.haltWifiActions();
    }

    @Override
    public void onInternetDisconnects() {
        engine.toDisconnectedState();
    }

    @Override
    public void onReceivedKnownTowerId() {/*do nothing*/}

    @Override
    public void onReceivedUnknownTowerId() {
        engine.persist();
    }

}