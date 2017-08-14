package mahorad.com.wifeye.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import mahorad.com.wifeye.di.component.ActivityComponent;
import mahorad.com.wifeye.di.component.DaggerActivityComponent;
import mahorad.com.wifeye.di.module.ActivityModule;
import mahorad.com.wifeye.di.qualifier.ApplicationContext;
import mahorad.com.wifeye.service.EngineService;
import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;

import static mahorad.com.wifeye.util.Constants.PERMISSIONS;
import static mahorad.com.wifeye.util.Utils.isRunning;
import static mahorad.com.wifeye.util.Utils.openPermissions;

public abstract class BaseActivity extends ActivityManagePermission {

    private ActivityComponent component;

    @Inject
    @ApplicationContext
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeComponent();
        injectDependencies();
    }

    protected void initializeComponent() {
        component = DaggerActivityComponent
                .builder()
                .applicationComponent(BaseApplication.component())
                .activityModule(new ActivityModule(this))
                .build();
    }

    public ActivityComponent component() {
        return component;
    }

    protected abstract void injectDependencies();
    
    /************************************************/

    public void toggleService() {
        if (isRunning(context, EngineService.class)) {
            stopEngineService();
        } else {
            handlePermissions();
        }
    }

    public void handlePermissions() {
        askCompactPermissions(PERMISSIONS, new PermissionResult() {
            @Override
            public void permissionGranted() { startEngineService(); }
            @Override
            public void permissionDenied() { finish(); }
            @Override
            public void permissionForeverDenied() {
                openPermissions(context);
            }
        });
    }

    public void startEngineService() {
        Intent intent = new Intent(context, EngineService.class);
        startService(intent);
    }

    public void stopEngineService() {
        Intent intent = new Intent(context, EngineService.class);
        stopService(intent);
    }


}
