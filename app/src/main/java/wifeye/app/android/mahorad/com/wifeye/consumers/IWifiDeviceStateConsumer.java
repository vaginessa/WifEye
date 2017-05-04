package wifeye.app.android.mahorad.com.wifeye.consumers;

import wifeye.app.android.mahorad.com.wifeye.publishers.WifiState;

public interface IWifiDeviceStateConsumer {

    void onWifiStateChanged(WifiState state);

}
