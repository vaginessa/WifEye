package wifeye.app.android.mahorad.com.wifeye.state;

public class StateDisconnected extends State {

    public StateDisconnected(Engine machine) {
        super(machine);
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

    @Override
    public String toString() {
        return "DISCONNECTED";
    }
}