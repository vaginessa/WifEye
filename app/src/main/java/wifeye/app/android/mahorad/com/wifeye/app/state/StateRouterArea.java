package wifeye.app.android.mahorad.com.wifeye.app.state;

public class StateRouterArea extends State {

    public StateRouterArea(Engine machine) {
        super(machine);
    }

    @Override
    public Type type() {
        return Type.RouterArea;
    }

    @Override
    public void onInternetConnected() {
        engine.haltWifiAct();
    }

    @Override
    public void onInternetDisconnects() {
        engine.toDisconnectedState();
    }

    @Override
    public void onReceivedKnownTowerId() {/*do nothing*/}

    @Override
    public void onReceivedUnknownTowerId() {
        engine.persist();
    }

}