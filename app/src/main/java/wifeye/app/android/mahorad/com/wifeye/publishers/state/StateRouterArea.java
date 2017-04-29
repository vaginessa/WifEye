package wifeye.app.android.mahorad.com.wifeye.publishers.state;

public class StateRouterArea extends State {

    public StateRouterArea(Engine machine) {
        super(machine);
    }

    @Override
    public void onInternetConnected() {
        engine.halt();
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

    @Override
    public String toString() {
        return "ROUTER AREA: received a tower identifier while connected to Internet";
    }
}