package mahorad.com.wifeye.engine.state;

public interface IState {

    StateType type();

    void onInternetConnected(String ssid);

    void onInternetDisconnects();

    void onReceivedKnownTowerId(String ctid);

    void onReceivedUnknownTowerId(String ctid);
    
}
