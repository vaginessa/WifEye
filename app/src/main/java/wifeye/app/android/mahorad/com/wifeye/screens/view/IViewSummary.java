package wifeye.app.android.mahorad.com.wifeye.screens.view;

import wifeye.app.android.mahorad.com.wifeye.publishers.Action;
import wifeye.app.android.mahorad.com.wifeye.publishers.WifiState;

public interface IViewSummary {

    void updateHotspotState(String ssid, String date);

    void updateTowerIdState(String ctid, String date);

    void updateEngineState(String state, String date);

    void updateActionState(Action action, String date);

    void updateWifiDeviceState(WifiState state);

}
