package wifeye.app.android.mahorad.com.wifeye.app.consumers;

public interface IInternetListener {

    void onInternetConnected(String ssid);

    void onInternetDisconnected();

}
