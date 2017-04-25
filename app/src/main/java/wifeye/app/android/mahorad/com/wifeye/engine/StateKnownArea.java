package wifeye.app.android.mahorad.com.wifeye.engine;

public class StateKnownArea extends State {

    public StateKnownArea(Engine machine) {
        super(machine);
    }

    @Override
    public void onInternetConnected() {
        engine.toConnectedState();
        engine.persist();
    }

    @Override
    public void onInternetDisconnects() {/*do nothing*/}

    @Override
    public void onReceivedKnownTowerId() {
        engine.standbyWifi();
    }

    @Override
    public void onReceivedUnknownTowerId() {
        engine.toUnknownAreaState();
    }

    @Override
    public String toString() {
        return "KNOWN AREA: received a recognized tower identifier";
    }
}
