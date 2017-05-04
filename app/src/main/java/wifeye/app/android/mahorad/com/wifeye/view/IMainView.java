package wifeye.app.android.mahorad.com.wifeye.view;

import wifeye.app.android.mahorad.com.wifeye.publishers.OngoingActionPublisher.Action;
import wifeye.app.android.mahorad.com.wifeye.publishers.WifiState;

public interface IMainView {

    void updateHotspotState(String ssid, String date);

    void updateServiceState(boolean enabled, String date);

    void updateTowerIdState(String ctid, String date);

    void updateEngineState(String state, String date);

    void updateActionState(Action action, String date);

    void updatePersistence(String repository);

    void updateWifiDeviceState(WifiState state);
}
