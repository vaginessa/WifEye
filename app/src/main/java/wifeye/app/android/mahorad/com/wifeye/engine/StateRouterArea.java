package wifeye.app.android.mahorad.com.wifeye.engine;

public class StateRouterArea extends State {

    public StateRouterArea(Engine machine) {
        super(machine);
    }

    @Override
    public void onInternetConnected() {/*do nothing*/}

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

    @Override
    public String toString() {
        return "ROUTER AREA: received a tower identifier while connected to Internet";
    }
}