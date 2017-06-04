package wifeye.app.android.mahorad.com.wifeye.app.consumers;

import wifeye.app.android.mahorad.com.wifeye.app.publishers.Wifi;

public interface IWifiConsumer {

    void onWifiStateChanged(Wifi.State state);

}
