package wifeye.app.android.mahorad.com.wifeye.gui.summary.mvp;

import java.util.Date;

import wifeye.app.android.mahorad.com.wifeye.app.publishers.Action;
import wifeye.app.android.mahorad.com.wifeye.app.publishers.WifiState;

public interface ISummaryView {

    void updateHotspotState(String ssid, Date date);

    void updateTowerIdState(String ctid, Date date);

    void updateEngineState(String state, Date date);

    void updateActionState(Action action, Date date);

    void updateWifiDeviceState(WifiState state);

}
