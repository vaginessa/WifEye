package mahorad.com.wifeye.engine.state;

import mahorad.com.wifeye.engine.Engine;

public class StateInitial extends State {

    public StateInitial(Engine engine) {
        super(engine);
    }

    @Override
    public Type type() {
        return Type.Initial;
    }

    @Override
    public void onInternetConnected() {
        engine.toConnectedState();
    }

    @Override
    public void onInternetDisconnects() {
        engine.toDisconnectedState();
    }

    @Override
    public void onReceivedKnownTowerId() {
        engine.toKnownAreaState();
        engine.observeWifi();
    }

    @Override
    public void onReceivedUnknownTowerId() {
        engine.toUnknownAreaState();
    }

}