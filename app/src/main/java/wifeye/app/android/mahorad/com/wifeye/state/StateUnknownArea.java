package wifeye.app.android.mahorad.com.wifeye.state;

public class StateUnknownArea extends State {

    public StateUnknownArea(Engine machine) {
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
        engine.disableWifi();
    }

    @Override
    public String toString() {
        return "UNKNOWN AREA";
    }
}
