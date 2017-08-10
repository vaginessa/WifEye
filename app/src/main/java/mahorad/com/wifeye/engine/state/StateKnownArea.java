package mahorad.com.wifeye.engine.state;

import mahorad.com.wifeye.engine.Engine;

public class StateKnownArea extends State {

    public StateKnownArea(Engine engine) {
        super(engine);
    }

    @Override
    public Type type() {
        return Type.KnownArea;
    }

    @Override
    public void onInternetConnected() {
        engine.toConnectedState();
        engine.haltWifiAct();
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
        engine.toUnknownAreaState();
    }

}
