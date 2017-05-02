package wifeye.app.android.mahorad.com.wifeye.presenter;

import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;
import wifeye.app.android.mahorad.com.wifeye.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.MainService;
import wifeye.app.android.mahorad.com.wifeye.consumers.ICellTowerIdConsumer;
import wifeye.app.android.mahorad.com.wifeye.consumers.IOngoingActionConsumer;
import wifeye.app.android.mahorad.com.wifeye.consumers.IWifiSsidNameConsumer;
import wifeye.app.android.mahorad.com.wifeye.consumers.ISystemStateConsumer;
import wifeye.app.android.mahorad.com.wifeye.publishers.OngoingActionPublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.OngoingActionPublisher.Action;
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
        updateViewStates();
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

    private void updateViewStates() {
        updateHotspotState();
        updateServiceState();
        updateTowerIdState();
        updateActionState();
        updateEngineState();
    }

    private void updateActionState() {
        Action action = MainApplication
                .mainComponent()
                .actionPublisher()
                .ongoingAction();
        view.updateActionState(action, getDate());
    }

    private void updateHotspotState() {
        String ssid = MainApplication
                .mainComponent()
                .ssidPublisher()
                .currentHotspot();
        view.updateHotspotState(ssid, getDate());
    }

    private void updateTowerIdState() {
        String ctid = MainApplication
                .mainComponent()
                .ctidPublisher()
                .currentTowerId();
        String date = (ctid == null || ctid == "")
                ? "" : getDate();
        view.updateTowerIdState(ctid, date);
    }

    private void updateEngineState() {
        String state = MainApplication
                .mainComponent()
                .stateMachine()
                .state();
        view.updateEngineState(state, getDate());
    }

    private void updateServiceState() {
        boolean enabled = MainApplication
                .mainComponent()
                .utilities()
                .isServiceRunning(MainService.class);
        view.updateServiceState(enabled, getDate());
    }

    @Override
    public void onInternetConnected(String ssid) {
        view.updateHotspotState(ssid, getDate());
    }

    @Override
    public void onInternetDisconnected() {
        view.updateHotspotState(null, getDate());
    }

    @Override
    public void onReceivedKnownTowerId(String ctid) { view.updateTowerIdState(ctid, getDate()); }

    @Override
    public void onReceivedUnknownTowerId(String ctid) {
        view.updateTowerIdState(ctid, getDate());
    }

    @Override
    public void onStateChanged(IState state) {
        view.updateEngineState(state.toString(), getDate());
    }

    @Override
    public void onActionChanged(Action action) {
        view.updateActionState(action, getDate());
    }

    private String getDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(Calendar.getInstance().getTime());
    }
}
