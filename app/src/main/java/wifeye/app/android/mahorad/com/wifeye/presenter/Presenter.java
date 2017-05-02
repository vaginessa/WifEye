package wifeye.app.android.mahorad.com.wifeye.presenter;

import android.content.Context;
import android.content.Intent;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;
import wifeye.app.android.mahorad.com.wifeye.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.MainService;
import wifeye.app.android.mahorad.com.wifeye.consumers.ICellTowerIdConsumer;
import wifeye.app.android.mahorad.com.wifeye.consumers.IOngoingActionConsumer;
import wifeye.app.android.mahorad.com.wifeye.consumers.IWifiSsidNameConsumer;
import wifeye.app.android.mahorad.com.wifeye.consumers.ISystemStateConsumer;
import wifeye.app.android.mahorad.com.wifeye.state.IState;
import wifeye.app.android.mahorad.com.wifeye.view.IMainView;

public class Presenter implements
        IPresenter,
        IWifiSsidNameConsumer,
        ICellTowerIdConsumer,
        ISystemStateConsumer,
        IOngoingActionConsumer  {

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
                .ctidPublisher()
                .subscribe(this);

        MainApplication
                .mainComponent()
                .statePublisher()
                .subscribe(this);

        MainApplication
                .mainComponent()
                .actionPublisher()
                .subscribe(this);
    }

    @Override
    public void onCreate() {}

    @Override
    public void onPause() {}

    @Override
    public void onResume() {
        updateServiceState();
    }

    @Override
    public void onDestroy() {}

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

    @Override
    public void handlePermissions() {
        handleCoarseLocationPermission();
        handleDiskReadWritePermissions();
    }

    private void handleCoarseLocationPermission() {
        String permission = PermissionUtils.Manifest_ACCESS_COARSE_LOCATION;
        final ActivityManagePermission activity = (ActivityManagePermission) this.view;
        activity.askCompactPermission(permission, new PermissionResult() {
            @Override
            public void permissionGranted() { }
            @Override
            public void permissionDenied() { activity.finish(); }
            @Override
            public void permissionForeverDenied() { }
        });
    }

    private void handleDiskReadWritePermissions() {
        String[] permissionRequests = {
                PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE,
                PermissionUtils.Manifest_READ_EXTERNAL_STORAGE
        };
        final ActivityManagePermission activity = (ActivityManagePermission) this.view;
        activity.askCompactPermissions(permissionRequests, new PermissionResult() {
            @Override
            public void permissionGranted() { }
            @Override
            public void permissionDenied() { activity.finish(); }
            @Override
            public void permissionForeverDenied() { }
        });
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
    public void onReceivedKnownTowerId(String ctid) { view.updateReceivedCtid(ctid); }

    @Override
    public void onReceivedUnknownTowerId(String ctid) {
        view.updateReceivedCtid(ctid);
    }

    @Override
    public void onStateChanged(IState state) {
        view.updateEngineState(state);
    }

    @Override
    public void onDisabling() {
        view.updateOngoingAction("DISABLING");
    }

    @Override
    public void onObserveModeDisabling() {
        view.updateOngoingAction("OBSERVE: Disabling");
    }

    @Override
    public void onObserveModeEnabling() {
        view.updateOngoingAction("OBSERVE: Enabling");
    }

    @Override
    public void onHalted() {
        view.updateOngoingAction("HALTED");
    }
}
