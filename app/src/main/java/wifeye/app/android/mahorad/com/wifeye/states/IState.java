package wifeye.app.android.mahorad.com.wifeye.states;

public interface IState {

    void onInternetConnected();

    void onInternetDisconnects();

    void onReceivedKnownTowerId();

    void onReceivedUnknownTowerId();
    
}
