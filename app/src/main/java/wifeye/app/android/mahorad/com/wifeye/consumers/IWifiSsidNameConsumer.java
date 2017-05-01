package wifeye.app.android.mahorad.com.wifeye.consumers;

public interface IWifiSsidNameConsumer {

    void onInternetConnected(String ssid);

    void onInternetDisconnected();

}
