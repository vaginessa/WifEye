package wifeye.app.android.mahorad.com.wifeye.app.state;

public class StateKnownArea extends State {

    public StateKnownArea(Engine machine) {
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
        engine.observeWifi();
    }

    @Override
    public void onReceivedUnknownTowerId() {
        engine.toUnknownAreaState();
    }

    @Override
    public String toString() {
        return "KNOWN AREA";
    }
}
