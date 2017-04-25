package wifeye.app.android.mahorad.com.wifeye.engine;

public interface IState {

    void onInternetConnected();

    void onInternetDisconnects();

    void onReceivedKnownTowerId();

    void onReceivedUnknownTowerId();
    
}
