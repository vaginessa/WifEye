package wifeye.app.android.mahorad.com.wifeye.state;

public class StateInitial extends State {

    public StateInitial(Engine machine) {
        super(machine);
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

    @Override
    public String toString() {
        return "INITIAL";
    }
}