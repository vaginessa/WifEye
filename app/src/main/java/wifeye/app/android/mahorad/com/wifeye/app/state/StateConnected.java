package wifeye.app.android.mahorad.com.wifeye.app.state;

public class StateConnected extends State {

    public StateConnected(Engine engine) {
        super(engine);
    }

    @Override
    public Type type() {
        return Type.Connected;
    }

    @Override
    public void onInternetConnected() {
        engine.haltWifiAct(); // stopMainService any ongoing timeouts
        engine.persist(); // persist another SSIDs perhaps
    }

    @Override
    public void onInternetDisconnects() {
        engine.toDisconnectedState();
        engine.disableWifi(); // startMainService a disabling timeout
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

}