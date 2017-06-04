package wifeye.app.android.mahorad.com.wifeye.app.state;

public class StateRouterArea extends State {

    public StateRouterArea(StateMachine machine) {
        super(machine);
    }

    @Override
    public Type type() {
        return Type.RouterArea;
    }

    @Override
    public void onInternetConnected() {
        stateMachine.haltWifiAct();
    }

    @Override
    public void onInternetDisconnects() {
        stateMachine.toDisconnectedState();
    }

    @Override
    public void onReceivedKnownTowerId() {/*do nothing*/}

    @Override
    public void onReceivedUnknownTowerId() {
        stateMachine.persist();
    }

}