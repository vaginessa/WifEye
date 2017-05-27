package wifeye.app.android.mahorad.com.wifeye.gui.mvp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import wifeye.app.android.mahorad.com.wifeye.app.MainApplication;
import wifeye.app.android.mahorad.com.wifeye.app.MainService;
import wifeye.app.android.mahorad.com.wifeye.app.constants.Constants;
import wifeye.app.android.mahorad.com.wifeye.app.utilities.Utilities;
import wifeye.app.android.mahorad.com.wifeye.gui.dagger.HomeScope;

public class HomePresenter {

    private final CompositeDisposable disposable = new CompositeDisposable();

    private final ServiceStateReceiver receiver = new ServiceStateReceiver();
    private class ServiceStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean enabled = intent
                    .getExtras()
                    .getBoolean(Constants.EXTRAS_SERVICE_STATE);
            view.updateFloatingButton(enabled);
            if (enabled)
                view.showSnackbar("Service Enabled.");
        }
    }

    @Inject
    HomeView view;

    @Inject @HomeScope
    ActivityManagePermission activity;

    @Inject
    Utilities utils;

    /**
     * Home Screen Presenter
     */
    public HomePresenter() {
        MainApplication
                .homeComponent()
                .inject(this);
    }

    public void onCreate() {
        disposable.add(mainButtonDisposable());
        disposable.add(mainMenuDisposable());
        view.selectFirstMenuItem();
    }

    public void onResume() {
        registerEngineEvents();
    }

    public void onPause() {
        unregisterEngineEvents();
    }

    public void onDestroy() {
        disposable.clear();
    }

    public Disposable mainButtonDisposable() {
        return view
                .observeActionButton()
                .subscribe(__ -> toggleService());
    }

    private Disposable mainMenuDisposable() {
        return view
                .observeNavigationView()
                .subscribe(view::onNavigationItemSelected);
    }

    public void toggleService() {
        if (utils.isRunning(MainService.class)) {
            stopMainService();
        } else {
            handlePermissions();
        }
    }

    public void handlePermissions() {
        activity.askCompactPermissions(Constants.PERMISSIONS, new PermissionResult() {
            @Override
            public void permissionGranted() { startMainService(); }
            @Override
            public void permissionDenied() { activity.finish(); }
            @Override
            public void permissionForeverDenied() {
                utils.openPermissions(activity);
            }
        });
    }

    public void startMainService() {
        Intent intent = new Intent(activity, MainService.class);
        activity.startService(intent);
    }

    public void stopMainService() {
        Intent intent = new Intent(activity, MainService.class);
        activity.stopService(intent);
    }

    public void registerEngineEvents() {
        boolean enabled = utils.isRunning(MainService.class);
        view.updateFloatingButton(enabled);
        activity.registerReceiver(receiver,
                new IntentFilter(Constants.INTENT_SERVICE_STATE));
    }

    public void unregisterEngineEvents() {
        activity.unregisterReceiver(receiver);
    }
}
