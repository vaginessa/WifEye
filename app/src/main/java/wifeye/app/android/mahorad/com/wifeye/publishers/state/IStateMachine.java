package wifeye.app.android.mahorad.com.wifeye.publishers.state;

interface IStateMachine {

    void internetConnected(String ssid);

    void internetDisconnected();

    void receivedKnownTowerId();

    void receivedUnknownTowerId(String ctid);

}
