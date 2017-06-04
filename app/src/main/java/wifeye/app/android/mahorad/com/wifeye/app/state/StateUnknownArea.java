package wifeye.app.android.mahorad.com.wifeye.app.state;

public class StateUnknownArea extends State {

    public StateUnknownArea(StateMachine machine) {
        super(machine);
    }

    @Override
    public Type type() {
        return Type.UnknownArea;
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
        stateMachine.disableWifi();
    }

}
