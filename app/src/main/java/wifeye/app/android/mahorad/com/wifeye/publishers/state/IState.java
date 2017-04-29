package wifeye.app.android.mahorad.com.wifeye.publishers.state;

public interface IState {

    void onInternetConnected();

    void onInternetDisconnects();

    void onReceivedKnownTowerId();

    void onReceivedUnknownTowerId();
    
}
