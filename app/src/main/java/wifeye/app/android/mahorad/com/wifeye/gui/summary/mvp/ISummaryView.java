package wifeye.app.android.mahorad.com.wifeye.gui.summary.mvp;

import wifeye.app.android.mahorad.com.wifeye.app.publishers.Action;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.WifiState;

public interface ISummaryView {

    void updateHotspotState(String ssid, String date);

    void updateTowerIdState(String ctid, String date);

    void updateEngineState(String state, String date);

    void updateActionState(Action action, String date);

    void updateWifiDeviceState(WifiState state);

}
