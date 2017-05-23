package wifeye.app.android.mahorad.com.wifeye.app.consumers;

public interface IWifiSsidNameConsumer {

    void onInternetConnected(String ssid);

    void onInternetDisconnected();

}
