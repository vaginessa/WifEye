package wifeye.app.android.mahorad.com.wifeye.app.state;

public class StateConnected extends State {

    public StateConnected(StateMachine machine) {
        super(machine);
    }

    @Override
    public Type type() {
        return Type.Connected;
    }

    @Override
    public void onInternetConnected() {
        stateMachine.haltWifiAct(); // stopMainService any ongoing timeouts
        stateMachine.persist(); // persist another SSIDs perhaps
    }

    @Override
    public void onInternetDisconnects() {
        stateMachine.toDisconnectedState();
        stateMachine.disableWifi(); // startMainService a disabling timeout
    }

    @Override
    public void onReceivedKnownTowerId() {
        stateMachine.toRouterAreaState();
    }

    @Override
    public void onReceivedUnknownTowerId() {
        stateMachine.toRouterAreaState();
        stateMachine.persist(); // persist the location
    }

}