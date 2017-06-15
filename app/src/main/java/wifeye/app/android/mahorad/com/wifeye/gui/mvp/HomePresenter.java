package wifeye.app.android.mahorad.com.wifeye.gui.mvp;

import android.content.Intent;

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

    private final CompositeDisposable composite = new CompositeDisposable();
    private Disposable disposable;

    @Inject HomeView view;
    @Inject Utilities utils;
    @Inject @HomeScope
    ActivityManagePermission activity;

    /**
     * Home Screen Presenter
     */
    public HomePresenter() {
        MainApplication
                .homeComponent()
                .inject(this);
    }

    public void onCreate() {
        composite.add(mainButtonDisposable());
        composite.add(mainMenuDisposable());
        view.selectFirstMenuItem();
    }

    public void onResume() {
        observeMainService();
    }

    public void onPause() {
        ignoreMainService();
    }

    public void onDestroy() {
        composite.clear();
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

    private void observeMainService() {
        if (isValid(disposable)) return;
        disposable = MainService
                .observable()
                .subscribe(bool -> view.updateFloatingButton(bool));
    }

    private boolean isValid(Disposable d) {
        return d != null && !d.isDisposed();
    }

    private void ignoreMainService() {
        if (disposable.isDisposed())
            return;
        disposable.dispose();
    }
}
