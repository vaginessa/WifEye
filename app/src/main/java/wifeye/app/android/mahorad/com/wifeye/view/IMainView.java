package wifeye.app.android.mahorad.com.wifeye.view;

import wifeye.app.android.mahorad.com.wifeye.state.IState;

public interface IMainView {

    void updateSsidNameInfo(String ssid);

    void updateEngineState(IState state);

    void updateServiceState(boolean enabled);

    void updateOngoingAction(String event);
}
