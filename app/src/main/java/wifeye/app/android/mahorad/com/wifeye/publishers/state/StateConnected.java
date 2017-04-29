package wifeye.app.android.mahorad.com.wifeye.publishers.state;

public class StateConnected extends State {

    public StateConnected(Engine machine) {
        super(machine);
    }

    @Override
    public void onInternetConnected() {
        engine.halt(); // stop any ongoing timeouts
        engine.persist(); // persist another SSIDs perhaps
    }

    @Override
    public void onInternetDisconnects() {
        engine.toDisconnectedState();
        engine.disableWifi(); // start a disabling timeout
    }

    @Override
    public void onReceivedKnownTowerId() {
        engine.toRouterAreaState();
    }

    @Override
    public void onReceivedUnknownTowerId() {
        engine.toRouterAreaState();
        engine.persist(); // persist the location
    }

    @Override
    public String toString() {
        return "INTERNET CONNECTED: device is now connected to the Internet";
    }
}