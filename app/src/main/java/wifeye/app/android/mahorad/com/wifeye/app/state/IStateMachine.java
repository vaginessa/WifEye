package wifeye.app.android.mahorad.com.wifeye.app.state;

interface IStateMachine {

    void internetConnected(String ssid);

    void internetDisconnected();

    void receivedKnownTowerId();

    void receivedUnknownTowerId(String ctid);

}
