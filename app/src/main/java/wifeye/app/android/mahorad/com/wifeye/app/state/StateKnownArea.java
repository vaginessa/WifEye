package wifeye.app.android.mahorad.com.wifeye.app.state;

public class StateKnownArea extends State {

    public StateKnownArea(StateMachine machine) {
        super(machine);
    }

    @Override
    public Type type() {
        return Type.KnownArea;
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
        stateMachine.observeWifi();
    }

    @Override
    public void onReceivedUnknownTowerId() {
        stateMachine.toUnknownAreaState();
    }

}
