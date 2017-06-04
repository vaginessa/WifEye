package wifeye.app.android.mahorad.com.wifeye.app.state;

public class StateDisconnected extends State {

    public StateDisconnected(StateMachine machine) {
        super(machine);
    }

    @Override
    public Type type() {
        return Type.DisConnected;
    }

    @Override
    public void onInternetConnected() {
        stateMachine.toConnectedState();
        stateMachine.haltWifiAct();
        stateMachine.persist();
    }

    @Override
    public void onInternetDisconnects() {/*do nothing*/}

    @Override
    public void onReceivedKnownTowerId() {
        stateMachine.toKnownAreaState();
        stateMachine.observeWifi();
    }

    @Override
    public void onReceivedUnknownTowerId() {
        stateMachine.toUnknownAreaState();
        stateMachine.disableWifi();
    }

}