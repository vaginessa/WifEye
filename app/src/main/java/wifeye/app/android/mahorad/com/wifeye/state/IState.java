package wifeye.app.android.mahorad.com.wifeye.state;

public interface IState {

    void onInternetConnected();

    void onInternetDisconnects();

    void onReceivedKnownTowerId();

    void onReceivedUnknownTowerId();
    
}
