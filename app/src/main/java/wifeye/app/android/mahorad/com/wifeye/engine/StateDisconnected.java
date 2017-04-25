package wifeye.app.android.mahorad.com.wifeye.engine;

public class StateDisconnected extends State {

    public StateDisconnected(Engine machine) {
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
        engine.toKnownAreaState();
        engine.standbyWifi();
    }

    @Override
    public void onReceivedUnknownTowerId() {
        engine.toUnknownAreaState();
        engine.disableWifi();
    }

    @Override
    public String toString() {
        return "INTERNET DISCONNECTED: device is no longer connected to the Internet";
    }
}