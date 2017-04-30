package wifeye.app.android.mahorad.com.wifeye.presenter;

import android.content.Context;
import android.content.Intent;

import wifeye.app.android.mahorad.com.wifeye.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.MainService;
import wifeye.app.android.mahorad.com.wifeye.consumers.ISsidNameConsumer;
import wifeye.app.android.mahorad.com.wifeye.consumers.ISystemStateConsumer;
import wifeye.app.android.mahorad.com.wifeye.state.IState;
import wifeye.app.android.mahorad.com.wifeye.view.IMainView;

public class Presenter implements IPresenter, ISsidNameConsumer, ISystemStateConsumer {

    private final IMainView view;

    public Presenter(IMainView view) {
        this.view = view;
        subscribe();
    }

    private void subscribe() {
        MainApplication
                .mainComponent()
                .ssidPublisher()
                .subscribe(this);

        MainApplication
                .mainComponent()
                .statePublisher()
                .subscribe(this);

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {
        updateServiceState();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void startMainService() {
        Context context = MainApplication
                .mainComponent()
                .context();
        Intent intent = new Intent(context, MainService.class);
        context.startService(intent);
        updateServiceState();

    }

    @Override
    public void stopMainService() {
        Context context = MainApplication
                .mainComponent()
                .context();
        Intent intent = new Intent(context, MainService.class);
        context.stopService(intent);
        updateServiceState();
    }

    private void updateServiceState() {
        boolean enabled = MainApplication
                .mainComponent()
                .utilities()
                .isServiceRunning(MainService.class);
        view.updateServiceState(enabled);
    }

    @Override
    public void onInternetConnected(String ssid) {
        view.updateSsidNameInfo(ssid);
    }

    @Override
    public void onInternetDisconnected() {
        view.updateSsidNameInfo(null);
    }

    @Override
    public void onStateChanged(IState state) {
        view.updateEngineState(state);
    }
}
