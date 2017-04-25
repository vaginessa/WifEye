package wifeye.app.android.mahorad.com.wifeye.states;

public class StateConnected extends State {

    public StateConnected(Engine machine) {
        super(machine);
    }

    @Override
    public void onInternetConnected() {
        engine.persist(); // persist another SSIDs perhaps
        engine.halt(); // stop any ongoing timeouts
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