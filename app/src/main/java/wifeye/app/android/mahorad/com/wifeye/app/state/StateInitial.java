package wifeye.app.android.mahorad.com.wifeye.app.state;

public class StateInitial extends State {

    public StateInitial(Engine machine) {
        super(machine);
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