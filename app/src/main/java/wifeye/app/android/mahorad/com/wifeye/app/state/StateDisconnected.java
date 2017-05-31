package wifeye.app.android.mahorad.com.wifeye.app.state;

public class StateDisconnected extends State {

    public StateDisconnected(Engine machine) {
        super(machine);
    }

    @Override
    public Type type() {
        return Type.DisConnected;
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
        engine.toUnknownAreaState();
        engine.disableWifi();
    }

}