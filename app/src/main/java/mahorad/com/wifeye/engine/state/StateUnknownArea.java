package mahorad.com.wifeye.engine.state;

import mahorad.com.wifeye.engine.Engine;

public class StateUnknownArea extends State {

    public StateUnknownArea(Engine engine) {
        super(engine);
    }

    @Override
    public Type type() {
        return Type.UnknownArea;
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
        engine.toKnownAreaState();
        engine.observeWifi();
    }

    @Override
    public void onReceivedUnknownTowerId() {
        engine.disableWifi();
    }

}
