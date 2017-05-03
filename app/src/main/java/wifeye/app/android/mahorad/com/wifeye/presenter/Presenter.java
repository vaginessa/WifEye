package wifeye.app.android.mahorad.com.wifeye.presenter;

import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;
import wifeye.app.android.mahorad.com.wifeye.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.MainService;
import wifeye.app.android.mahorad.com.wifeye.consumers.ICellTowerIdConsumer;
import wifeye.app.android.mahorad.com.wifeye.consumers.IOngoingActionConsumer;
import wifeye.app.android.mahorad.com.wifeye.consumers.IPersistenceConsumer;
import wifeye.app.android.mahorad.com.wifeye.consumers.IWifiSsidNameConsumer;
import wifeye.app.android.mahorad.com.wifeye.consumers.ISystemStateConsumer;
import wifeye.app.android.mahorad.com.wifeye.persist.IPersistence;
import wifeye.app.android.mahorad.com.wifeye.publishers.CellTowerIdPublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.OngoingActionPublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.OngoingActionPublisher.Action;
import wifeye.app.android.mahorad.com.wifeye.publishers.PersistencePublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.SystemStatePublisher;
import wifeye.app.android.mahorad.com.wifeye.publishers.WifiSsidNamePublisher;
import wifeye.app.android.mahorad.com.wifeye.state.IState;
import wifeye.app.android.mahorad.com.wifeye.utilities.Utilities;
import wifeye.app.android.mahorad.com.wifeye.view.IMainView;

public class Presenter implements
        IPresenter,
        IWifiSsidNameConsumer,
        ICellTowerIdConsumer,
        ISystemStateConsumer,
        IOngoingActionConsumer,
        IPersistenceConsumer {

    private final IMainView view;

    @Inject Utilities utils;
    @Inject WifiSsidNamePublisher ssidPublisher;
    @Inject OngoingActionPublisher actionPublisher;
    @Inject CellTowerIdPublisher ctidPublisher;
    @Inject SystemStatePublisher statePublisher;
    @Inject PersistencePublisher repoPublisher;
    @Inject IPersistence persistence;

    /**
     *
     * @param view
     */
    public Presenter(IMainView view) {
        this.view = view;
        MainApplication
                .mainComponent()
                .inject(this);
        subscribe();
    }

    private void subscribe() {
        ssidPublisher.subscribe(this);
        ctidPublisher.subscribe(this);
        statePublisher.subscribe(this);
        actionPublisher.subscribe(this);
        repoPublisher.subscribe(this);
    }

    @Override
    public void onCreate() {}

    @Override
    public void onPause() {}

    @Override
    public void onResume() { updateViewStates(); }

    @Override
    public void onDestroy() {}

    @Override
    public void startMainService() {
        Context context = MainApplication
                .appComponent()
                .context();
        Intent intent = new Intent(context, MainService.class);
        context.startService(intent);
        updateServiceState();
    }

    @Override
    public void stopMainService() {
        Context context = MainApplication
                .appComponent()
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
        updateRepositoryState();
    }

    private void updateActionState() {
        Action action = actionPublisher.action();
        String date = actionPublisher.date();
        view.updateActionState(action, date);
    }

    private void updateHotspotState() {
        String ssid = ssidPublisher.ssid();
        String date = ssidPublisher.date();
        view.updateHotspotState(ssid, date);
    }

    private void updateTowerIdState() {
        String ctid = ctidPublisher.ctid();
        String date = (ctid == null || ctid.equals(""))
                ? "" : ctidPublisher.date();
        view.updateTowerIdState(ctid, date);
    }

    private void updateEngineState() {
        String state = statePublisher.state();
        String date = statePublisher.date();
        view.updateEngineState(state, date);
    }

    private void updateRepositoryState() {
        view.updatePersistence(persistence.toString());
    }

    private void updateServiceState() {
        boolean enabled = utils.isRunning(MainService.class);
        view.updateServiceState(enabled, utils.simpleDate());
    }

    @Override
    public void onInternetConnected(String ssid) {
        view.updateHotspotState(ssid, utils.simpleDate());
    }

    @Override
    public void onInternetDisconnected() {
        view.updateHotspotState(null, utils.simpleDate());
    }

    @Override
    public void onReceivedKnownTowerId(String ctid) {
        view.updateTowerIdState(ctid, utils.simpleDate());
    }

    @Override
    public void onReceivedUnknownTowerId(String ctid) {
        view.updateTowerIdState(ctid, utils.simpleDate());
    }

    @Override
    public void onStateChanged(IState state) {
        view.updateEngineState(state.toString(), utils.simpleDate());
    }

    @Override
    public void onActionChanged(Action action) {
        view.updateActionState(action, utils.simpleDate());
    }

    @Override
    public void onDataPersisted() {
        String contents = persistence.toString();
        view.updatePersistence(contents);
    }
}
