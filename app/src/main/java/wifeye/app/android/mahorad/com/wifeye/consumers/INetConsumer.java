package wifeye.app.android.mahorad.com.wifeye.consumers;

public interface INetConsumer {

    void onInternetConnected(String ssid);

    void onInternetDisconnected();

}
