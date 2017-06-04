package wifeye.app.android.mahorad.com.wifeye.app.state;

public class StateInitial extends State {

    public StateInitial(StateMachine machine) {
        super(machine);
    }

    @Override
    public Type type() {
        return Type.Initial;
    }

    @Override
    public void onInternetConnected() {
        stateMachine.toConnectedState();
    }

    @Override
    public void onInternetDisconnects() {
        stateMachine.toDisconnectedState();
    }

    @Override
    public void onReceivedKnownTowerId() {
        stateMachine.toKnownAreaState();
        stateMachine.observeWifi();
    }

    @Override
    public void onReceivedUnknownTowerId() {
        stateMachine.toUnknownAreaState();
    }

}