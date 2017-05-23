package wifeye.app.android.mahorad.com.wifeye.app.consumers;

import wifeye.app.android.mahorad.com.wifeye.app.publishers.WifiState;

public interface IWifiDeviceStateConsumer {

    void onWifiStateChanged(WifiState state);

}
